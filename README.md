# JavaChess client
Frontend implementation for JavaChess

To communicate with heroku server (in production), run
`java -jar -Dproduction=true target/javachessclient-0.1.0-jar-with-dependencies.jar`
The recommended java version is 14.0.2

To run on localhost (which would be faster), start the server in development mode
and run the above .jar file with -Dproduction=false.
