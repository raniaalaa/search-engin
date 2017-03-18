//package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.io.*;
import java.sql.Statement;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DATABASE {
    public Connection conn = null;
    public DATABASE() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Search Engine";
            conn = DriverManager.getConnection(url, "root", "");
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
    public boolean Check_Exist(String url) throws SQLException {
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
    public void Insert(String url,String text) throws SQLException {

        String sql="INSERT INTO `record`(`ID`, `URL`, `document`,  `Visted`, `file`) VALUES (NULL,'"+url+"','"+text+"','0','0');";
        //"insert into record(`ID`, `URL`, `Visted`,'file') VALUES (NULL,'"+url+"', '0','0') ";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }

    public void UpdateVisted(String url) throws SQLException {

        String sql="UPDATE `record` SET `Visted`=1 WHERE URL ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }
    public void UpdateDoc(String url,String text) throws SQLException {
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
       try{
            int id=Getid(url);
           String filename=id+".txt";
           File myfile = new File(filename);
           FileWriter writer = new FileWriter(myfile, true);
           writer.write(text);

        //   FileWriter writer = new FileWriter(textname, true);
          // writer.write(text);
           //writer.flush();
           writer.close();
           // BufferedWriter writer
            //FileWriter fw
         /*  File file = new File("'"+url+"'"+".html");

           // creates the file
           file.createNewFile();

           // creates a FileWriter Object
           FileWriter writer = new FileWriter(file);

           // Writes the content to the file
           writer.write(text);
           writer.flush();
           writer.close();
           /* PrintWriter writer = new PrintWriter("'"+url+"'"+".html", "UTF-8");
            writer.print(text);
            writer.println("The second line");
            writer.close();*/
        } catch (IOException e) {
            // do something
        }
       // String sql="UPDATE record SET document = '"+text+"'WHERE url = '"+url+"'";
        //Statement sta = conn.createStatement();
        //sta.execute(sql);
    }
    public void Updatefile(String url) throws SQLException {

        String sql="UPDATE record SET `file` = '1' WHERE `record`.`url` ='"+url+"'";
        Statement sta = conn.createStatement();
        System.out.println("file updated");
        sta.execute(sql);
    }

    public int GetCount() throws SQLException {
        String sql="SELECT COUNT(*)  FROM `record` WHERE `file`=1;" ;

        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        int value=0;
        if (rs.next()) {
            value= Integer.parseInt(rs.getString(1));
           // System.out.println(value);
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
    public String GetURL(int n,int ind) throws SQLException {

        String sql="SELECT url FROM record where visted =0 and file=0 LIMIT "+n;
        Statement sta = conn.createStatement();
        String value="";

        ResultSet resultSet=sta.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        int count=0;
        while (resultSet.next()) {

          //  for (int i = ind-1; i < columnsNumber; i++)
            if(count==ind)
            {    value = resultSet.getString(1);
                int id=Integer.parseInt(Thread.currentThread().getName());
               // int c=Getid(value);
               // System.out.println("thread num"+ id+" is taking url num "+c+" title: "+ value);
                break;
            }
            count++;
        }
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


