
/**
 * Décrivez votre classe GenreFilter ici.
 *
 * @author (votre nom)
 * @version (un numéro de version ou une date)
 */
import java.util.*;

public class GenreFilter implements Filter{
    
    private String genre;
    
    public GenreFilter(String gen) {
        genre = gen;
    }
    
    @Override
    public boolean satisfies(String id) {
        String [] genres = MovieDatabase.getGenres(id).split(",");
        for (int k=0; k<genres.length; k++) {
            //System.out.println(genres[k]);
            if (genres[k].trim().equals(genre))
                return true;
        }
        return false;
    }

}
