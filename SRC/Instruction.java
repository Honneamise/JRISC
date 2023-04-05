class Ins
{
    public static final int TYPE_R  = 0; 
    public static final int TYPE_I  = 1;
    public static final int TYPE_Ie = 2;
    public static final int TYPE_Is = 3;
    public static final int TYPE_S  = 4;
    public static final int TYPE_B  = 5;
    public static final int TYPE_U  = 6;
    public static final int TYPE_J  = 7;

    String name;
    int type;
    int opcode;

    public Ins(String name, int type, int opcode)
    {
        this.name = name;
        this.type = type;
        this.opcode = opcode;
    }

    public static int encodeR(int opcode, int rd, int rs1, int rs2, int funct3, int funct7) 
    {
        return (funct7 << 25) | (rs2 << 20) | (rs1 << 15) | (funct3 << 12) | (rd << 7) | opcode;
    }

    public static int encodeI(int opcode, int rd, int rs1, int imm, int funct3) 
    {
        return (imm << 20) | (rs1 << 15) | (funct3 << 12) | (rd << 7) | opcode;
    }

    public static int encodeIe(int opcode, int funct12) 
    {
        return encodeI(opcode, 0, 0, funct12, 0);
    }

    public static int encodeIs(int opcode, int rd, int rs1, int imm, int funct3, int funct7) 
    {
        return (funct7 <<25) | (imm << 20) | (rs1 << 15) | (funct3 << 12) | (rd << 7) | opcode;
    }

    public static int encodeS(int opcode, int rs2, int rs1, int imm, int funct3) 
    {
        int imm11to5 = (imm >> 5) & 0x7F;
        int imm4to0 = imm & 0x1F;
        return (imm11to5 << 25) |(rs2 << 20) | (rs1 << 15) | (funct3 << 12) | (imm4to0 << 7) | opcode;
    }

    public static int encodeB(int opcode, int rs1, int rs2, int imm, int funct3) 
    {
        int imm12 = (imm >> 12) & 1;
        int imm11 = (imm >> 11) & 1;
        int imm10to5 = (imm >> 5) & 0x3F;
        int imm4to1 = (imm >> 1) & 0xF;
        
        return (imm12 << 31) | (imm10to5 << 25) | (rs2 << 20) | (rs1 << 15) | (funct3 << 12) | (imm4to1 << 8) | (imm11 << 7) | opcode;
    }

    public static int encodeU(int opcode, int rd, int imm) 
    {
        return (imm << 12) | (rd << 7) | opcode;
    }

    public static int encodeJ(int opcode, int rd, int imm) 
    {
        int imm20 = (imm >> 20) & 0x1;
        int imm19to12 = (imm >> 12) & 0xFF;
        int imm11 = (imm >> 11) & 0x1;
        int imm10to1 = (imm >> 1) & 0x3FF;
        
        return (imm20 << 31) | (imm10to1 << 21) | (imm11 << 20) | (imm19to12 << 12) | (rd << 7) | opcode;
    }

    public static void decodeR(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int rd = (instruction >> 7) & 0x1F;
        int funct3 = (instruction >> 12) & 0x7;
        int rs1 = (instruction >> 15) & 0x1F;
        int rs2 = (instruction >> 20) & 0x1F;
        int funct7 = (instruction >> 25) & 0x7F;
    }
    
    public static void decodeI(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int rd = (instruction >> 7) & 0x1F;
        int funct3 = (instruction >> 12) & 0x7; 
        int rs1 = (instruction >> 15) & 0x1F;
        int imm = (instruction >> 20);
    }

    public static void decodeIe(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int rd = (instruction >> 7) & 0x1F;
        int funct3 = (instruction >> 12) & 0x7; 
        int rs1 = (instruction >> 15) & 0x1F;
        int funct12 = (instruction >> 20);       
    }

    public static void decodeIs(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int rd = (instruction >> 7) & 0x1F;
        int funct3 = (instruction >> 12) & 0x7;
        int rs1 = (instruction >> 15) & 0x1F;
        int imm = (instruction >> 20) & 0x1F;
        int funct7 =  (instruction >> 25) & 0x7F;       
    }
    
    public static void decodeS(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int funct3 = (instruction >> 12) & 0x7;
        int rs1 = (instruction >> 15) & 0x1F;
        int rs2 = (instruction >> 20) & 0x1F;

        int imm11to5 = (instruction >> 25) & 0x7F;
        int imm4to0 = (instruction >> 7) & 0x1F;       
        int imm = (imm11to5 << 5) | imm4to0;
    }
    
    public static void decodeB(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int funct3 = (instruction >> 12) & 0x7;
        int rs1 = (instruction >> 15) & 0x1F;
        int rs2 = (instruction >> 20) & 0x1F;

        int imm12 = (instruction >> 31) & 0x1;
        int imm11 = (instruction >> 7) & 0x1;
        int imm10to5 = (instruction >> 25) & 0x3F;
        int imm4to1 = (instruction >> 8) & 0xF;
        int imm = (imm12 << 12) | (imm11 << 11) | (imm10to5 << 5) | (imm4to1 << 1);   
    }
    
    public static void decodeU(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int rd = (instruction >> 7) & 0x1F;
        int imm = (instruction >> 12) & 0xFFFFF;
    }
    
    public static void decodeJ(int instruction) 
    {
        int opcode = instruction & 0x7F;
        int rd = (instruction >> 7) & 0x1F;

        int imm20 = (instruction >> 31) & 0x1;
        int imm19to12 = (instruction >> 12) & 0xFF;
        int imm11 = (instruction >> 20) & 0x1;
        int imm10to1 = (instruction >> 21) & 0x3FF;
        int imm = (imm20 << 19) | (imm19to12 << 11) | (imm11 << 10) | (imm10to1 << 0);
    }
}

class InsR extends Ins
{
    int rd;
    int rs1;
    int rs2;
    int funct3;
    int funct7;

    public InsR(String name, int opcode, int rd, int rs1, int rs2, int funct3, int funct7)
    {
        super(name, Ins.TYPE_R, opcode);

        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.funct3 = funct3;
        this.funct7 = funct7;
    }
}

class InsI extends Ins
{
    int rd;
    int rs1;
    int funct3;
    int imm;

    public InsI(String name, int opcode, int rd, int rs1, int funct3, int imm)
    {
        super(name, Ins.TYPE_I, opcode);
    
        this.rd = rd;
        this.rs1 = rs1;
        this.funct3 = funct3;
        this.imm = imm;
    }
}

class InsIe extends Ins
{
    int rd;
    int rs1;
    int funct3; 
    int funct12;      

    public InsIe(String name, int opcode, int rd, int rs1, int funct3, int funct12)
    {
        super(name, Ins.TYPE_Ie, opcode);
    
        this.rd = rd;
        this.rs1 = rs1;
        this.funct3 = funct3;
        this.funct12 = funct12;
    }
}

class InsIs extends Ins
{
    int rd;
    int rs1;
    int funct3;
    int funct7;     
    int imm;
        
    public InsIs(String name, int opcode, int rd, int rs1, int funct3, int funct7, int imm)
    {
        super(name, Ins.TYPE_Is, opcode);
    
        this.rd = rd;
        this.rs1 = rs1;
        this.funct3 = funct3;
        this.funct7 = funct7;
        this.imm = imm;
    }
}

class InsS extends Ins
{
    int rs1;
    int rs2;
    int funct3; 
    int imm;      

    public InsS(String name, int opcode, int rs1, int rs2, int funct3, int imm)
    {
        super(name, Ins.TYPE_S, opcode);
    
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.funct3 = funct3;
        this.imm = imm;
    }
}

class InsB extends Ins
{
    int rs1;
    int rs2;
    int funct3; 
    int imm;      

    public InsB(String name, int opcode, int rs1, int rs2, int funct3, int imm)
    {
        super(name, Ins.TYPE_B, opcode);
    
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.funct3 = funct3;
        this.imm = imm;
    }
}

class InsU extends Ins
{
    int rd;
    int imm;

    public InsU(String name, int opcode, int rd, int imm)
    {
        super(name, Ins.TYPE_U, opcode);
    
        this.rd = rd;
        this.imm = imm;
    } 
}

class InsJ extends Ins
{
    int rd;
    int imm;

    public InsJ(String name, int opcode, int rd, int imm)
    {
        super(name, Ins.TYPE_J, opcode);
    
        this.rd = rd;
        this.imm = imm;
    } 
}
