
/**
 * Write a description of class wasd here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Film
{
    private String title;
    private String genre;
    private int score;
    private int ageLimit;
    
    public Film(String title, String genre, int score, int ageLimit){
        this.title = title;
        this.genre = genre;
        this.score = score;
        this.ageLimit = ageLimit;
    }
    
    public String toString(){
        return (title + ", " + genre + ", Score: " + score + ", Age limit: "+ageLimit + " years");
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getGenre(){
        return genre;
    }
    
    public int getScore(){
        return score;
    }
    public int getAgeLimit(){ return ageLimit;}
}
