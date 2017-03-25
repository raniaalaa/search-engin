The Crawler :
Divides its work between number of threads which specified by the user.
to run it, enter the number of threads you want from 1 to 20.
Each of them crawls number of web pages , downloads them in folder named (html),inserts their links into database ,goes to another web page and so on.
The Indexer :
Read the documents from (html) folder ,stores the words with their positions and importances into database.