/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package Project;

/**
 *
 * @author mennna
 */
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//class to dealing with files
public class FILER {
	private static  String Text;
	
	public FILER()
	{
		Text="";
	}
    public static String GetText()   //function to return the text of the document
    {
    	return Text;
    }
    public static String getDescription(String query,long Doc_id) throws FileNotFoundException, IOException
    { 
        boolean phrase;
        String description="";
        String content = "" ;
        File f=new File("C:\\Users\\mennna\\Documents\\NetBeansProjects\\Search\\"+Doc_id+".html");
        org.jsoup.nodes.Document doc=Jsoup.parse(f, "UTF-8");
        content=content+" "+doc.text();
        content= content.toLowerCase();
                if (query.endsWith("\"")==true && query.startsWith("\"")==true)
            {
                phrase=true;
            }
                else
                    phrase=false;
        int query_length=0;
       
        String query_words[] = query.split("\\P{Alpha}+");
        query_length=query_words.length;
        String words[] = content.split("\\P{Alpha}+");
        int index = ArrayUtils.indexOf(words, query_words[0]);
        System.out.println("index "+index);
        int i=0,start=0,end=0;
        if(phrase&&query_length>1)
        {
            if(index-10<0)
                    start=0;
                else
                    start=index-10;
                if(index+20>content.length()-1)
                    end=content.length()-1;
                else
                    end=index+20;
                  for( i=start;i<end;i++)
                {
                   if( query.indexOf(words[i]) != -1)
                { 
                        description+="<b> "+words[i]+"</b>"; 
                }
            else
                description+=" "+words[i];
        }
                
        }
        else if(query_length==1)
        { 
                if(index-10<0)
                    start=0;
                else
                    start=index-10;
                if(index+20>content.length()-1)
                    end=content.length()-1;
                else
                    end=index+20;
                    for( i=start;i<end;i++)
                {
                    if(words[i].equals(query))
                     { 
                        description+="<b> "+words[i]+"</b>";
                     }
                    else
                    { 
                        description+=" "+words[i];
                    }
                } 
         }
        else if(!phrase&&query_length>1)
        { 
            if(index-10<0)
                    start=0;
                else
                    start=index-10;
                if(index+20>content.length()-1)
                    end=content.length()-1;
                else
                    end=index+20;
                  for( i=start;i<end;i++)
                {
                   if( query.indexOf(words[i]) != -1)
                { 
                        description+="<b> "+words[i]+"</b>"; 
                }
            else
                description+=" "+words[i];
        }
        }
          System.out.println("description  "+description);
                return description;
    }
    
	public static  String [] Dealing_Files(File f) throws IOException  //return array of important strings in the file
	{ 
		Text="";
		String [] Importants={"","","",""}; //first element is the title,second is all headers,third is img alt,4th is the url
		org.jsoup.nodes.Document doc=Jsoup.parse(f, "UTF-8");
		Importants[0]=doc.title(); //get the title of the file
		//Text=Text+" "+doc.title(); 
	        String tag="h";
	        String All_Headers="";
		Elements Header;
	        for(int i=1;i<20;i++) //loop to get text with headers tag of the file
		 {  
		   tag="h"+String.valueOf(i);
		   Header = doc.select(tag);
		   if(Header.size()>0)
			{
				Header= doc.getElementsByTag(tag);
				String pConcatenated="";
				for (Element x: Header) 
				{
				         pConcatenated+= x.text()+" ";
				}
				All_Headers=All_Headers+pConcatenated;
			}
			else
					break;
				
		 }
	         Importants[1]=All_Headers;
		 Text=Text+" "+doc.text();  //get the text of the document
		 Elements img = doc.getElementsByTag("img"); //get the text with img tag 
		 for (Element element : img) 
		 {
			if(element.attr("alt") != null && !(element.attr("alt").equals("")))
			   {
			     Text=Text+" "+element.attr("alt");
			     Importants[2]=Importants[2]+" "+element.attr("alt");
			   }
	      }
       return Importants;
	}

}

