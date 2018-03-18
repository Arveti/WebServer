Web Server implementation which server static files from the given folder

Usage : java -jar WebServer.jar port rootFolderPath

If Opened in Eclipse - Add log4j and junit as library

Instead of thread pool implemented independent threading via ClientHandler class.
Main file - MultiThreadedWebServer.java

Example : From the project folder - java -jar WebServer.jar 8000 root
          From browser - http://localhost:8000/index.html