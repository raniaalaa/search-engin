import java.io.File;
import javax.swing.text.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



public class FILER {
	private static  String Text;
	
	public FILER()
	{
		Text="";
	}
    public static String GetText()
    {
    	return Text;
    }
	public static  String [] Dealing_Files(File f) throws IOException
	{ 
		Text="";
		String [] Importants={"","",""}; //first element is the title,second is all headers,third is img alt
		org.jsoup.nodes.Document doc=Jsoup.parse(f, "UTF-8");
		Importants[0]=doc.title();
		
			 String tag="h";
			   String All_Headers="";
			   Elements Header;
			   
			   for(int i=1;i<20;i++)
				{  
				   tag="h"+String.valueOf(i);
				   Header = doc.select(tag);
				if(Header.size()>0)
					{
				Header= doc.getElementsByTag(tag);
				String pConcatenated="";
				        for (Element x: Header) {
				            pConcatenated+= x.text()+" ";
				            System.out.println("pConcatenated  "+pConcatenated);
				        }
				        All_Headers=All_Headers+pConcatenated;
				        System.out.println("All_Headers "+All_Headers);
					}
				else
					break;
				
				}
			   Importants[1]=All_Headers;
			  Text=Text+" "+doc.text();
			  String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
				Elements img = doc.getElementsByTag("img");
				   for (Element element : img) 
				   {
			            if(element.attr("alt") != null && !(element.attr("alt").equals("")))
			            {
			            	Text=Text+" "+element.attr("alt");
			            	Importants[2]=Importants[2]+" "+element.attr("alt");
			            }
			        }
				  
			   System.out.println("text  "+Text);
			
       return Importants;
	}

}