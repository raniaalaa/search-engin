/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

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
        String description="";
        String content = "" ;
        //new Scanner(new File("C:\\Users\\mennna\\Documents\\NetBeansProjects\\Search\\11.html")).useDelimiter("\\Z").next();
        File f=new File("C:\\Users\\mennna\\Documents\\NetBeansProjects\\Search\\"+Doc_id+".html");
        org.jsoup.nodes.Document doc=Jsoup.parse(f, "UTF-8");
        content=content+" "+doc.text();
        //System.out.println(content);
        content= content.toLowerCase();
         System.out.println("contenttttttt "+content);
        //int index = content.indexOf(query);
               //System.out.println("index "+index);
        String words[] = content.split("\\P{Alpha}+");
       int index = ArrayUtils.indexOf(words, query);
        System.out.println("lengthhhhhhhhhhhhhhhh"+words.length);
                int i,start,end;
                if(index-10<0)
                    start=0;
                else
                    start=index-10;
                if(index+20>content.length()-1)
                    end=content.length()-1;
                else
                    end=index+20;
                                System.out.println("hereee "+query+" "+Doc_id+" "+index+" "+start+" "+end);
                for( i=start;i<end;i++)
                {
                    description+=" "+words[i];
                } 
                return description;
    }
    
	public static  String [] Dealing_Files(File f) throws IOException  //return array of important strings in the file
	{ 
		Text="";
		String [] Importants={"","",""}; //first element is the title,second is all headers,third is img alt
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

