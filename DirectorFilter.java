
/**
 * Décrivez votre classe DirectorFilter ici.
 *
 * @author (votre nom)
 * @version (un numéro de version ou une date)
 */
import java.util.*;
public class DirectorFilter implements Filter{
    private String [] directors;
    
    public DirectorFilter(String [] dirs) {
        directors = dirs;
    }
    
    @Override
    public boolean satisfies(String id) {
        String []  dirs = MovieDatabase.getDirector(id).split(",");
        for (String dirX : dirs) {
            for (String dirY : directors) {
                //System.out.println(genres[k]);
                if (dirX.trim().equals(dirY.trim()))
                    return true;
            }
        }
        return false;
    }
}
