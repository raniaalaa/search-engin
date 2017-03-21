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
static DATABASE db=new DATABASE();
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
public static int Importance(String title,String word,String header,String img) 
{
	for (String title_element: title.split("\\P{Alpha}+")) 
	{
		if(word.equals(title_element.toLowerCase()))
		{
			return 4;
		}
	}
	for (String img_element: img.split("\\P{Alpha}+")) 
	{
		if(word.equals(img_element.toLowerCase()))
		{
			return 3;
		}
	}
	for (String header_element: header.split("\\P{Alpha}+")) 
	{
		if(word.equals(header_element.toLowerCase()))
		{
			return 2;
		}
	}
	return 1;
	 
}
public static String Stemmer(String word)
{
	 porterStemmer ps=new porterStemmer();
		return ps.stemTerm(word); 
}
 public static void Handling_Stop_Words(String txt,int doc_id) throws Exception
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
			end_pos=pos;
			//end_pos=start_pos+Expression.length(); /////////////////8alt
			if(!db.CheckExpressionExistance(Expression))
			{
					db.InsertInExpressions(Expression);
					long EID=db.GetExpressionID(Expression);
					db.InsertInExpressionsPositions(EID,doc_id,start_pos,end_pos);
			}
		}
		else 
			Phrase="";
		pos++;
		}
 }


 public static void Run(String txt,int doc_id,String [] Importants) throws Exception
 {
	 int pos=0;
//////////////////////////////////////parse html file /////////////////////////////////
		Handling_Stop_Words(txt,doc_id);
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
			if(db.CheckWordExistance(w))
			{
			WID=db.GetWordID(w);
			}
			int important;
			if(!w.equals(sw)&&!db.CheckWordExistance(w)&&!db.CheckWordExistance(sw))
			{
				if(!sw.equals(""))
				{
					//count++;
					db.InsertInIndexer(sw,-1);
					long ID=db.GetWordID(sw);
					//get doc_id hagibha mn l main
					important=Importance(Importants[0],sw,Importants[1],Importants[2]);
					db.InsertInWordPositions(ID,doc_id,pos,important);
					
					db.InsertInIndexer(w,ID);
					WID=db.GetWordID(w);
					important=Importance(Importants[0],w,Importants[1],Importants[2]);
					db.InsertInWordPositions(WID,doc_id,pos,important);	
				}
			}
			else if(!w.equals(sw)&&db.CheckWordExistance(sw)&&!db.CheckWordExistance(w))
			{
				long ID=db.GetWordID(sw);	
				db.InsertInIndexer(w,ID);
				WID=db.GetWordID(w);
				important=Importance(Importants[0],w,Importants[1],Importants[2]);
				db.InsertInWordPositions(WID,doc_id,pos,important);
			
			}
			else if(w.equals(sw)&&!db.CheckWordExistance(sw)&&!db.CheckWordExistance(w))
			{
				if(!sw.equals(""))
					{
					db.InsertInIndexer(sw,-1);
					important=Importance(Importants[0],w,Importants[1],Importants[2]);
					WID=db.GetWordID(sw);
					db.InsertInWordPositions(WID,doc_id,pos,important);
					}
				
				
			}
		
		}
 }
 
	}


