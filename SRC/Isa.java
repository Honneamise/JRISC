import java.util.*;

public class Isa 
{
    public Map<String, int[]> set;

    Isa()
    {
        set = new HashMap<>();
    }
    
    public void addSet(Map<String, int[]> set)
    {
        this.set.putAll(set);
    }

    public int[] get(String name)
    {
        return set.get(name.toUpperCase());
    }

    public Set<String> keywords()
    {
        return set.keySet();
    }
}
