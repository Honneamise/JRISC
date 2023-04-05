import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Jrisc
{
    public static void usage()
    {
        System.out.println("USAGE : java Jrisc srcfile dstfile");
    }

    public static void main(String args[])
    {
        String source = null;
        File srcFile = null;
        Path srcPath = null;
        File dstFile = null;
        Path dstPath = null;

        //usage
        if(args.length!=2) { usage(); return; }

        //load src
        try
        {
            srcFile = new File(args[0]);

            srcPath = Path.of(srcFile.getAbsolutePath());
                
            source = Files.readString(srcPath);
        }
        catch (Exception ex) 
        { 
            System.out.println("Failed to load file : " + srcFile.getAbsolutePath()); 
            return; 
        }

        //assemble
        Map<String,Integer> labels = Assembler.Parse(source);

        if(labels==null) { System.out.println(Assembler.error); return; }

        byte[] output = Assembler.Build(source, labels);

        if(output==null)  { System.out.println(Assembler.error); return; }

        //save
        try
        {
            dstFile = new File(args[1]);
            dstPath = dstFile.toPath();

            Files.write(dstPath, output);
        }
        catch(Exception ex)
        { 
            System.out.println("Failed to save file : " + dstFile.getAbsolutePath()); 
            return; 
        }

        String res = srcFile.getAbsolutePath();
        res += " --> ";
        res += dstFile.getAbsolutePath();
        res += "\nSUCCESS !!!";

        System.out.println(res);
    }
}

