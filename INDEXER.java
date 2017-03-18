import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.*;
import java.util.*;
public class INDEXER {

	public static void main(String[] args) throws Exception {
		////////////////////////////////////// parse html file /////////////////////////////////
		String url="http://www.quackit.com/html";
		Document d=Jsoup.connect(url).timeout(6000).get();
		String txt=d.text();
		System.out.println(txt);
		/////////////////////// split words and insert them in the database/////////////////////
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/indexer","root","");
	    String w,u,queryCheck;
	    PreparedStatement statement;
	    ResultSet resultSet;
		for (String word: txt.split("\\P{Alpha}+")) 
		{
	         w=word.toLowerCase();
	         u=word;
	         ///////////check if the word doesn't exist in the database -> insert it////////////
	         queryCheck = "SELECT * from `index` WHERE word = '"+w+"'";
	         statement = con.prepareStatement(queryCheck);
	         resultSet = statement.executeQuery();
	         if(!resultSet.next())
	         {
	        	 statement = con.prepareStatement("insert into `index` (word,doc) values('"+w+"','"+url+"')");
	        	 statement.execute();
	         }
	     }
		}
	}
