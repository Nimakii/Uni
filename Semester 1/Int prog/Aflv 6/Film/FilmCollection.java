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
        return films.stream().filter(f -> f.getGenre().equals(genre))
                    .findFirst();
    }
    
    public Optional<Film> bestFilmOfGenre(String genre){
        return films.stream().filter(f -> f.getGenre().equals(genre))
                             .max(Comparator.comparing(f -> f.getScore()));
    }
    
    public void printFilmCollection(int age){
        System.out.println("In the film collection owned by "+ owner + " resides the following films which are legal to watch at the age of "+ age);
        Collections.sort(films, Comparator.comparing( (Film f) -> f.getGenre())
                                          .thenComparing( (Film f) -> f.getScore()));
        films.stream().filter(f -> f.getAgeLimit()> age).collect(Collectors.toList()).forEach(f -> System.out.println(f));
    }
}
