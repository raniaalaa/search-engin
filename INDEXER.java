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
public static Boolean CheckExistance(Connection con,String Word) throws Exception
{
	String queryCheck = "SELECT * from `Word` WHERE word = '"+Word+"'";
	PreparedStatement statement = con.prepareStatement(queryCheck);
    ResultSet resultSet = statement.executeQuery();
    return (resultSet.next());
    	
}
public static long GetID(Connection con,String Word) throws Exception
{
	String queryCheck = "SELECT Wid from `Word` WHERE word = '"+Word+"'";
	PreparedStatement statement = con.prepareStatement(queryCheck);
    ResultSet result = statement.executeQuery();
  
    if(result.next())
    	return (result.getLong("Wid"));
    else
    	return -1;
}
public static String Stemmer(String word)
{
	 porterStemmer ps=new porterStemmer();
		return ps.stemTerm(word); 
}
 public static String Handling_Stop_Words(Connection con,String txt) throws Exception
 {   int pos=0,count=0;
	 String Expression = "";
	 String[] parts =txt.split("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})");
		while(pos<parts.length)
			{
			
			//System.out.println(parts[pos]);
			//Boolean 
			String Phrase="";
			Expression = "";
			String First_Stop_Word="";
			if (pos<parts.length&&stopwords.contains(parts[pos]))
					{

				First_Stop_Word=parts[pos];	
				pos++;
					}
		while (pos<parts.length&&stopwords.contains(parts[pos]))
			{
			Phrase=Phrase+" "+parts[pos];
			pos++;
			}
		if(Phrase.length()>1)
		{   count++;
			Expression=First_Stop_Word+Phrase;
			if(!CheckExistance(con,Expression))
			{
					InsertInIndexer(con,Expression,-1);
			}
		}
		else 
			Phrase="";
		pos++;
		}
		//System.out.println("countttttt"+count);
	 return txt;
 }
 //function to check importance

 public static void Run(int doc_id) throws Exception
 {
	 int pos=0;
//////////////////////////////////////parse html file /////////////////////////////////
	 
		 //http://www.quackit.com/html
	   String url="http://stackoverflow.com/";
		Document doc=Jsoup.connect(url).timeout(6000).get();
		String txt=doc.text();
		String Title = doc.title();
		
	   String tag="h";
	   String All_Headers="";
	   Elements Header;
	   for(int i=1;i<20;i++)
		{  
		   tag="h"+String.valueOf(i);
		   Header = doc.select(tag);
		   System.out.println("size  "+Header.size());
		if(Header.size()>0)
			{
		Header= doc.getElementsByTag(tag);
		String pConcatenated="";
		        for (Element x: Header) {
		            pConcatenated+= x.text()+" ";
		            System.out.println("hedeeeeeeeeeeeeeer----------->"+x.text()+" ");
		        }
		        All_Headers=All_Headers+pConcatenated;
			}
		else
			break;
		
		}
	 //String txt="computing Computer send  Java toString() Method Jobs  SENDFiles  Whiteboard  Net Meeting Tools  Articles Facebook Google+ Twitter Linkedin YouTube Home Tutorials Library Coding Ground Tutor Connect Videos Search Java Tutorial Java - Home Java - Overview Java - Environment Setup Java - Basic Syntax Java - Object & Classes Java - Basic Datatypes Java - Variable Types Java - Modifier Types Java - Basic Operators Java - Loop Control Java - Decision Making Java - Numbers Java - Characters Java - Strings Java - Arrays Java - Date & Time Java - Regular Expressions Java - Methods Java - Files and I/O Java - Exceptions Java - Inner classes Java Object Oriented Java - Inheritance Java - Overriding Java - Polymorphism Java - Abstraction Java - Encapsulation Java - Interfaces Java - Packages Java Advanced Java - Data Structures Java - Collections Java - Generics Java - Serialization Java - Networking Java - Sending Email Java - Multithreading Java - Applet Basics Java - Documentation Java Useful Resources Java - Questions and Answers Java - Quick Guide Java - Useful Resources Java - Discussion Java - Examples Selected Reading Developer's Best Practices Questions and Answers Effective Resume Writing HR Interview Questions Computer Glossary Who is Who Java - toString() Method Advertisements Previous Page Next Page   Description The method is used to get a String object representing the value of the Number Object. If the method takes a primitive data type as an argument, then the String object representing the primitive data type value is returned. If the method takes two arguments, then a String representation of the first argument in the radix specified by the second argument will be returned. Syntax Following are all the variants of this method ? String toString()";
	
		///////////////////////////////////////////////////////////////////////////////////////
		Connection con=Connect();
		txt=Handling_Stop_Words(con,txt);
		String w,sw,w1;
		long WID;
		for (String word: txt.split("\\P{Alpha}+")) 
		{
			w=word.toLowerCase();
			if(stopwords.contains(w))
				continue;
			else 
				pos++;
			//stemming
			w1=w;
			sw=Stemmer(w1);
			if(CheckExistance(con,w))
			{
			WID=GetID(con,w);
			}
			int important;
			if(!w.equals(sw)&&!CheckExistance(con,w)&&!CheckExistance(con,sw))
			{
				if(!sw.equals(""))
				{
					//count++;
					InsertInIndexer(con,sw,-1);
					long ID=GetID(con,sw);
					//get doc_id hagibha mn l main
					important=Importance(Title,sw,All_Headers);
					InsertInWordPositions(con,ID,doc_id,pos,important);
					
					InsertInIndexer(con,w,ID);
					WID=GetID(con,w);
					important=Importance(Title,w,All_Headers);
					InsertInWordPositions(con,WID,doc_id,pos,important);	
				}
			}
			else if(!w.equals(sw)&&CheckExistance(con,sw)&&!CheckExistance(con,w))
			{
				long ID=GetID(con,sw);	
				InsertInIndexer(con,w,ID);
				WID=GetID(con,w);
				important=Importance(Title,w,All_Headers);
				InsertInWordPositions(con,WID,doc_id,pos,important);
			
			}
			else if(w.equals(sw)&&!CheckExistance(con,sw)&&!CheckExistance(con,w))
			{
				if(!sw.equals(""))
					{InsertInIndexer(con,sw,-1);
					important=Importance(Title,w,All_Headers);
					WID=GetID(con,sw);
					InsertInWordPositions(con,WID,doc_id,pos,important);
					}
				
				
			}
		
		}
 }
 
	}


