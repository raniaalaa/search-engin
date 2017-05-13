import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class UpdateRank implements Runnable{
    private static final String useragent ="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

	public void run()
	{
		String url;
		Document doc;
		ResultSet rs;
        int OutLinks;
	    String tmpurl;
		while(true)
		{
			try {
				rs=DATABASE.Get_TORank();
				if(rs.next())
				{
					url=rs.getString(2);
					System.out.println("raaaaaaaank  "+url);
					if(!url.equals("")&&url!=null)
					{
						doc = Jsoup.connect(url).timeout(8000).userAgent(useragent).get();
				        org.jsoup.select.Elements links=doc.select("a[href]");
						OutLinks=links.size();
				        for(Element e: links)		
				        {
				            tmpurl=DATABASE.Normalize(e.attr("abs:href"));
				            if(!DATABASE.Check_Exist(tmpurl))
				            {
			                	 DATABASE.ConRanking(tmpurl,rs.getDouble(3),OutLinks);
		
				            }
				        }
					}
				}
		        
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}