import java.util.*;

public class RV32I 
{
    public static final Map<String, int[]> set;

    static int[] add    = {Ins.TYPE_R, 0x33, 0x00, 0x00};
    static int[] sub    = {Ins.TYPE_R, 0x33, 0x00, 0x20};
    static int[] sll    = {Ins.TYPE_R, 0x33, 0x01, 0x00};
    static int[] slt    = {Ins.TYPE_R, 0x33, 0x02, 0x00};
    static int[] sltu   = {Ins.TYPE_R, 0x33, 0x03, 0x00};
    static int[] xor    = {Ins.TYPE_R, 0x33, 0x04, 0x00};
    static int[] srl    = {Ins.TYPE_R, 0x33, 0x05, 0x00};
    static int[] sra    = {Ins.TYPE_R, 0x33, 0x05, 0x20};
    static int[] or     = {Ins.TYPE_R, 0x33, 0x06, 0x00};
    static int[] and    = {Ins.TYPE_R, 0x33, 0x07, 0x00};

    static int[] jalr   = {Ins.TYPE_I, 0x67, 0x00}; 
    static int[] lb     = {Ins.TYPE_I, 0x03, 0x00};
    static int[] lh     = {Ins.TYPE_I, 0x03, 0x01};
    static int[] lw     = {Ins.TYPE_I, 0x03, 0x02};
    static int[] lbu    = {Ins.TYPE_I, 0x03, 0x04};
    static int[] lhu    = {Ins.TYPE_I, 0x03, 0x05};
    static int[] addi   = {Ins.TYPE_I, 0x13, 0x00};
    static int[] slti   = {Ins.TYPE_I, 0x13, 0x02};
    static int[] sltiu  = {Ins.TYPE_I, 0x13, 0x03};
    static int[] xori   = {Ins.TYPE_I, 0x13, 0x04};
    static int[] ori    = {Ins.TYPE_I, 0x13, 0x06};
    static int[] andi   = {Ins.TYPE_I, 0x13, 0x07};
    

    static int[] ecall  = {Ins.TYPE_Ie, 0x73, 0x00};
    static int[] ebreak = {Ins.TYPE_Ie, 0x73, 0x01};
    static int[] fence  = {Ins.TYPE_Ie, 0x0F, 0x00};

    static int[] slli   = {Ins.TYPE_Is, 0x13, 0x01, 0x00};
    static int[] srli   = {Ins.TYPE_Is, 0x13, 0x05, 0x00};
    static int[] srai   = {Ins.TYPE_Is, 0x13, 0x05, 0x20};

    static int[] sb     = {Ins.TYPE_S, 0x23, 0x00};
    static int[] sh     = {Ins.TYPE_S, 0x23, 0x01};
    static int[] sw     = {Ins.TYPE_S, 0x23, 0x02};

    static int[] beq    = {Ins.TYPE_B, 0x63, 0x00};
    static int[] bne    = {Ins.TYPE_B, 0x63, 0x01};
    static int[] blt    = {Ins.TYPE_B, 0x63, 0x04};
    static int[] bge    = {Ins.TYPE_B, 0x63, 0x05};
    static int[] bltu   = {Ins.TYPE_B, 0x63, 0x06};
    static int[] bgeu   = {Ins.TYPE_B, 0x63, 0x07};

    static int[] lui    = {Ins.TYPE_U, 0x37};
    static int[] auipc  = {Ins.TYPE_U, 0x17};

    static int[] jal    = {Ins.TYPE_J, 0x6f};

    static 
    {
        set = new HashMap<>();

        set.put("LUI",      lui);
        set.put("AUIPC",     auipc);
        set.put("JAL",      jal);
        set.put("JALR",     jalr);    
        set.put("BEQ",      beq);
        set.put("BNE",      bne);
        set.put("BLT",      blt);
        set.put("BGE",      bge);
        set.put("BLTU",     bltu);
        set.put("BGEU",     bgeu);
        set.put("LB",       lb);
        set.put("LH",       lh);
        set.put("LW",       lw);
        set.put("LBU",      lbu);
        set.put("LHU",      lhu);
        set.put("SB",       sb);
        set.put("SH",       sh);
        set.put("SW",       sw);
        set.put("ADDI",     addi);
        set.put("SLTI",     slti);
        set.put("SLTIU",    sltiu);
        set.put("XORI",     xori);
        set.put("ORI",      ori);
        set.put("ANDI",     andi);
        set.put("SLLI",     slli);
        set.put("SRLI",     srli);
        set.put("SRAI",     srai);
        set.put("ADD",      add);
        set.put("SUB",      sub);
        set.put("SLL",      sll);
        set.put("SLT",      slt);
        set.put("SLTU",     sltu);
        set.put("XOR",      xor);
        set.put("SRL",      srl);
        set.put("SRA",      sra);
        set.put("OR",       or);
        set.put("AND",      and);
        set.put("FENCE",    fence);
        set.put("ECALL",    ecall);
        set.put("EBREAK",   ebreak);
             
    }
}
