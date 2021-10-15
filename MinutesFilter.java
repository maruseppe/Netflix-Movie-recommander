
/**
 * Décrivez votre classe MinutesFilter ici.
 *
 * @author (votre nom)
 * @version (un numéro de version ou une date)
 */
public class MinutesFilter implements Filter{
    private int minutes;
    private int mxMinutes;
	
    public MinutesFilter(int mints,int maxts) {
    	minutes = mints;
    	mxMinutes= maxts;
    }
    
    @Override
    public boolean satisfies(String id) {
    	return MovieDatabase.getMinutes(id) >= minutes && MovieDatabase.getMinutes(id) <= mxMinutes;
    }
}
