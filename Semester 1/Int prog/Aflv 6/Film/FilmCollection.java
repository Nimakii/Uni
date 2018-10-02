import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class FilmCollection here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class FilmCollection
{
    private String owner;
    private List<Film> films;
    
    public FilmCollection(String owner){
        this.owner = owner;
        films = new ArrayList<Film>();
    }
    
    public void add(Film f) { films.add(f); }
    
    public Optional<Film> filmOfGenre ( String genre){
        return films.stream().filter(b -> b.getGenre().equals(genre))
                    .findFirst();
    }
    
    public Optional<Film> bestFilmOfGenre(String genre){
        return films.stream().filter(b -> b.getGenre().equals(genre))
                             .max(Comparator.comparing(b -> b.getScore()));
    }
}
