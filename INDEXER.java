import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
public class INDEXER {
static DATABASE db=new DATABASE();
static Map<String,String> expressionsMap = new HashMap<String,String>(); //Pair.left->l expression,Pair.right->importance,integer->count
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

INDEXER()
{}
////////////////////////////////Get the importance of a word////////////////////////////////////////////
public static int Importance(String [] importance,String word) 
{
	for (String title_element: importance[0].split("\\P{Alpha}+")) ///If title -> 4
	{
		if(word.equals(title_element.toLowerCase()))
		{
			return 4;
		}
	}
	for (String img_element: importance[2].split("\\P{Alpha}+"))  ///If image -> 3
	{
		if(word.equals(img_element.toLowerCase()))
		{
			return 3;
		}
	}
	for (String header_element: importance[1].split("\\P{Alpha}+")) ///If header -> 2
	{
		if(word.equals(header_element.toLowerCase()))
		{
			return 2;
		}
	}
	return 1;                                                       ///Else ->1
	 
}

////////////////////////////////Get the importance of an expression////////////////////////////////////////////
public static int Expression_Importance(String [] importance,String expression) 
{
	if(importance[0].contains(expression))
		return 4;
	if(importance[1].contains(expression))
		return 3;
	if(importance[2].contains(expression))
		return 2;
	return 1;                                                       ///Else ->1

}
/////////////////////////////////Get the stemming of a word//////////////////////////////////////////
public static String Stemmer(String word)
{
	 return porterStemmer.stemTerm(word); 
}
 /////////////////////////////////////Run the indexer//////////////////////////////////////////////
 public static void Run(String txt,long doc_id,String [] Importants) throws Exception
 {      
	    if(DATABASE.wordsCount()>0)
	    	DATABASE.rstUpdated(doc_id);
	    int wordCount=1,swordCount=1;
	    long docSize=0;
        String[] parts = txt.split("\\P{Alpha}+");
	    String Expression = "",position="",Phrase="",First_Stop_Word="",w,sw;
	    int pos=0,importantW,importantSW;
		boolean WordExist,SWordExist;
		//first string->word,integer->importance,string->positions,string->stemming,integer->count
	    Map<Pair<String,Integer>,Pair<String,Pair<String,Integer>>> words = new HashMap<Pair<String,Integer>,Pair<String,Pair<String,Integer>>>();
        Pair<String,Integer> pSW;
		Pair<String,Integer> pW;
        Pair<String, Pair<String, Integer>> PosStem;
	    //////////////////////////////////////parse html file /////////////////////////////////
		while(pos<parts.length)
		{
			Phrase="";
			Expression = "";
			First_Stop_Word="";
			w=parts[pos].toLowerCase();
			docSize++;
			if(w.equals(""))
			{
				pos++;
				continue;
			}
			if(!stopwords.contains(w))
			{
				if(w.equals(""))
					continue;
				///////////////////////// importantW is the importance of the word////////////////////
				importantW=Importance(Importants,w);
				pW=Pair.of(w,importantW);    /////pW is the pair of (the word , pair of(importance,count))
				WordExist=words.containsKey(pW);
				if(WordExist)
				{
					wordCount=words.get(pW).getRight().getRight()+1;
				}
				else
				{
					wordCount=1;
				}
				////////////////// sw is the stemming of the word/////////////////////////////////////
				sw=Stemmer(w);
				///////////////// importantSW is the importance of the word after stemming ///////////
				importantSW=Importance(Importants,sw);
				pSW=Pair.of(sw,importantSW);        /////pSW is the pair of (the word after stemming , importance)
				SWordExist=words.containsKey(pSW);
				////////If the word != the word after the stemming and both of them doesn't exist add them///////////////////
				if(!w.equals(sw)&&!WordExist&&!SWordExist)
				{
					/// insert the stemming and the word
					if(!sw.equals(""))
						words.put(pSW,Pair.of("",Pair.of("NULL",wordCount)));
					position=","+Integer.toString(pos);
					
					PosStem=Pair.of(position,Pair.of(sw,wordCount));
					words.put(pW,PosStem);
					wordCount++;
				}
				//////If the word != the word after the stemming but the word after the stemming is exist just add the word/////
				else if(!w.equals(sw)&&!WordExist&&SWordExist)
				{
					// insert the word
					position=","+Integer.toString(pos);
					PosStem=Pair.of(position,Pair.of(sw,wordCount));
					words.put(pW,PosStem);
				}
				//////If the word = the word after the stemming and it is not exist add it//////////////////////
				else if(w.equals(sw)&&!WordExist&&!SWordExist)
				{
					// insert one of them
					position=","+Integer.toString(pos);
					words.put(pSW,Pair.of(position,Pair.of("NULL",wordCount)));

				}
				//////If the word = the word after the stemming and it is exist just add the new position //////
				else if(SWordExist&&w.equals(sw))
				{
					// add the new position
					position=words.get(pSW).getLeft()+","+Integer.toString(pos);
					words.put(pSW,Pair.of(position,Pair.of("NULL",wordCount)));
				}
				/////If the word != the word after the stemming and they are exist add the new position for the word////////// 
				else if(WordExist&&!w.equals(sw))
				{
					// add the new position
					position=words.get(pW).getLeft()+","+Integer.toString(pos);
					words.put(pW,Pair.of(position,Pair.of(sw,wordCount)));
				}
				///////////////////////////////////////////////////////////////////////////////////////
				pos=pos+1;

			  }
			else 	//////////////////////////////////Handleing_Stop_Words//////////////////////////////
			{
				Phrase="";
				/////////////////////////////////Ignore these words ///////////////////////////////////////
				if ((!w.equals("a")&&!w.equals("an")&&!w.equals("the")&&!w.equals("is")&&!w.equals("if")&&!w.equals("were")&&!w.equals("was")&&!w.equals("i")&&!w.equals("of")&&!w.equals("and")&&!w.equals("in")&&!w.equals("it")&&!w.equals("us")&&!w.equals("our")&&!w.equals("in")&&!w.equals("your")&&!w.equals("you")&&!w.equals("yours")&&!w.equals("on")))
				{
					First_Stop_Word=w;
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
					///////////////////////////// Phrase = Expressions ////////////////////////////////
					while (pos<parts.length&&stopwords.contains(w)&&(!w.equals("a")&&!w.equals("an")&&!w.equals("the")&&!w.equals("is")&&!w.equals("if")&&!w.equals("were")&&!w.equals("was")&&!w.equals("i")&&!w.equals("of")&&!w.equals("and")&&!w.equals("in")&&!w.equals("it")&&!w.equals("us")&&!w.equals("our")&&!w.equals("in")&&!w.equals("your")&&!w.equals("you")&&!w.equals("yours")&&!w.equals("on")))
					{
						w=parts[pos].toLowerCase();
					     if(!w.equals("")&&!w.equals(" "))
					    	 Phrase=Phrase+" "+w;
						 pos++;
						if(pos<parts.length)
							 w=parts[pos].toLowerCase();
						else 
							break;
					}
				}
				if(Phrase.length()>1)
				{
					int important;
					String LastCount;
					Expression=First_Stop_Word+Phrase;
					important=Expression_Importance(Importants,Expression);
					String Value="";
					long LastDoc;
					if(expressionsMap.containsKey(Expression))   //If this expression is exist in the map
					{							
						Value=expressionsMap.get(Expression);
				        String[] ExPerDoc = Value.split(",");
				        LastCount=ExPerDoc[ExPerDoc.length-1];
				        LastDoc=Long.parseLong(ExPerDoc[ExPerDoc.length-3]);
				     // If this expression is exist in the same file
				        if(LastDoc==doc_id)
				        {
				        	LastCount=Integer.toString((Integer.parseInt(LastCount)+1));
				        	Value=Value.substring(0,Value.lastIndexOf(','));
				        	Value=Value+","+LastCount;
				        }
				        //////////////////////If it is the first time in this document
				        else
				        {
					        Value=Value+","+Long.toString(doc_id)+","+Integer.toString(important)+",1";
				        }
					}
					else                                //If this the first time for this expression
					{
						Value=Long.toString(doc_id)+","+Integer.toString(important)+",1";
					}
					if(!Expression.equals(""))
						expressionsMap.put(Expression,Value);   
				}
				else 
					Phrase="";	
			}		
	   }
	   DATABASE.InsWords(words,doc_id,docSize,Importants);                    // Insert the words into the database 
	   DATABASE.DeleteNotUpdated(doc_id);
	   words.clear();

 }
 public static void InsEx() throws Exception
 {
 	DATABASE.InsExpressions(expressionsMap);   // Insert the expressions into the database
 }
}

