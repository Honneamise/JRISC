public class Register 
{
    

    public static int get(String name)
    {
        int index = 0;

        //X names
        if(name.length()>=2 && name.toUpperCase().charAt(0)=='X')
        {
            try{ index = Integer.parseInt(name.substring(1));}
            catch(Exception ex){ return -1;}

            return (index>=0 && index<32) ? index : -1; 
        }

        //A names A0-A7
        if(name.length()==2 && name.toUpperCase().charAt(0)=='A')
        {
            try{ index = Integer.parseInt(name.substring(1));}
            catch(Exception ex){ return -1;}

            return (index>=0 && index<8) ? index+10 : -1;
        }

        //S names S2-S11
        if(name.length()>=2 && name.toUpperCase().charAt(0)=='S')
        {
            try{ index = Integer.parseInt(name.substring(1));}
            catch(Exception ex){ return -1;}

            if (index>=2 && index<12) { return index+16; }
        }

        //T names T3-T6
        if(name.length()==2 && name.toUpperCase().charAt(0)=='T')
        {
             try{ index = Integer.parseInt(name.substring(1));}
            catch(Exception ex){ return -1;}
            
            if (index>=3 && index<7) { return index+25; }
        }

        //aliases
        switch(name.toUpperCase())
        {
            case "ZERO":
                return 0; 

            case "RA":
                return 1;

            case "SP":
                return 2;

            case "GP":
                return 3;
            
            case "TP":
                return 4;

            case "T0":
                return 5;

            case "T1":
                return 6;
                
            case "T2":
                return 7;

            case "S0":
            case "FP":
                return 8; 
 
            case "S1":
                return 9; 

            default:
                return -1;
        }
    }
}
