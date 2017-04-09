import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;

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
