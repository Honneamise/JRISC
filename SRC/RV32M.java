import java.util.*;

public class RV32M
{
    public static final Map<String, int[]> set;

    static int[] mul    = {Ins.TYPE_R, 0x33, 0x00, 0x01};
    static int[] mulh   = {Ins.TYPE_R, 0x33, 0x01, 0x01};
    static int[] mulhsu = {Ins.TYPE_R, 0x33, 0x02, 0x01};
    static int[] mulhu  = {Ins.TYPE_R, 0x33, 0x03, 0x01};    
    static int[] div    = {Ins.TYPE_R, 0x33, 0x04, 0x01};
    static int[] divu   = {Ins.TYPE_R, 0x33, 0x05, 0x01};
    static int[] rem    = {Ins.TYPE_R, 0x33, 0x06, 0x01};
    static int[] remu   = {Ins.TYPE_R, 0x33, 0x07, 0x01};

    static 
    {
        set = new HashMap<>();

        set.put("MUL",      mul);
        set.put("MULH",     mulh);
        set.put("MULHSU",   mulhsu);
        set.put("MULHU",    mulhu);    
        set.put("DIV",      div);
        set.put("DIVU",     divu);
        set.put("REM",      rem);
        set.put("REMU",     remu);
        
    }
}
