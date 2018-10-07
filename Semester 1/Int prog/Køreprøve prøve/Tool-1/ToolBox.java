import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class ToolBox here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ToolBox
{
    private String name;
    private List<Tool> tools;
    
    public ToolBox(String name){
        this.name = name;
        this.tools = new ArrayList<Tool>();
    }
    
    public void add(Tool t){
        tools.add(t);
    }
    
    public List<Tool> electricToolsF(){
        return tools.stream().filter(t -> t.getElectric()).collect(Collectors.toList());
    }
    public ArrayList<Tool> electricToolsI(){
        ArrayList<Tool> res = new ArrayList<Tool>();
        for (Tool t : tools){
            if(t.getElectric()){
                res.add(t);
            }
        }
        return res;
    }
    
    public int priceI(boolean electric){
        int res = 0;
        for (Tool t : tools){
            if ( t.getElectric() == electric && ( res == 0 || t.getPrice() < res)){
                res = t.getPrice();
            }
        }
        return res;
    }
    public int priceF(boolean electric){
        if(tools.stream().filter(t -> t.getElectric() == electric)
                             .mapToInt(t -> t.getPrice()).min().isPresent()){
            return (tools.stream().filter(t -> t.getElectric() == electric)
                             .mapToInt(t -> t.getPrice()).min().getAsInt());
        }
        else { return 0; }
    }
    
    public void printToolBoxI(){
        Collections.sort(tools);
        System.out.println(name);
        for (Tool t: tools){
            System.out.println(t);
        }
        Collections.shuffle(tools);
    }
    public void printToolBoxF(){
        Collections.sort(tools , Comparator.comparing((Tool t) -> t.getPrice())
                                           .thenComparing(t -> t.getName()));
        System.out.println(name);
        tools.forEach(t -> System.out.println(t));
        Collections.shuffle(tools);
    }
}
