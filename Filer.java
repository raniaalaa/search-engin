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



public class Filer {
	private static  String Text;
	
	public Filer()
	{
		Text="";
	}
    public static String GetText()
    {
    	return Text;
    }
	public static  String [] Dealing_Files(File f) throws IOException
	{
		String [] Importants={"",""}; //first element is the title,second is all headers
		org.jsoup.nodes.Document doc=Jsoup.parse(f, "UTF-8");
		Elements Head = doc.head().select("*");
		for (Element element :Head) 
		{
			Importants[0]=doc.title();
			Text=Text+element.ownText(); //da l mafrod yrg3 l 7aga elly fe l head
		}
		Elements Body = doc.body().select("*");
		for (Element element :Body) 
		{//ageeb l tags
			 String tag="h";
			   String All_Headers="";
			   Elements Header;
			   for(int i=1;i<20;i++)
				{  
				   tag="h"+String.valueOf(i);
				   Header = doc.select(tag);
				  // System.out.println("size  "+Header.size());
				if(Header.size()>0)
					{
				Header= doc.getElementsByTag(tag);
				String pConcatenated="";
				        for (Element x: Header) {
				            pConcatenated+= x.text()+" ";
				        }
				        All_Headers=All_Headers+pConcatenated;
					}
				else
					break;
				
				}
			   Importants[1]=All_Headers;
			   Text=Text+element.ownText();  //da l mafrod yrg3 l 7aga elly fe l body
		}
       return Importants;
	}

}
