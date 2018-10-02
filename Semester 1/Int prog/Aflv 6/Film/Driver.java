
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Film f1 = new Film("The Room", "drama" , 1 , 14);
        Film f2 = new Film("Star wars 1", "space opera" , 10 , 14);
        Film f3 = new Film("Star wars 2", "space opera" , 9 , 14);
        Film f4 = new Film("Star wars 3", "space opera" , 12 , 14);
        Film f5 = new Film("Star wars 4", "space opera" , 4 , 35);
        
        System.out.println(f1.toString());
        System.out.println(f2.toString());
        System.out.println(f3.toString());
        System.out.println(f4.toString());
        System.out.println(f5.toString());
        
        FilmCollection mine = new FilmCollection("Jens Kristian");
        mine.add(f1);
        mine.add(f2);
        mine.add(f3);
        mine.add(f4);
        mine.add(f5);
        System.out.println();
        System.out.println(mine.filmOfGenre("space opera"));
        System.out.println();
        System.out.println(mine.bestFilmOfGenre("space opera"));
    }
}
