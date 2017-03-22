import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.io.*;
import java.sql.Statement;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.MalformedURLException;

import ch.sentric.URL;

public class DATABASE {
    public static Connection conn = null;
    public DATABASE() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Search Engine";
            // conn = DriverManager.getConnection(url, "root", "");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SearchEngine?autoReconnect=true&useSSL=false","root","");
            System.out.println("conn built");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet RunSql(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.executeQuery(sql);
    }

    public boolean RunSql2(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.execute(sql);
    }
    public void Restaet() throws SQLException {
        String sql;
        Statement sta;
        ResultSet rs;
        String[] query={"DELETE FROM `expressionspositions` WHERE 1;","DELETE FROM `expressions` WHERE 1;","DELETE FROM `wordpositions` WHERE 1;","DELETE FROM `word` WHERE 1;","DELETE FROM `record` WHERE 1;","DELETE FROM `restrictedurls` WHERE 1;","ALTER TABLE `record` AUTO_INCREMENT = 1;","INSERT INTO `searchengine`.`record` (`ID`, `URL`, `document`, `Visted`, `file`,`invalid`) VALUES (NULL, 'http://resource-zone.com', '', '0', '0','0'),(NULL, 'http://en.wikipedia.org/wiki/main_page', '', '0', '0','0'), (NULL, 'http://stackoverflow.com', '', '0', '0','0'), (NULL, 'http://nature.com/index.html', '', '0', '0','0'), (NULL, 'http://w3.org', '', '0', '0','0'), (NULL, 'http://dmoztools.net', '', '0', '0','0'), (NULL, 'http://unicef.org', '', '0', '0','0') ,(NULL, 'http://ibm.com/us-en', '', '0', '0','0');"};
        for(int i=0;i<8;i++)
        {
	        sql=query[i];
	        sta = conn.createStatement();
	        sta.execute(sql);
        }
        return;
    }
    public String Normalize(String url) throws MalformedURLException
    {
    	URL u = new URL(url.toLowerCase());
 		url = u.getNormalizedUrl();
 		if(url.equals(""))
 			return url;
 		else
 	 		url = "http://"+url;
 		return url;
    }
    public boolean Check_Exist(String url) throws SQLException, MalformedURLException {
    	url=Normalize(url);
        String sql="SELECT id FROM `record` WHERE url='"+url+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        if(!rs.isBeforeFirst()){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean Check_Restrictedurl(String url) throws SQLException {
        String sql="SELECT id FROM `restrictedurls` WHERE ulr='"+url+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        if(!rs.isBeforeFirst()){
            return true;
        }
        else {
            return false;
        }
    }
    public void Insert(String url,String text) throws SQLException, MalformedURLException {

        if(url.equals(""))
            return;
        url=Normalize(url);
        if(url.equals(""))
            return;
        String sql="INSERT INTO `record`(`ID`, `URL`, `document`,  `Visted`, `file`, `invalid`) VALUES (NULL,'"+url+"','"+text+"','0','0','0');";
        //"insert into record(`ID`, `URL`, `Visted`,'file') VALUES (NULL,'"+url+"', '0','0') ";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }

    public void UpdateVisted(String url) throws SQLException, MalformedURLException {
        url=Normalize(url);
        String sql="UPDATE `record` SET `Visted`=1 WHERE URL ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }
    public void UpdateDoc(String url,String text) throws SQLException, MalformedURLException {
//UPDATE customers SET record = '"+Judy+"'WHERE url = '"url"'"";
        //BufferedWriter bw = null;
        // FileWriter fw = null;
      /*  try {
            String content = "This is the content to write into file\n";
            fw = new FileWriter("'"+url+"'"+".html");
            bw = new BufferedWriter(fw);
            bw.write(text);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    	url=Normalize(url);
        try{
            // BufferedWriter writer
            //FileWriter fw     	
            PrintWriter writer = new PrintWriter(Getid(url)+".html", "UTF-8");
            writer.print(text);
            writer.println("The second line");
            writer.close();
        } catch (IOException e) {
            System.out.println("hereeeeeeeeeeeeeeeeee");
        }
        // String sql="UPDATE record SET document = '"+text+"'WHERE url = '"+url+"'";
        //Statement sta = conn.createStatement();
        //sta.execute(sql);
    }
    public void Updatefile(String url) throws SQLException, MalformedURLException {
    	url=Normalize(url);
        String sql="UPDATE record SET `file` = '1' WHERE `record`.`url` ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }

    public int GetCount() throws SQLException {
        String sql="SELECT COUNT(*)  FROM `record` WHERE `file`=1;" ;

        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        int value=0;
        if (rs.next()) {
            value= Integer.parseInt(rs.getString(1));
          //  System.out.println(value);
            }
        return value;
    }
    public int Getid(String url) throws SQLException {

    	String sql="SELECT id FROM `record` WHERE url='"+url+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        int value=0;
        if (rs.next()) {
            value= Integer.parseInt(rs.getString(1));
            int id=Integer.parseInt(Thread.currentThread().getName());
            System.out.println("thread num"+id +" "+value);}
        return value;
    }
    public void SetInvalid(String url) throws SQLException {
        String sql="UPDATE `record` SET `invalid`=1 WHERE URL ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }
    public String GetURL(int n,int ind) throws SQLException, MalformedURLException {

        String sql="SELECT url FROM record where visted =0 and file=0 and invalid=0 LIMIT 1";
        Statement sta = conn.createStatement();
        String value="";

        ResultSet resultSet=sta.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
      //  int columnsNumber = rsmd.getColumnCount();
    //    int count=0;
        if (resultSet.next()) {
        	URL url = new URL(resultSet.getString(1).toLowerCase());
    		System.out.println("http://"+url.getNormalizedUrl());  
    		value = "http://"+url.getNormalizedUrl();
          //  System.out.println(value);
           // break;
        }
            //  for (int i = ind-1; i < columnsNumber; i++)
//            if(count==ind)
               
            	
  //          count++;
       
        // if (i > 1) System.out.print(",  ");
                /*String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
        if (resultSet.next()) {
            value = resultSet.getString(1);
            System.out.println(value);}*/
        return value;
    }
    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }

    /////////////functions of indexer////////////////////////////////////////////////////////
    public static void InsertInExpressions(String Expression) throws ClassNotFoundException, SQLException
    {
        PreparedStatement  statement = conn.prepareStatement("insert into `Expressions` (`Expression`,`E_ID`) values('"+Expression+"',NULL)");
        statement.execute();
    }
    public static void InsertInIndexer(String Word,long Sword) throws ClassNotFoundException, SQLException
    {
        PreparedStatement statement;
        if(Sword==-1)
            statement = conn.prepareStatement("insert into `Word` (`word`,`Wid`,`Stemming`) values('"+Word+"',NULL,NULL)");
        else
            statement = conn.prepareStatement("insert into `Word` (`word`,`Wid`,`Stemming`) values('"+Word+"',NULL,"+Sword+")");
        statement.execute();
    }
    public static void InsertInWordPositions(long w_id,long doc_id,int position,int importance) throws SQLException
    {
        //importance=3 if title,=2 if header,=1 else
        PreparedStatement statement;
        statement = conn.prepareStatement("insert into `wordpositions` (`w_id`,`doc_id`,`position`,`importance`) values("+w_id+","+doc_id+","+position+","+importance+")");
        statement.execute();
    }
    public static void InsertInExpressionsPositions(long expression_id,long doc_id,int start_position,int end_position,int important) throws SQLException
    {

        PreparedStatement statement;
        statement = conn.prepareStatement("insert into `expressionspositions` (`expression_id`,`doc_id`,`start_pos`,`end_pos`,`importance`) values("+expression_id+","+doc_id+","+start_position+","+end_position+","+important+")");
        statement.execute();
    }
    //Check exitance in word table
    public static Boolean CheckWordExistance(String Word) throws Exception
    {
        String queryCheck = "SELECT * from `Word` WHERE word = '"+Word+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());

    }
    //Check exitance in expression table
    public static Boolean CheckExpressionExistance(String Expression) throws Exception
    {
        String queryCheck = "SELECT * from `Expressions` WHERE Expression = '"+Expression+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());

    }
    public static long GetWordID(String Word) throws Exception
    {
        String queryCheck = "SELECT Wid from `Word` WHERE word = '"+Word+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet result = statement.executeQuery();

        if(result.next())
            return (result.getLong("Wid"));
        else
            return -1;
    }
    public static String GetText(int doc_id) throws Exception
    {
        String queryCheck = "SELECT document from `record` WHERE ID = '"+doc_id+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet result = statement.executeQuery();

        if(result.next())
            return (result.getString("document"));
        else
            return "";
    }
    public static long GetExpressionID(String Expression) throws Exception
    {
        String queryCheck = "SELECT E_ID from `Expressions` WHERE Expression = '"+Expression+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet result = statement.executeQuery();

        if(result.next())
            return (result.getLong("E_ID"));
        else
            return -1;
    }

   /* public  void DoNotCrawl(String url) throws SQLException, IOException
    {//Connection conn1 = null;
        String str,robot,temp;
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
        str= str.toLowerCase();
        if(str.contains("user-agent: *"))
        {
            while(str!=null)
            {
                if(str.contains("disallow:"))
                {
                    arr=str.split(" ");
                    robot=url+arr[1];
                    System.out.println(robot);
                    //insert in the DB
                    Statement st = conn.createStatement();
                    String query="INSERT INTO `restrictedurls`(`ulr`, `id`) VALUES ('"+robot+"',NULL)";
                    st.executeUpdate(query);
                    //
                }
                robot+=str;
                str=in.readLine();
                if(str!=null)
                str= str.toLowerCase();
            }
        }
    }*/
	/*public void crawl(String url)
    {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            System.out.println("Received web page at " + url);
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                this.links.add(link.absUrl("href"));
            }
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            System.out.println("Error in out HTTP request " + ioe);
        }
    }*/
}
