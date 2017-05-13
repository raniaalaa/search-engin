import java.net.MalformedURLException;
import java.util.*;
import java.sql.Statement;
import java.io.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CRAWLER implements Runnable{
    private static final String useragent ="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    List<String> urlarray;

    private Document doc;
    private Document doc1;
    private DATABASE db=new DATABASE();
    int number;

    public CRAWLER( int n,boolean restart) throws SQLException, MalformedURLException ,IOException{ // mythread=t;



        urlarray=new ArrayList<String>();


        urlarray.add("https://en.wikipedia.org/wiki/main_Page");
        urlarray.add("https://answers.yahoo.com");
        urlarray.add("http://stackoverflow.com");
        urlarray.add("http://w3schools.com");
        urlarray.add("http://www.imdb.com");
        urlarray.add("http://www.wikihow.com");
        urlarray.add("https://opensource.org");
        urlarray.add("http://marketwatch.com");
        urlarray.add("http://www.carmagazine.co.uk");
        urlarray.add("http://www.bbc.com");
        urlarray.add("http://www.ebay.co.uk");
        urlarray.add("https://www.nytimes.com");
        urlarray.add("https://www.amazon.com");
        urlarray.add("http://unicef.org");
        urlarray.add("http://www.mti.edu.eg");
        urlarray.add("https://w3.org");
        urlarray.add("http://nature.com/index.html");
        urlarray.add("https://www.pinterest.com");
        urlarray.add("http://ibm.com/us-en");
        urlarray.add("https://www.microsoft.com");
        urlarray.add("https://dailytekk.com/");
        urlarray.add("http://www.weather.gov/");
        urlarray.add("https://www.poets.org");
        urlarray.add("http://www.food.com/");
        urlarray.add("https://www.booking.com/");
        urlarray.add("http://www.androidcentral.com/");
        urlarray.add("http://comicbook.com/");
        urlarray.add("http://youtube.com/");
        urlarray.add("https://indy100.com");
        urlarray.add("https://www.cnet.com/");
        urlarray.add("https://tripadvisor.com");
        urlarray.add("http://huffingtonpost.com");
        urlarray.add("https://ebookfriendly.com/");
        urlarray.add("https://thebalance.com");
        urlarray.add("https://bigfuture.collegeboard.org/");
        urlarray.add("https://skift.com/");
        if(restart)
        {
            db.Restart(urlarray);

            for(int i=0;i<urlarray.size();i++)
            {

                System.out.println(i+" "+urlarray.get(i));
                String url=db.Normalize(urlarray.get(i));
                try{doc = Jsoup.connect(url).timeout(8000).userAgent(useragent).get();
                }
                catch (IOException e)
                {
                    db.SetInvalid(url);
                    continue;
                }
                boolean flag3=IsHTML(doc.html());
                boolean f1=doc.html().equals("");
                if(!f1&&flag3)
                {
                    DoNotCrawl(url, url);
                    db.Insert("",url,0, "");
                    db.UpdateDoc(url,doc.html());
                    db.Updatefile(url);

                }
            }
        }
        number=n;

    }
    public  void DoNotCrawl(String url,String now) throws SQLException
    {

        try
        {   String str,robot,temp;
            str=robot=temp="";
            InputStream inputStream;
            temp=url+"/robots.txt";

            URL myURL = new URL(temp);
            inputStream=myURL.openStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader in = new BufferedReader(inputStreamReader);
            String arr[];
            str=in.readLine();
            if(str!=null)
                str= str.toLowerCase();
            if(str!=null && str.contains("user-agent: *"))
            {
                while(str!=null)
                {
                    if(str.contains("disallow:"))
                    {
                        arr=str.split(" ");
                        if(arr.length<2)
                            return;
                        robot=url+arr[1];
                        //insert in the DB
                        Statement st = db.conn.createStatement();
                        boolean flag=db.Check_Restrictedurl(robot);
                        if(flag) {
                            String query = "INSERT INTO `restrictedurls`(`ulr`, `id`) VALUES ('" + robot + "',NULL)";

                            st.executeUpdate(query);
                        }

                    }
                    robot+=str;
                    str=in.readLine();
                    if(str!=null)
                        str= str.toLowerCase();
                }

            }

        }
        catch(IOException e)
        {
            return;

        }


    }
    public  boolean CheckRobotDisallow(String url) throws SQLException, IOException
    {

        String sql="SELECT id FROM `restrictedurls` WHERE ulr='"+url+"'";
        ResultSet rs;
        Statement sta = db.conn.createStatement();
        rs = sta.executeQuery(sql);

        if(!rs.isBeforeFirst())
        {

            return true;
        }
        else {

            return false;
        }
    }
    public boolean IsHTML(String text) throws IOException
    {
        if(!text.equals("")) {
            if (text.contains("<!doctype html>") || text.contains("<html>"))
                return true;

            else return false;}
        return false;
    }

    public void GetURLText(String url) throws SQLException, IOException {

        URL url1 = new URL(url);
        BufferedReader in = new BufferedReader( new InputStreamReader(url1.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);

    }


    public void Crawl(String url) throws SQLException, IOException {
        if (url != null && !url.equals("")) {
            int id = Integer.parseInt(Thread.currentThread().getName());
            System.out.println("Thread num " + id + " will Crawl with " + url);
            try {
                doc = Jsoup.connect(url).timeout(8000).userAgent(useragent).get();
            } catch (IOException e) {
                db.SetInvalid(url);
                return;
            }


            if (!db.Getfile(url)) {
                boolean flag3 = IsHTML(doc.html());
                boolean f1 = doc.html().equals("");
                if (!f1 && flag3) {
                    DoNotCrawl(url, url);
                    db.UpdateDoc(url, doc.html());
                    db.Updatefile(url);
                }


            }

            org.jsoup.select.Elements links = doc.select("a[href]");

            int OutLinks = links.size();
            boolean flagrobot, flagexist = false;
            for (Element e : links)

            {
                String tmpurl = db.Normalize(e.attr("abs:href"));
                flagrobot = CheckRobotDisallow(tmpurl);
                flagexist = db.Check_Exist(tmpurl);
                if (!flagexist) {
                    if (!url.equals(tmpurl)&& !db.GetUpdated(url)) {
                        db.update_rank(url, tmpurl, OutLinks);
                    }
                    continue;
                }

                if (flagrobot) {

                    synchronized (db) {
                        doc1 = Jsoup.connect(tmpurl).timeout(8000).userAgent(useragent).get();
                        flagexist = db.Check_Exist(tmpurl);
                        if (!flagexist) {
                            if (!db.GetUpdated(url)) {
                                db.update_rank(url, tmpurl, OutLinks);

                            }
                            long docid = db.Getid(tmpurl);
                            String content = new Scanner(new File(docid + ".html")).useDelimiter("\\Z").next();

                            Document tmpdoc = Jsoup.connect(tmpurl).timeout(3000).userAgent(useragent).get();
                            if (!content.equals(tmpdoc.html())) {

                                db.UpdateDoc(tmpurl, tmpdoc.html());
                                db.ResetUrl(tmpurl);
                                continue;
                            } else if (flagexist) {


                                // synchronized (db)

                                if (flagrobot && flagexist) {
                                    try {

                                        db.Insert(url, tmpurl, OutLinks, "");
                                    } catch (IOException e1) {
                                        db.SetInvalid(tmpurl);
                                        continue;
                                    }
                                }
                            }
                            DoNotCrawl(tmpurl, url);
                            if (!doc1.html().equals("") && IsHTML(doc1.html())) {
                                db.UpdateDoc(tmpurl, doc1.html());
                                db.Updatefile(tmpurl);
                            }
                        }

                    }

                }
            }
        }

    }    @Override
    public void run() {
        String url="";
        int id=Integer.parseInt(Thread.currentThread().getName());

        int count=0;
        String currentUrl="";
        try {

            count = db.GetCount();
        } catch (SQLException e1) {

        }
        while(true){
            try {
                {synchronized(db){

                    currentUrl = db.GetURL(number,id);
                    db.UpdateVisted(currentUrl);}
                }
                Crawl(currentUrl);
                count =db.GetCount();
            }
            catch (SQLException |IOException e ) {
                System.out.println("invalid url ");
            }
            continue;
        }
    }

}

