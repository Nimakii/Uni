
/**
 * Write a description of class Musician here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Musician implements Comparable<Musician>
{
    private String name;
    private String instrument;
    private int skillLevel;
    
    public Musician(String name, String instrument, int skillLevel){
        this.name = name;
        this.instrument = instrument;
        this.skillLevel = skillLevel;
    }
    
    public String toString(){
        return(name + ", " + instrument + " (Skill level: " + skillLevel + ")");
    }
    
    public int getSkill(){
        return skillLevel;
    }
    
    public String getName(){
        return name;
    }
    
    public String getInstrument(){
        return instrument;
    }
    
    public int compareTo(Musician m){
        if ( this.instrument.compareTo(m.instrument) == 0){
            return this.skillLevel - m.skillLevel;
        }
        return this.instrument.compareTo(m.instrument);
    }
}
