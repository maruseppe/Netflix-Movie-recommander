
/**
 * Décrivez votre classe FourthRating ici.
 *
 * @author (votre nom)
 * @version (un numéro de version ou une date)
 */
import java.util.*;
public class FourthRating {
    
     public double getAverageByID (String idMovie, int minimalRaters) {
        ArrayList <Rater> myRaters= RaterDatabase.getRaters();  
        int count=0;
        double sum=0;
        for (Rater rat : myRaters) {
            if (rat.hasRating(idMovie)) {
                sum +=rat.getRating(idMovie);
                count++;
            }     
        }
        if (count>=minimalRaters)
            return sum/count;
        
        return 0.0;
    }
    
     public ArrayList<Rating> getAverageRatings (int minimalRaters) {
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        ArrayList <Rating> rtgAvg = new ArrayList <Rating>();
        
        for (String idMovie : movies) {
            if (getAverageByID(idMovie, minimalRaters) > 0.0)
                rtgAvg.add( new Rating( idMovie,getAverageByID(idMovie, minimalRaters) ) );
        }
        return rtgAvg;
    }
    
    public ArrayList<Rating> getAverageRatingsByFilter (int minimalRaters, Filter filterCriteria ) {
    
        ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);
        ArrayList <Rating> rtgAvg = new ArrayList <Rating>();
        
        for (String idMovie : movies) {
            if (getAverageByID(idMovie, minimalRaters) > 0.0)
                rtgAvg.add( new Rating( idMovie,getAverageByID(idMovie, minimalRaters) ) );
        }
        return rtgAvg;
    }
  
    private double dotProduct (Rater me, Rater r ){
        
         double sum=0.0;
         for (String idMovie : me.getItemsRated()) {
             if (r.hasRating (idMovie))
                sum +=(me.getRating(idMovie)-5)*(r.getRating(idMovie)-5); 
         }    
        return sum;
    }
    
    private ArrayList <Rating> getSimilarities (String idRater) {
        
        ArrayList <Rater> myRaters= RaterDatabase.getRaters();
        //System.out.println (myRaters.subList(0,5));
        myRaters.remove(RaterDatabase.getRater(idRater));
        //System.out.println (myRaters.subList(0,4));
        
        ArrayList <Rating> simRaters = new ArrayList <Rating>();
        
        for (Rater rat : myRaters) {
            double dp=dotProduct(rat,RaterDatabase.getRater(idRater));
            if (dp>=0)
                simRaters.add(new Rating (rat.getID(),dp));
        }
        Collections.sort(simRaters, Collections.reverseOrder());
        return simRaters;
    }
    
    //create the array list of top similar raters
    private ArrayList<Rating> createSimRaters(String idRater, int numSimilarRaters) {
	ArrayList<Rating> simRaters = new ArrayList<>();

	//get the similar rater list 
	ArrayList<Rating> simList = getSimilarities(idRater);

	for(int i = 0; i < numSimilarRaters; i++) 
		simRaters.add(new Rating(simList.get(i).getItem(), (simList.get(i).getValue())));
	
	return simRaters;  
    }
    
    
    //check if a movie rated by the top similar raters has a minimal number of sim ratings 
    private boolean movieHasMinimalSimRaters(String idMovie, ArrayList<Rating> simRaters, int minimalRatings) {
  
        int count = 0;
        for(Rating rat : simRaters){
            //check if the rater has a positive rating for the given movie
            double value = RaterDatabase.getRater(rat.getItem()).getRating(idMovie);  //rater.getRating(idMovie)
            if(value > 0.0)
                count++;
        }
        //check if the movie has at least minimal rating
        if(count >= minimalRatings) 
            return true;
        
        return false; 
    }

    //calculate a movie similar average by weighting over the top similar raters
    private double calculateSimMovies(String idMovie, ArrayList<Rating> simRaters){
	
        int count = 0;
	double sumRating = 0.0;
	for (Rating simRater : simRaters){
            //acess all movies rated by simRater : rater.getItemsRated(), and store them in a temporary array List
            ArrayList<String> moviesSimRated = RaterDatabase.getRater(simRater.getItem()).getItemsRated(); 
        	
            if(moviesSimRated.contains(idMovie)){
    		//get rater similar rating value
    		double similRating = simRater.getValue();
    		//get rater original rating for the particular movie 
    		double originalRating = RaterDatabase.getRater(simRater.getItem()).getRating(idMovie);
                            //calculate the movie'similar average
    		sumRating += similRating * originalRating;
    		count++;
    	    } 
	} 
	return sumRating / count;
	}
	
    public ArrayList <Rating> getSimilarRatings (String idRater, int numSimilarRaters, int minimalRaters) {
    
        ArrayList<Rating> simRaters = createSimRaters(idRater, numSimilarRaters);
        //System.out.println (simRaters.subList(0,numSimilarRaters));
       
        // create an array list of idMovies rated by the top similar raters and which have minimal ratings
        ArrayList <String> idSimMovies = new ArrayList <String>();
        int count=0;
        for (Rating simRater : simRaters) {
            // create an array list of all id movies rated by simRater : rater.getItemsRated()
            ArrayList <String> idSimRaterMovies = RaterDatabase.getRater(simRater.getItem()).getItemsRated();
            for (String idMovie : idSimRaterMovies) {
                                                    //check if movie has minimal ratings given by the top similar raters 
                if (!idSimMovies.contains(idMovie) && movieHasMinimalSimRaters(idMovie, simRaters,  minimalRaters))
                    idSimMovies.add(idMovie);
            }
        }
        Collections.sort(idSimMovies);
        //System.out.println (idSimMovies.subList(0,numSimilarRaters));
      
        // create an array list of ratings of the top similar movies where original ratings are weighted by simratings 
        ArrayList <Rating> simMovies = new ArrayList <Rating>();
        
        for (String idMovie : idSimMovies){	
            double avgRating = calculateSimMovies(idMovie, simRaters);
            //add the movieID and it's average rating to the list as a Rating object (instance)
            simMovies.add(new Rating(idMovie, avgRating));
    	}
        
        Collections.sort(simMovies, Collections.reverseOrder());
        return simMovies;
        
    }
    
    public ArrayList <Rating> getSimilarRatingsByFilter (String idRater, int numSimilarRaters, int minimalRaters, Filter filterCriteria) {
    
        ArrayList<Rating> simRaters = createSimRaters(idRater, numSimilarRaters);
        //System.out.println (simRaters.subList(0,numSimilarRaters));
       
        // create an array list of idMovies rated by the top similar raters and which have minimal ratings
        ArrayList <String> idSimMovies = new ArrayList <String>();
        int count=0;
        for (Rating simRater : simRaters) {
            // create an array list of all id movies rated by simRater : rater.getItemsRated()
            ArrayList <String> idSimRaterMovies = RaterDatabase.getRater(simRater.getItem()).getItemsRated();
            for (String idMovie : idSimRaterMovies) {
                                                    //check if movie has minimal ratings given by the top similar raters 
                if (!idSimMovies.contains(idMovie) && movieHasMinimalSimRaters(idMovie, simRaters,  minimalRaters))
                    idSimMovies.add(idMovie);
            }
        }
        Collections.sort(idSimMovies);
        //System.out.println (idSimMovies.subList(0,numSimilarRaters));
      
        // create an array list of ratings of the top similar movies where original ratings are weighted by simratings 
        ArrayList <Rating> simMovies = new ArrayList <Rating>();
        
        // create a database of movies verifying the filter Criteria
        ArrayList<String> filteredMovies = MovieDatabase.filterBy(filterCriteria);
	
        for (String idMovie : idSimMovies){
            // check if idMovie passes the filtered criteria, i.e. it is contained in the filterd movie database
            if(filteredMovies.contains(idMovie)) {
                double avgRating = calculateSimMovies(idMovie, simRaters);
                //add the movieID and it's average rating to the list as a Rating object (instance)
                simMovies.add(new Rating(idMovie, avgRating));
            }
    	}
        Collections.sort(simMovies, Collections.reverseOrder());
        return simMovies;
    }
}
