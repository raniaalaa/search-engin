import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import org.apache.commons.collections4.bag.SynchronizedBag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



//import com.mysql.fabric.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.net.URL;
//import org.apache.commons.io.FileUtils;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SPIDER implements Runnable{
    private static final String useragent =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    List<String> urlarray;

    Document doc;
    // public Connection conn = null;
    DATABASE db=new DATABASE();
    int number;
    Thread mythread ;
    public SPIDER(){}
    public SPIDER ( int n) throws SQLException
    { // mythread=t;
        // t.start();
        //  this.conn=conn;
        urlarray=new ArrayList<String>();
        urlarray.add("https://en.wikipedia.org/wiki/Main_Page");
        urlarray.add("http://stackoverflow.com/");
        urlarray.add("http://w3schools.com/");
        urlarray.add("https://www.tutorialspoint.com/");
        urlarray.add("http://illinois.edu/");
        urlarray.add("https://opensource.org/");
        // urlarray.add("http://www.dmoz.org/");
        urlarray.add("http://marketwatch.com/");
        urlarray.add("http://usnews.com/");
        urlarray.add("https://www.amazon.com/");
        urlarray.add("http://unicef.org/");
        urlarray.add("http://pen.io/");
        urlarray.add("https://w3.org/");
        urlarray.add("https://drupal.org/");
        urlarray.add("http://ibm.com/us-en/");
        urlarray.add("http://nature.com/index.html");
        urlarray.add("https://tripadvisor.com.eg/");
        urlarray.add("http://statcounter.com/");
        urlarray.add("http://tiny.cc/");

        number=n;

    }
    public  void DoNotCrawl(String url,String now) throws SQLException
    {//Connection conn1 = null;
        System.out.println("Thread num "+Thread.currentThread().getName()+" in DNC with "+now);

        try{   String str,robot,temp;
            str=robot=temp="";
            InputStream inputStream;
            temp=url+"/robots.txt";
            //URL myURL=new URL(url+"/robots.txt");
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
                        // System.out.println(robot);
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
            System.out.println("Thread num "+Thread.currentThread().getName()+" leaving DNC with "+now);
        }
        catch(IOException e)
        {
            return;

        }


    }
    public  boolean CheckRobotDisallow(String url) throws SQLException, IOException
    {
        System.out.println("Thread num "+Thread.currentThread().getName()+" in CRD");
        String sql="SELECT id FROM `restrictedurls` WHERE ulr='"+url+"'";
        ResultSet rs;
        Statement sta = db.conn.createStatement();
        rs = sta.executeQuery(sql);

        if(!rs.isBeforeFirst())
        {
            System.out.println("Thread num "+Thread.currentThread().getName()+" leaving CRD");
            return true;
        }
        else {
            System.out.println("Thread num "+Thread.currentThread().getName()+" leaving CRD");
            return false;
        }
    }
    public boolean IsHTML(String text) throws IOException
    {

        if(text.contains("<!doctype html>")||text.contains("<html>"))
            return true;

        else return false;
    }
    public void Crawl(String url) throws SQLException, IOException
    {if(url!=null&&!url.equals(""))
    { 
        int id=Integer.parseInt(Thread.currentThread().getName());
        System.out.println("Thread num "+id+" will Crawl with "+url);
        doc = Jsoup.connect(url).timeout(4000).userAgent(useragent).get();
        System.out.println("Jsoup connected for "+id);
        if(!doc.html().equals(""))
        {    db.UpdateDoc(url,doc.html());
            db.Updatefile(url);
            System.out.println("doc done from "+id+" with "+url);

        }
        System.out.println("jsoup done");
        org.jsoup.select.Elements links=doc.select("a[href]");
        boolean flag3=IsHTML(doc.html());

        db.UpdateVisted(url);

        boolean flag1,flag2;
        for(Element e: links)

        {  String tmpurl=e.attr("abs:href");
            DoNotCrawl(tmpurl,url);
         //   System.out.println("dont crawl");

            flag1=db.Check_Exist(tmpurl);

            flag2=CheckRobotDisallow(tmpurl);
          //  System.out.println("CheckRobotDisallow ");
            if(flag1&flag2&flag3)
            {
                db.Insert(tmpurl, "");
            //    System.out.println("insert ");
            }
            //   else System.out.print(" thread number" +id+" link exist in db ");


        }
        System.out.println("Thread num "+id+" leaving the Crawl");
    }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
      //  System.out.print("run ");
        String url="";
        int id=Integer.parseInt(Thread.currentThread().getName());
        System.out.println("Thread num "+id+" is running");
     //   url=urlarray.get(Integer.parseInt(Thread.currentThread().getName()));
        int count=0;
        String currentUrl="";
        try {
          //  System.out.print("r");
            count = db.GetCount();
        } catch (SQLException e1) {
            // TODO Auto-generated catch
            //System.out.print("rundsd ");
            e1.printStackTrace();
        }
        while(count<5000){
            try {//System.out.print("try");
                Statement st=db.conn.createStatement();

                ResultSet result;

                String q="select * from record";
                result=st.executeQuery(q);

                if(!result.next())// if empty
                {
                    currentUrl = url;
                }
                else
                {synchronized(this){
                    currentUrl = db.GetURL(number,id);
                    db.UpdateVisted(currentUrl);}
                }
                Crawl(currentUrl);
                count =db.GetCount();
            }
            catch (SQLException |IOException e ) {
                // TODO Auto-generated catch block
                System.out.println("invalid url ");
                try {
                    db.SetInvalid(currentUrl);
                } catch (SQLException e1) {
                   // e1.printStackTrace();
                }
                //  e.printStackTrace();
               /* try {
                    //int v,f;
                    //v=db.Getvisit(currentUrl);
                    //f=db.Getfile(currentUrl);
                    //if(v==1&&f==0)
                      //  db.ResetVisted(currentUrl);
                    continue;
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                   // e1.printStackTrace();
                    System.out.println("invalid ResetVisted ");
                }*/
                continue;
                //e.printStackTrace();
            } //catch (IOException e) {
            //System.out.print("invalid url ");

            //e.printStackTrace();
            //}
        }

    }

}
