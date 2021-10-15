# Netflix-Movie-recommander

Ratings and recommendations could be based on what Netflix or Amazon do. 
Find viewers or buyers similar to you. In other words, you, the user, enter some seed ratings. 
These latter will be used to find existing raters in the database, viewers or buyers, with ratings similar to yours. 
Thus, the algorithm will provide with recommendations based on the top movies or purchases which have been recommended by these raters. 

The main algorithm within the file Recommandation Runner enabled a user to run my program interactively on the internet. 
The user was presented with a list of movies to rate, generated randomly from a database or alternatively filtered by genre, director, duration, etc.
The list is displayed in a html table styled using CSS. 
The user, then, will submit his ratings, and a list of recommended movies will be displayed. 
The recommendation algorithm is based on two main methods named getSimilarities and getSimilarRatings. 
getSimilarities is used to generate a HashMap of raters Id and the dot product between their ratings and the user ratings amongst the movies rated by the user.
On the other hand, getSimilarRatings generate an HashMap of Movies Id and their weighted average. 
For each movie which has been rated at least 10 times in the database, the method calculates a weighted average movie rating.
This average is limited to the top 5 similar raters with higher similarity ratings issued by the getSimilarities method above, 
and it is equal to the sum of the dot product between the similarity ratings and the ratings they actually gave to that movie. 
This will emphasize those raters who are closer to the user, since they have greater weights. 
A html table with the recommended movies, and their details such as title, genre, duration, etc. will be generated and display as an output to the user.
