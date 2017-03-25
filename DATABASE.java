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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import org.apache.commons.lang3.tuple.Pair;

//import org.apache.commons.lang3.Pair;

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
    public void Restart(List<String> arr) throws SQLException, MalformedURLException {
        String sql;
        Statement sta;
        sql="UPDATE `record` SET `Visted`=0,`file`=0";
        sta = conn.createStatement();
        sta.execute(sql);        
        return;
    }
    public String Normalize(String url) throws MalformedURLException
    {   url=url.replace("www.", "");
        URL u = new URL(url.toLowerCase());
        url = u.getNormalizedUrl();
        if(url.equals(""))
            return url;
        else
            url = "http://"+url;
        return url;
    }
    public synchronized boolean Check_Exist(String url) throws SQLException, MalformedURLException {
        if(url.equals(""))
            return true;
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
    public synchronized void Insert(String url,String text) throws SQLException, MalformedURLException {
        int iid=Integer.parseInt(Thread.currentThread().getName());
        boolean check=Check_Exist(url);
        if(check)
        { if(url.equals(""))
                return;
            url=Normalize(url);
            if(url.equals(""))
                return;
            String sql="INSERT INTO `record`(`ID`, `URL`, `document`,  `Visted`, `file`, `invalid`) VALUES (NULL,'"+url+"','"+text+"','0','0','0');";
            Statement sta = conn.createStatement();
            sta.execute(sql);
        }
    }

    public synchronized void UpdateVisted(String url) throws SQLException, MalformedURLException {
        String sql="UPDATE `record` SET `Visted`=1 WHERE URL ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }


    //////////

    public synchronized void UpdateDoc(String url,String text) throws SQLException {

        if(!text.equals("")) {
            try {
                long id = Getid(url);
                if(id!=0)
                {String filename = "html/"+id + ".html";
                File myfile = new File(filename);
                FileWriter writer = new FileWriter(myfile, true);
                writer.write(text);
                writer.close();
              System.out.println("thread update doc num "+id +" url=  "+url);}
            }
            catch (IOException e) {
            }

        }

    }
    public  synchronized void Updatefile(String url) throws SQLException, MalformedURLException {
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
        }
        return value;
    }

    public  synchronized long Getid(String url) throws SQLException {

        String sql="SELECT id FROM `record` WHERE url='"+url+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        long value=0;
        if (rs.next()) {
            value= Long.parseLong(rs.getString(1));

        }
        return value;
    }
    public boolean Getfile(String url) throws SQLException {

        String sql="SELECT file FROM `record` WHERE url='"+url+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        long value=0;
        if (rs.next()) {
            value= Long.parseLong(rs.getString(1));
            int id=Integer.parseInt(Thread.currentThread().getName());
        }
        if(value==1)
            return true;
        return false;
    }
    public void SetInvalid(String url) throws SQLException {
        String sql="UPDATE `record` SET `invalid`=1 WHERE URL ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }
    public synchronized String GetURL(int n,int ind) throws SQLException, MalformedURLException {

        String sql="SELECT url FROM record where visted =0 and invalid=0 LIMIT 1";
        Statement sta = conn.createStatement();
        String value="";

        ResultSet resultSet=sta.executeQuery(sql);

        if (resultSet.next()) {
            value = resultSet.getString(1);
        }

        return value;
    }
    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }
    //////////////////////////////functions of indexer/////////////////////////////////////////////////
    
    //Check exitance in word table
    public static Boolean CheckWordExistance(String Word) throws Exception
    {
        String queryCheck = "SELECT * from `Word` WHERE word = '"+Word+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());

    }
    public static Boolean CheckWordInDoc(String Word,long DocId) throws Exception
    {
    	long WID=GetWordID(Word); 
        String queryCheck = "SELECT * from `wordpositions` WHERE w_id = '"+WID+"' and doc_id = '"+DocId+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());
    }
    public static Boolean CheckExpressionInDoc(String Expression,long DocId) throws Exception
    {
    	long EID=GetWordID(Expression); 
        String queryCheck = "SELECT * from `expressionscounts` WHERE Expression_id = '"+EID+"' and Doc_id = '"+DocId+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());
    }
    //Check if the expression exists in expression table
    public static Boolean CheckExpressionExistance(String Expression) throws Exception
    {
        String queryCheck = "SELECT * from `Expressions` WHERE Expression = '"+Expression+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());

    }
    ///check existance in expressionscount
    public static Boolean CheckExpressionCountExistance(long EID) throws Exception
    {
        String queryCheck = "SELECT * from `expressionscount` WHERE Expression_id = '"+EID+"'";
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
    public static String GetPosition(String Word,long DOCID) throws Exception
    {
    	long WID=GetWordID(Word);
    	String queryCheck = "SELECT `position` from `wordpositions` WHERE w_id = '"+WID+"' and doc_id = '"+DOCID+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        String value="";
        if (resultSet.next()) {
            value= resultSet.getString(1);
        }
        return value;	
    }
    public static int GetImportance(String Word,long DOCID) throws Exception
    {
    	long WID=GetWordID(Word);
    	String queryCheck = "SELECT `importance` from `wordpositions` WHERE w_id = '"+WID+"' and doc_id = '"+DOCID+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        int value=1;
        if (resultSet.next())
        {
            value= Integer.parseInt(resultSet.getString(1));
            System.out.println(value);
        }
        return value;	
    }
    public static void InsWords(Map<Pair<String, Integer>, Pair<String, String>> words,long DocID) throws Exception
    {      
        PreparedStatement statement;
        Pair<String,Integer> Key;     //KeyPair.left->word       , //KeyPair.right->importance
        Pair<String,String> Value;    //ValuePair.left->position , //ValuePair.right->stemming
        long WID;
        for (Map.Entry<Pair<String,Integer>, Pair<String,String>> entry: words.entrySet())
        {
        	Key = entry.getKey();             
            Value = entry.getValue();
            if(CheckWordExistance(Key.getLeft()))  //the word is inserted before in the database
            {
            	 if(CheckWordInDoc(Key.getLeft(),DocID))//check if the word was in the document before updating
                 {
                 	 WID=GetWordID(Key.getLeft());
     	                ///////////////check if position changed///////////////
     	           	 if(!Value.getLeft().equals(GetPosition(Key.getLeft(),DocID)))
     	           	 {
     	           		 statement = conn.prepareStatement("UPDATE `wordpositions` SET `position`='"+Value.getLeft()+"' WHERE w_id='"+WID+"' and doc_id ='"+DocID+"'");
     	           	     statement.execute();
     	           	 }	
     	           	 ///////////////check if importance changed///////////////
     	           	 if(Key.getRight()!=GetImportance(Key.getLeft(),DocID))
     	           	 {
     	           		 statement = conn.prepareStatement("UPDATE `wordpositions` SET `importance`='"+Key.getRight()+"' WHERE w_id='"+WID+"' and doc_id ='"+DocID+"'");
     	           	     statement.execute();
     	           	 }	
     	           	 
                 }
            	 else    //the word isn't in the document
                 {
     	           	 if(!Value.getLeft().equals(""))
     	                {
     		     	        WID=GetWordID((String)Key.getLeft());
     		     	        statement = conn.prepareStatement("insert into `wordpositions` (`w_id`,`doc_id`,`position`,`importance`) values("+WID+","+DocID+",'"+Value.getLeft()+"','"+(int)Key.getRight()+"')");
     		     	        statement.execute();
     	                }
                 }
            	
            } //the word isn't inserted before in the database
            else
            {
            	
		            	 statement = conn.prepareStatement("insert into `Word` (`word`,`Wid`,`Stemming`) values('"+(String)Key.getLeft()+"',NULL,'"+(String)Value.getRight()+"')");
		            	 WID=GetWordID((String)Key.getLeft());
  		     	         statement = conn.prepareStatement("insert into `wordpositions` (`w_id`,`doc_id`,`position`,`importance`) values("+WID+","+DocID+",'"+Value.getLeft()+"','"+(int)Key.getRight()+"')");
  		     	         statement.execute();
            	
            }
       
       }	
 }
    public static void InsExpressions(Map<String, String> expressionsMap) throws Exception
    {
    	PreparedStatement statement;
    	long EID;
    	for (Entry<String, String> entry: expressionsMap.entrySet())
	    { 
    		String Key=entry.getKey();
    		String Value=entry.getValue();
    		if(CheckExpressionExistance(Key))
    		{
    			EID=GetExpressionID(Key);
    			if(CheckExpressionCountExistance(EID)) //expression is in the table of expressionscount
        		{
        			statement = conn.prepareStatement("UPDATE `expressionscounts` SET `E_Details`='"+Value+"' WHERE Expression_id='"+EID+"'");
              	    statement.execute();
        		}	
        		else   //expression is not in the table of expressionscount
        		{

               	 statement = conn.prepareStatement("insert into `expressionscounts` (`Expression_id`,`E_Details`) values('"+EID+"',NULL,'"+Value+"')");
               	 statement.execute();
        		}	
    		}
    		else
    		{
    			//insert in table expressions
    			statement = conn.prepareStatement("insert into `expressions` (`Expression`,`E_ID`) values('"+Key+"',NULL)");
    	        statement.execute();
    			EID=GetExpressionID(Key);
    			//insert in table expressionscount
    			statement = conn.prepareStatement("insert into `expressionscounts` (`Expression_id`,`E_Details`) values('"+EID+"',NULL,'"+Value+"')");
              	 statement.execute();
    		}
    		
	    }
    	
    }
    
   
}