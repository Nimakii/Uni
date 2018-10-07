import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class wasd here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ToolBox
{
    private String owner;
    private List<Tool> tools;
    
    public ToolBox(String owner){
        this.owner = owner;
        this.tools = new ArrayList<Tool>();
    }
    
    public void add(Tool t){tools.add(t);}
    
    public ArrayList<Tool> heavyToolsI(int weight){
        ArrayList<Tool> res = new ArrayList<Tool>();
        for (Tool t: tools){
            if(t.getWeight()>=weight){
                res.add(t);
            }
        }
        return res;
    }
    
    public List<Tool> heavyToolsF(int weight){
        return tools.stream().filter(t -> t.getWeight() >= weight)
                             .collect(Collectors.toList());
    }
    
    public int lightToolF(){
        Optional<Tool> res = tools.stream().min(Comparator.comparing(t->t.getWeight()));
        if(res.isPresent()){
            return res.get().getPrice();
        }
        return 0;
    }
    
    public int lightToolI(){
        Tool res = null;
        for (Tool t : tools){
            if ( res == null || t.getWeight()< res.getWeight()){
                res = t;
            }
        }
        if (res == null){
            return 0;
        }
        return res.getPrice();
    }
    
    public void printToolBoxI(){
        System.out.println(owner);
        Collections.sort(tools);
        for(Tool t : tools){
            System.out.println(t);
        }
        Collections.shuffle(tools);
    }
    
    public void printToolBoxF(){
        System.out.println(owner);
        Collections.sort(tools , Comparator.comparing((Tool t) -> t.getWeight())
                                           .thenComparing(t -> t.getPrice()));
        tools.forEach(t->System.out.println(t));
        Collections.shuffle(tools);
    }
}
