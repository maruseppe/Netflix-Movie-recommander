
/**
 * Décrivez votre classe RecommandationRunner ici.
 *
 * @author (votre nom)
 * @version (un numéro de version ou une date)
 */


import java.util.*;
public class RecommendationRunner implements Recommender{
    
    public ArrayList <String> getItemsToRate() {
        
        //initialize the moviedatabase
        MovieDatabase mvDB = new MovieDatabase();
        mvDB.initialize("ratedmoviesfull.csv");
        //System.out.println("the number of movies is " + mvDB.size());
        
        // create a database of all movies ids 
        ArrayList<String> idMovies = MovieDatabase.filterBy(new TrueFilter());
	
        // create an array list of 15 idMovies randomly chosen from the database 
        ArrayList <String> idRanMovies = new ArrayList <String>();
        
        Random myRandom = new Random();
        for (int k=0; k < 15; k++){
            int index = myRandom.nextInt(idMovies.size());
    	    idRanMovies.add(idMovies.get(index));
        }
	return idRanMovies;
    }
    
    public void printRecommendationsFor(String webRaterID) {
        
        FourthRating fr = new FourthRating ();
        //FourthRatingsTest fr = new FourthRatingsTest ();
        
        RaterDatabase rtDB = new RaterDatabase ();
        rtDB.initialize("ratings.csv");
        //System.out.println("the number of raters is " + rtDB.size());
        
        MovieDatabase mvDB = new MovieDatabase();
        mvDB.initialize("ratedmoviesfull.csv");
        //System.out.println("the number of movies is " + mvDB.size());
        
        // create an ArrayList of similar Movies based ont the top 10 similar raters and with minimal rating equal to 3
        ArrayList <Rating> simMoviesTot = fr.getSimilarRatings(webRaterID,10,3);
        
        // check if the the returned array List is longer than  15 elements
        ArrayList<Rating> simMovies = new ArrayList<Rating> ();
        if (simMoviesTot.size() == 0 ) {
        System.out.println("List of recommended films NOT FOUND");
        }
        
        else {
            
            for (int i = 0; i < simMoviesTot.size(); i++) {
               if (i < 15 ) {
                   simMovies.add(new Rating(simMoviesTot.get(i).getItem(), (simMoviesTot.get(i).getValue())));
               }
            }
            
            
            // set the html table style
            String style = ("<style>");
            style += ("table, th, td {");
            style += ("border: 1px solid black;");
            style += ("border-collapse: collapse;");
            style += ("width:100%;}");
            style += ("th, td {");
            style += ("padding: 10px;}");
            style += ("table#alter tr:nth-child(even) { ");
            style += ("background-color: #eee; } ");
            style += ("table#alter tr:nth-child(odd) { ");
            style += ("background-color: #fff; } ");
            style += ("table#alter th { ");
            style += ("color: white; ");
            style += ("background-color: gray;} ");
            style += ("</style>  ");
          
            System.out.println(style);
           
            
            // display the returned top similar movies in a html table 
            String html = ("<table> <tr> <th>Title</th> <th>Rating Value</th> </tr>");
            for (Rating rat : simMovies){
                html += ("<tr> <td>" + mvDB.getTitle(rat.getItem()) + "</td> <td>" +rat.getValue() +"</td> </tr>");
            }
            html += "</table>";
            System.out.println(html);
            }
            
        }
}