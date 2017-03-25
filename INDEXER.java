import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.*;
public class INDEXER {
static DATABASE db=new DATABASE();
//////////////////////////////////////Stopping Words List///////////////////////////////
static List<String> stopwords = Arrays.asList( 
		"a", "about", "above", "above", "across", "after"
		, "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also",
		"although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and", "another",
		"any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back",
		"be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind",
		"being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom", "but", "by", "call",
		"can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done",
		"down", "due", "during", "each", "eg", "eight", "either", "eleven", "else", "elsewhere", "empty", "enough",
		"etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify",
		"fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", 
		"front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here"
		, "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however",
		"hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last",
		"latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill",
		"mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", 
		"neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing",
		"now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others",
		"otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps", "please", 
		"put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she",
		"should", "show", "side", "since", "sincere", "six", "sixty", "so","some", "somehow", "someone", "something",
		"sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their"
		, "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon"
		, "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout",
		"thru","thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", 
		"under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever",
		"when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", 
		"wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why",
		"will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves",
		 "terms", "CONDITIONS", "conditions", "values", "interested.", "care",
		"sure", "contact", "grounds", "buyers", "tried", "said,", 
		"plan", "value", "principle.", "forces", "sent:", "is,", "was", "like", "discussion", "tmus", "diffrent.", 
		"layout", "area.", "thanks", "thankyou", "hello", "bye", "rise", "fell", "fall", "psqft.",
		"http://", "km", "miles");
/*
		"a", "able", "about",
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
		"will", "with", "would", "yet", "you", "your"*/



INDEXER()
{}
public static int Importance(String [] importance,String word) 
{
	for (String title_element: importance[0].split("\\P{Alpha}+")) 
	{
		if(word.equals(title_element.toLowerCase()))
		{
			return 4;
		}
	}
	for (String img_element: importance[2].split("\\P{Alpha}+")) 
	{
		if(word.equals(img_element.toLowerCase()))
		{
			return 3;
		}
	}
	for (String header_element: importance[1].split("\\P{Alpha}+")) 
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
 
 public static void Run(String txt,long doc_id,String [] Importants) throws Exception
 {
	 System.out.println("enter run");
	// System.out.println(txt);
	    String Expression = "";
	    int pos=0;
	    int start_pos=0;
	    int end_pos=0;
//////////////////////////////////////parse html file /////////////////////////////////
		String w,sw,w1;
		long WID=0,SWID=0;
		boolean WordExist,SWordExist;
		int importantW,importantSW;
		System.out.println("abl l mapppppppp");
	    Map<Pair,String> Originalwords = new HashMap<Pair,String>();
	    Map<Pair,Pair> words = new HashMap<Pair,Pair>();

	    System.out.println("b3d map l words");
	    Map<Pair,Integer> expressionsMap = new HashMap<Pair,Integer>(); //Pair.left->l expression,Pair.right->importance,integer->count

	    System.out.println("b3d map l exoressions");
	    String position="";
        String[] parts = txt.split("\\P{Alpha}+");
        Pair<String,Integer> pSW;
        Pair<String,Integer> pW;
        Pair<String,String> PosStem;

		while(pos<parts.length)
		{
			//System.out.println("posssssss"+pos);
		//	System.out.println("while");
			String Phrase="";
			Expression = "";
			String First_Stop_Word="";
			w=parts[pos].toLowerCase();
			if(w.equals(""))
				{
				pos++;
				continue;
				}

			if(!stopwords.contains(w))
			{
				if(w.equals(""))
					continue;
				importantW=Importance(Importants,w);
				pW=Pair.of(w,importantW);
			//	System.out.println(pW.getLeft());
				//System.out.println(pW.getRight());
				WordExist=words.containsKey(pW);
				///////////////////////////////////////////////////////////////////////////////////////
				//stemming
				sw=Stemmer(w);
				importantSW=Importance(Importants,sw);
				pSW=Pair.of(sw,importantSW);
				SWordExist=words.containsKey(pSW);
				
				/////lw msh bysaww b3d wl etnen msh mawgoden n7ot el sw w n7ot el w wl stemmingid bta3ha
				if(!w.equals(sw)&&!WordExist&&!SWordExist)
				{
					/// insert the stemming and the word
					if(!sw.equals(""))
					{
						words.put(pSW,Pair.of("NO","NULL"));
					}
					//	Originalwords.put(pSW,"NO");  // ("NO") Donot Insert It Into Position
					position=","+Integer.toString(pos);
					PosStem=Pair.of(position,sw);
					words.put(pW,PosStem);
				}
				else if(!w.equals(sw)&&!WordExist&&SWordExist)
				{
					// insert the word
					position=","+Integer.toString(pos);
					PosStem=Pair.of(position,sw);
					words.put(pW,PosStem);
				}
				else if(w.equals(sw)&&!WordExist&&!SWordExist)
				{
					// insert one of them
					position=","+Integer.toString(pos);
				//	Originalwords.put(pSW,position);
					words.put(pSW,Pair.of(position,"NULL"));

				}
				else if(SWordExist&&w.equals(sw))
				{
					// add the new position
					//position=Originalwords.get(pSW)+","+Integer.toString(pos);
				//	Originalwords.put(pSW,position);
					position=words.get(pSW).getLeft()+","+Integer.toString(pos);
					words.put(pSW,Pair.of(position,"NULL"));

				}
				else if(WordExist&&!w.equals(sw))
				{
					// add the new position
					position=words.get(pW).getLeft()+","+Integer.toString(pos);
					words.put(pW,Pair.of(position,sw));
				}
				
				///////////////////////////////////////////////////////////////////////////////////////
				pos=pos+1;

			  }
			else
			{
	////////////////////////////////////////Handleing_Stop_Words///////////////////////////////////////
				Phrase="";
				if ((!w.equals("a")&&!w.equals("an")&&!w.equals("the")&&!w.equals("is")&&!w.equals("if")&&!w.equals("were")&&!w.equals("was")&&!w.equals("i")&&!w.equals("of")&&!w.equals("and")&&!w.equals("in")&&!w.equals("it")&&!w.equals("us")&&!w.equals("our")&&!w.equals("in")&&!w.equals("your")&&!w.equals("you")&&!w.equals("yours")&&!w.equals("on")))
					{
				First_Stop_Word=w;
				//System.out.println("First_Stop_Word----------------->"+w);
				start_pos=pos;
				pos++;
					}
				else
				{
					pos++;
					continue;
				}
				
				if(pos<parts.length)
					{
					w=parts[pos].toLowerCase();
					Boolean d=stopwords.contains(w);
				   while (pos<parts.length&&stopwords.contains(w)&&(!w.equals("a")&&!w.equals("an")&&!w.equals("the")&&!w.equals("is")&&!w.equals("if")&&!w.equals("were")&&!w.equals("was")&&!w.equals("i")&&!w.equals("of")&&!w.equals("and")&&!w.equals("in")&&!w.equals("it")&&!w.equals("us")&&!w.equals("our")&&!w.equals("in")&&!w.equals("your")&&!w.equals("you")&&!w.equals("yours")&&!w.equals("on")))
						{
						//System.out.println("b3d kedaaaaaaaaa---------->"+w);
					     w=parts[pos].toLowerCase();
					     if(!w.equals("")&&!w.equals(" "))
						   {
					    	 Phrase=Phrase+" "+w;
						   }
						 pos++;
						if(pos<parts.length)
							 w=parts[pos].toLowerCase();
						else 
							break;
						}
			   }
		    	if(Phrase.length()>1)
					{  
						long EID;
						int important,count;
						Expression=First_Stop_Word+Phrase;
						//System.out.println("expressionnnnnnnn------>"+Expression);
						important=Importance(Importants,Expression);
						Pair<String,Integer> p=Pair.of(Expression,important);
						end_pos=pos;
						if(expressionsMap.containsKey(p))   //l kelma mogoda 2bl keda
							{
							count=expressionsMap.get(p);
							count++;
							}
						else
							{
							count=1;           //l kelma msh mogoda 2bl keda
							}
						
						if(!Expression.equals(""))
						expressionsMap.put(Pair.of(Expression,important),count);  
			        }
				else 
					Phrase="";	
			}
			
	   }
		System.out.println("5alst l whileeeeeeeeee");
	//   db.InsOriginalWords(Originalwords,doc_id);
	   db.InsWords(words,doc_id);
		System.out.println("expressionssssssssssssssssss");
	   db.InsExpressions(expressionsMap, doc_id);
	   System.out.println("5alst l insert");
	   Originalwords.clear();
		 System.out.println("fffffffffffffffffffffffffffffff");

 }
}

