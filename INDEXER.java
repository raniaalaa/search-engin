import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.*;
public class INDEXER {
//////////////////////////////////////Stopping Words List///////////////////////////////
static List<String> stopwords = Arrays.asList("a", "able", "about",
"across", "after", "all", "almost", "also", "am", "among", "an",
"and", "any", "are", "as","not", "at", "be", "because", "been", "but",
"by", "can", "cannot", "could", "dear", "did", "do", "does",
"either", "else", "ever", "every", "for", "from", "get", "got",
"had", "has", "have", "he", "her", "hers", "him", "his", "how",
"however", "i", "if", "in", "into", "is", "it", "its", "just",
"least", "let", "like", "likely", "may", "me", "might", "most",
"must", "my", "neither", "no", "nor", "of", "off", "often",
"on", "only", "or", "other", "our", "own", "rather", "said", "say",
"says", "she", "should", "since", "so", "some", "than", "that",
"the", "their", "them", "then", "there", "these", "they", "this",
"tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
"what", "when", "where", "which", "while", "who", "whom", "why",
"will", "with", "would", "yet", "you", "your");
INDEXER()
{}
public static Connection Connect() throws ClassNotFoundException, SQLException
{
	Class.forName("com.mysql.jdbc.Driver");
		//jdbc:mysql://localhost:3306/SearchEngine
	Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/SearchEngine?autoReconnect=true&useSSL=false","root","");
	return con; 
}
public static void InsertInExpressions(Connection con,String Expression) throws ClassNotFoundException, SQLException
{
	PreparedStatement  statement = con.prepareStatement("insert into `Expressions` (`Expression`,`E_ID`) values('"+Expression+"',NULL)");
	statement.execute();
}
public static void InsertInIndexer(Connection con,String Word,long Sword) throws ClassNotFoundException, SQLException
{
	PreparedStatement statement;
	if(Sword==-1)
		statement = con.prepareStatement("insert into `Word` (`word`,`Wid`,`Stemming`) values('"+Word+"',NULL,NULL)");
	else
		statement = con.prepareStatement("insert into `Word` (`word`,`Wid`,`Stemming`) values('"+Word+"',NULL,"+Sword+")");
	statement.execute();
}
public static int Importance(String title,String word,String header) 
{
	//azwed 3liha bta3t l img
	 if ( title.toLowerCase().indexOf(word.toLowerCase()) != -1 )
	   return 3;
	 
	 else if(header.toLowerCase().indexOf(word.toLowerCase()) != -1)
		 return 2;
	 
	 else return 1;
	 
}
public static void InsertInWordPositions(Connection con,long w_id,int doc_id,int position,int importance) throws SQLException 
{
	//importance=3 if title,=2 if header,=1 else
	PreparedStatement statement;
	statement = con.prepareStatement("insert into `wordpositions` (`w_id`,`doc_id`,`position`,`importance`) values("+w_id+","+doc_id+","+position+","+importance+")");
	statement.execute();
}
public static void InsertInExpressionsPositions(Connection con,long expression_id,int doc_id,int start_position,int end_position) throws SQLException 
{
	
	PreparedStatement statement;
	statement = con.prepareStatement("insert into `expressionspositions` (`Expression_id`,`Doc_id`,`Start_pos`,`End_pos`) values("+expression_id+","+doc_id+","+start_position+","+end_position+")");
	statement.execute();
}
//Check exitance in word table
public static Boolean CheckWordExistance(Connection con,String Word) throws Exception
{
	String queryCheck = "SELECT * from `Word` WHERE word = '"+Word+"'";
	PreparedStatement statement = con.prepareStatement(queryCheck);
    ResultSet resultSet = statement.executeQuery();
    return (resultSet.next());
    	
}
//Check exitance in expression table
public static Boolean CheckExpressionExistance(Connection con,String Expression) throws Exception
{
	String queryCheck = "SELECT * from `Expressions` WHERE word = '"+Expression+"'";
	PreparedStatement statement = con.prepareStatement(queryCheck);
    ResultSet resultSet = statement.executeQuery();
    return (resultSet.next());
    	
}
public static long GetWordID(Connection con,String Word) throws Exception
{
	String queryCheck = "SELECT Wid from `Word` WHERE word = '"+Word+"'";
	PreparedStatement statement = con.prepareStatement(queryCheck);
    ResultSet result = statement.executeQuery();
  
    if(result.next())
    	return (result.getLong("Wid"));
    else
    	return -1;
}
public static String GetText(Connection con,int doc_id) throws Exception
{
	String queryCheck = "SELECT document from `record` WHERE ID = '"+doc_id+"'";
	PreparedStatement statement = con.prepareStatement(queryCheck);
    ResultSet result = statement.executeQuery();
  
    if(result.next())
    	return (result.getString("document"));
    else
    	return "";
}
public static long GetExpressionID(Connection con,String Expression) throws Exception
{
	String queryCheck = "SELECT Expression_id from `Expressions` WHERE Expression = '"+Expression+"'";
	PreparedStatement statement = con.prepareStatement(queryCheck);
    ResultSet result = statement.executeQuery();
  
    if(result.next())
    	return (result.getLong("Expression_id"));
    else
    	return -1;
}
public static String Stemmer(String word)
{
	 porterStemmer ps=new porterStemmer();
		return ps.stemTerm(word); 
}
 public static void Handling_Stop_Words(Connection con,String txt,int doc_id) throws Exception
 {   int pos=0;
     int start_pos=0;
     int end_pos=0;
	 String Expression = "";
	 String[] parts =txt.split("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})");
		while(pos<parts.length)
			{
			String Phrase="";
			Expression = "";
			String First_Stop_Word="";
			if (pos<parts.length&&stopwords.contains(parts[pos]))
					{
				First_Stop_Word=parts[pos];	
				start_pos=pos;
				pos++;
					}
		while (pos<parts.length&&stopwords.contains(parts[pos]))
			{
			Phrase=Phrase+" "+parts[pos];
			pos++;
			}
		if(Phrase.length()>1)
		{  
			Expression=First_Stop_Word+Phrase;
			end_pos=start_pos+Expression.length();
			if(!CheckExpressionExistance(con,Expression))
			{
					InsertInExpressions(con,Expression);
					long EID=GetExpressionID(con,Expression);
					InsertInExpressionsPositions(con,EID,doc_id,start_pos,end_pos);
			}
		}
		else 
			Phrase="";
		pos++;
		}
 }


 public static void Run(String txt,int doc_id,String Title,String All_Headers) throws Exception
 {
	 int pos=0;
//////////////////////////////////////parse html file /////////////////////////////////
	    Connection con=Connect();
		 
		Handling_Stop_Words(con,txt,doc_id);
		String w,sw,w1;
		long WID;
		for (String word: txt.split("\\P{Alpha}+")) 
		{
			w=word.toLowerCase();
			pos++;
			if(stopwords.contains(w))
				continue;
			
			//stemming
			w1=w;
			sw=Stemmer(w1);
			if(CheckWordExistance(con,w))
			{
			WID=GetWordID(con,w);
			}
			int important;
			if(!w.equals(sw)&&!CheckWordExistance(con,w)&&!CheckWordExistance(con,sw))
			{
				if(!sw.equals(""))
				{
					//count++;
					InsertInIndexer(con,sw,-1);
					long ID=GetWordID(con,sw);
					//get doc_id hagibha mn l main
					important=Importance(Title,sw,All_Headers);
					InsertInWordPositions(con,ID,doc_id,pos,important);
					
					InsertInIndexer(con,w,ID);
					WID=GetWordID(con,w);
					important=Importance(Title,w,All_Headers);
					InsertInWordPositions(con,WID,doc_id,pos,important);	
				}
			}
			else if(!w.equals(sw)&&CheckWordExistance(con,sw)&&!CheckWordExistance(con,w))
			{
				long ID=GetWordID(con,sw);	
				InsertInIndexer(con,w,ID);
				WID=GetWordID(con,w);
				important=Importance(Title,w,All_Headers);
				InsertInWordPositions(con,WID,doc_id,pos,important);
			
			}
			else if(w.equals(sw)&&!CheckWordExistance(con,sw)&&!CheckWordExistance(con,w))
			{
				if(!sw.equals(""))
					{InsertInIndexer(con,sw,-1);
					important=Importance(Title,w,All_Headers);
					WID=GetWordID(con,sw);
					InsertInWordPositions(con,WID,doc_id,pos,important);
					}
				
				
			}
		
		}
 }
 
	}


