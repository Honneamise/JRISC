import java.io.*;
import java.util.*;

public class Assembler 
{
    public static String error = null;

    public static void setError(int line, String msg)
    {
        error = new String("Line : " + line + "\n" + msg);              
    }

    //check for valid label format
    public static boolean checkLabel(String label)
    {
        char c = label.charAt(0);

        if(!Character.isLetter(c)) {return false; }

        for (int i = 1; i < label.length(); i++) 
        {
            c = label.charAt(i);

            if (!(Character.isLetterOrDigit(c) || c == '_')) 
            {
                return false;
            }
        }

        return true;
    }

    //check valid number format
    public static boolean checkNumber(String number)
    {
        int base = 10;
        
        try
        {
            if (number.startsWith("0b")) 
            {
                base = 2;
                Integer.parseInt(number.substring(2), base);
            } 
            else if (number.startsWith("0x")) 
            {
                base = 16;
                Integer.parseInt(number.substring(2), base);
            } else 
            {
                Integer.parseInt(number,base);
            }
        } catch (Exception ex){ return false; }

        return true;
    }

    //call this only after check
    public static int parseNumber(String number)
    {
        int num = 0;
        int base = 10;
        
        try
        {
            if (number.startsWith("0b")) 
            {
                base = 2;
                num = Integer.parseInt(number.substring(2), base);
            } 
            else if (number.startsWith("0x")) 
            {
                base = 16;
                num = Integer.parseInt(number.substring(2), base);
            } else 
            {
                num = Integer.parseInt(number,base);
            }
        } catch (Exception ex){ }

        return num;
    }

    public static boolean checkSignedImm(int num, int bits)
    {
        int min = -(int)(Math.pow(2, bits-1));
        int max = (int)(Math.pow(2, bits-1) - 1);

        return (num >= min && num <= max);
    }
   
    public static boolean checkUnsignedImm(int num, int bits)
    {
        int max = (int)(Math.pow(2, bits) - 1);

        return (num >= 0 && num <= max);
    }

    public static Map<String,Integer> Parse(String source)
    {
        error = null;

        int address = 0;
        String line = null; 
        int lineCounter = 0;

        Map<String,Integer> labels= new HashMap<>();

        //we should add sets according to directives
        Isa isa = new Isa();
        isa.addSet(RV32I.set);
        isa.addSet(RV32M.set);

        BufferedReader reader = new BufferedReader(new StringReader(source));
        
        while(true) 
        {
            int index = 0;
            lineCounter++;

            try { line = reader.readLine(); } 
            catch (Exception e) { setError(lineCounter, "Error reading line"); return null; }
            
            if (line==null) { break; }; //end of stream

            //1: remove comment if present
            index = line.indexOf(';');
            if (index != -1) { line = line.substring(0, index); }
            
            //2: trim the line
            line = line.trim();

            //3: parse the label if present
            index = line.indexOf(':');
            if (index != -1) 
            {
                String labelName = line.substring(0, index).trim();
                line = line.substring(index + 1).trim();

                //validate label
                if(labelName.isEmpty()) { setError(lineCounter, "Empty label found"); return null; }

                if(!checkLabel(labelName))  { setError(lineCounter, "Invalid label format"); return null; }
                
                if(labels.get(labelName)!=null) { setError(lineCounter, "Duplicated label"); return null; }

                if(Register.get(labelName)!=-1) { setError(lineCounter, "Label cannot be a register"); return null; }
                    
                if(isa.keywords().contains(labelName)) { setError(lineCounter, "Label cannot be a keyword"); return null; }

                //finally !!!
                labels.put(labelName, address);
                
            }

            //4: parse the instruction if present
            if (!line.isEmpty()) 
            {
                String[] lineTokens = line.split("[\\s]+");
                
                int[] insParams = isa.get(lineTokens[0]);

                if(insParams == null) 
                { 
                    setError(lineCounter,"Instruction not found"); return null; 
                }

                switch(insParams[0])
                {
                    case Ins.TYPE_Ie:
                    {
                        if(lineTokens.length != 1) { setError(lineCounter,"Instruction require 0 parameters"); return null; }
                    } 
                    break;

                    case Ins.TYPE_R:
                    case Ins.TYPE_I:
                    case Ins.TYPE_Is:
                    case Ins.TYPE_S:
                    case Ins.TYPE_B:
                    {
                        if(lineTokens.length != 4) { setError(lineCounter,"Instruction require 3 parameters"); return null; }
                    }
                    break;

                    case Ins.TYPE_U:
                    case Ins.TYPE_J:
                    {
                        if(lineTokens.length != 3) { setError(lineCounter,"Instruction require 2 parameters"); return null; }
                    }
                    break;

                    default :
                    { 
                        setError(lineCounter,"Instruction type not found"); 
                        return null; 
                    }
                    
                }  
                
                address += 4; //advance address
            }
        }

        return labels;
    }

    public static byte[] Build(String source, Map<String,Integer> labels)
    {
        error = null;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int address = 0;

        String line = null; 
        int lineCounter = 0;

        //we should add sets according to directives
        Isa isa = new Isa();
        isa.addSet(RV32I.set);
        isa.addSet(RV32M.set);

        BufferedReader reader = new BufferedReader(new StringReader(source));
        
        while(true) 
        {
            try { line = reader.readLine(); } 
            catch (Exception e) { setError(lineCounter, "setError reading line"); return null; }
            
            if (line==null) { break; }; //end of stream

            int index = 0;
            lineCounter++;

            //1: remove comment if present
            index = line.indexOf(';');
            if (index != -1) { line = line.substring(0, index); }
            
            //2: trim the line
            line = line.trim();

            //3: remove the label if present
            index = line.indexOf(':');
            if (index != -1) 
            {
                line = line.substring(index + 1).trim();   
            }

            //4: generate the instruction if present
            if (!line.isEmpty()) 
            {
                String[] lineTokens = line.split("[\\s]+");

                int[] insParams = isa.get(lineTokens[0]);

                switch(insParams[0])
                {
                    case Ins.TYPE_R:
                    {
                        int opcode = insParams[1];
                        int funct3 = insParams[2];
                        int funct7 = insParams[3];

                        int rd = Register.get(lineTokens[1]);
                        int rs1 = Register.get(lineTokens[2]);
                        int rs2 = Register.get(lineTokens[3]);

                        if(rd<0 || rs1<0 || rs2<0){ setError(lineCounter,"Invalid register selected"); return null; }

                        int val = Ins.encodeR(opcode, rd, rs1, rs2, funct3, funct7);
       
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    }
                    break;

                    case Ins.TYPE_I:
                    {
                        int opcode = insParams[1];
                        int funct3 = insParams[2];

                        int rd = Register.get(lineTokens[1]);
                        int rs1 = Register.get(lineTokens[2]);

                        if(rd<0 || rs1<0){ setError(lineCounter,"Invalid register selected"); return null; }

                        int imm = 0;

                        if(labels.containsKey(lineTokens[3])) //is a label ?
                        {
                            imm = labels.get(lineTokens[3]);
                        }
                        else //is a number ?
                        {
                            if(!checkNumber(lineTokens[3])) { setError(lineCounter,"Invalid number format"); return null; }
                            imm = parseNumber(lineTokens[3]);
                        }

                        if(!checkSignedImm(imm, 12)) { setError(lineCounter,"Invalid immediate"); return null; }

                        int val = Ins.encodeI(opcode, rd, rs1, imm, funct3);
                        
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    }
                    break;

                    case Ins.TYPE_Ie:
                    {
                        int opcode = insParams[1];
                        int funct12 = insParams[2];

                        int val = Ins.encodeIe(opcode, funct12);
       
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    }
                    break;

                    case Ins.TYPE_Is:
                    {
                        int opcode = insParams[1];
                        int funct3 = insParams[2];
                        int funct7 = insParams[3];

                        int rd = Register.get(lineTokens[1]);
                        int rs1 = Register.get(lineTokens[2]);

                        if(rd<0 || rs1<0){ setError(lineCounter,"Invalid register selected"); return null; }

                        int imm = 0;

                        if(labels.containsKey(lineTokens[3])) //is a label ?
                        {
                            imm = labels.get(lineTokens[3]);
                        }
                        else //is a number ?
                        {
                            if(!checkNumber(lineTokens[3])) { setError(lineCounter,"Invalid number format"); return null; }
                            imm = parseNumber(lineTokens[3]);
                        }

                        if(!checkUnsignedImm(imm, 5)) { setError(lineCounter,"Invalid immediate"); return null; }

                        int val = Ins.encodeIs(opcode, rd, rs1, imm, funct3, funct7);
                        
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    }
                    break;

                    case Ins.TYPE_S:
                    {
                        int opcode = insParams[1];
                        int funct3 = insParams[2];

                        int rs1 = Register.get(lineTokens[1]);
                        int rs2 = Register.get(lineTokens[2]);

                        if(rs1<0 || rs2<0){ setError(lineCounter,"Invalid register selected"); return null; }

                        int imm = 0;

                        if(labels.containsKey(lineTokens[3])) //is a label ?
                        {
                            imm = labels.get(lineTokens[3]);
                        }
                        else //is a number ?
                        {
                            if(!checkNumber(lineTokens[3])) { setError(lineCounter,"Invalid number format"); return null; }
                            imm = parseNumber(lineTokens[3]);
                        }

                        if(!checkSignedImm(imm, 12)) { setError(lineCounter,"Invalid immediate"); return null; }

                        int val = Ins.encodeS(opcode, rs1, rs2, imm, funct3);
                    
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    }
                    break;

                    case Ins.TYPE_B:
                    {
                        int opcode = insParams[1];
                        int funct3 = insParams[2];

                        int rs1 = Register.get(lineTokens[1]);
                        int rs2 = Register.get(lineTokens[2]);

                        if(rs1<0 || rs2<0){ setError(lineCounter,"Invalid register selected"); return null; }

                        int imm = 0;

                        if(labels.containsKey(lineTokens[3])) //is a label ?
                        {
                            imm = labels.get(lineTokens[3]);

                            imm -= address; 
                        }
                        else //is a number ?
                        {
                            if(!checkNumber(lineTokens[3])) { setError(lineCounter,"Invalid number format"); return null; }
                            imm = parseNumber(lineTokens[3]);
                        }

                        if(!checkSignedImm(imm, 12)) { setError(lineCounter,"Invalid immediate"); return null; }

                        int val = Ins.encodeB(opcode, rs1, rs2, imm, funct3);
                    
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    }
                    break;

                    case Ins.TYPE_U:
                    {
                        int opcode = insParams[1];

                        int rd = Register.get(lineTokens[1]);

                        if(rd<0){ setError(lineCounter,"Invalid register selected"); return null; }

                        int imm = 0;

                        if(labels.containsKey(lineTokens[2])) //is a label ?
                        {
                            imm = labels.get(lineTokens[2]);
                        }
                        else //is a number ?
                        {
                            if(!checkNumber(lineTokens[2])) { setError(lineCounter,"Invalid number format"); return null; }
                            imm = parseNumber(lineTokens[2]);
                        }

                        if(!checkUnsignedImm(imm, 20)) { setError(lineCounter,"Invalid immediate"); return null; }

                        int val = Ins.encodeU(opcode, rd, imm);
                    
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    }
                    break;

                    case Ins.TYPE_J:
                    {
                        int opcode = insParams[1];

                        int rd = Register.get(lineTokens[1]);

                        if(rd<0){ setError(lineCounter,"Invalid register selected"); return null; }

                        int imm = 0;

                        if(labels.containsKey(lineTokens[2])) //is a label ?
                        {
                            imm = labels.get(lineTokens[2]);

                            imm -= address;
                        }
                        else //is a number ?
                        {
                            if(!checkNumber(lineTokens[2])) { setError(lineCounter,"Invalid number format"); return null; }
                            imm = parseNumber(lineTokens[2]);
                        }

                        if(!checkSignedImm(imm, 20)) { setError(lineCounter,"Invalid immediate"); return null; }

                        int val = Ins.encodeJ(opcode, rd, imm);
                    
                        output.write((val >> 24) & 0xFF);
                        output.write((val >> 16) & 0xFF);
                        output.write((val >> 8)  & 0xFF);
                        output.write((val >> 0)  & 0xFF);

                        System.out.printf("0x%08X\n",val);
                    };
                    break;
                    
                    default :
                    { 
                        setError(lineCounter,"Instruction type not found"); 
                        return null; 
                    }
                    
                }  
                
                address += 4; //advance address
            }
            
        }

        return output.toByteArray();
    }
}