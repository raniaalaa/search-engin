import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

//import org.apache.commons.lang3.Pair;



import java.io.FileWriter;
import java.net.MalformedURLException;

import ch.sentric.URL;

public class DATABASE {
    public static Connection conn = null;
    public DATABASE() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/searchengineph2";
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
    public synchronized void Insert(String Src,String url,int OutLinks,String text) throws SQLException, MalformedURLException {
        //  int iid=Integer.parseInt(Thread.currentThread().getName());

      	boolean check=Check_Exist(url);
          if(check)
          { 
          	if(url.equals(""))
                  return;
              url=Normalize(url);
              if(url.equals(""))
                  return;
              double NewRank=0.15;
              if(!Src.equals(""))
              	NewRank+=((0.85*GetRank(Src))/OutLinks);

              String sql="INSERT INTO `record`(`ID`, `URL`, `document`,  `Visted`, `file`, `invalid` , `rank`) VALUES (NULL,'"+url+"','"+text+"','0','0','0',"+NewRank+");";
              Statement sta = conn.createStatement();
              sta.execute(sql);
          }
      }
    
    public synchronized void update_rank(String urlOutLink,String urlInlink,int OutLinks) throws SQLException, MalformedURLException {
        
        if(urlInlink.equals(""))
                return;
        urlInlink=Normalize(urlInlink);
            if(urlInlink.equals(""))
                return;
            double NewRank=(GetRank(urlInlink)+((0.85*GetRank(urlOutLink))/OutLinks));
         //  System.out.println(urlOutLink+" : "+OutLinks+"  "+GetRank(urlInlink)+" "+urlInlink+"  "+NewRank);
       	 String sql="UPDATE `record` SET `rank`= "+NewRank+" WHERE URL ='"+urlInlink+"'";
            Statement sta = conn.createStatement();
            sta.execute(sql);
  }
  public  synchronized double GetRank(String url) throws SQLException {

      String sql="SELECT rank FROM `record` WHERE url='"+url+"'";
      Statement sta = conn.createStatement();
      ResultSet rs = sta.executeQuery(sql);
      double value=0;
      if (rs.next()) {
          value= Double.parseDouble(rs.getString(1));

      }
      return value;
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
    	//long WID=GetWordID(Word); 
        String queryCheck = "SELECT * from `word` WHERE word = '"+Word+"' and doc_id = '"+DocId+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());
    }
    public static Boolean CheckExpressionInDoc(String Expression,long DocId) throws Exception
    {
    	long EID=GetExpressionID(Expression); 
        String queryCheck = "SELECT * from `expressionscounts` WHERE Expression_id = '"+EID+"' and Doc_id = '"+DocId+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());
    }
    //Check if the expression exists in expression table
   /* public static Boolean CheckExpressionExistance(String Expression) throws Exception
    {
        String queryCheck = "SELECT * from `Expressions` WHERE Expression = '"+Expression+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());

    }*/
    ///check existance in expressionscount
    public static Boolean CheckExpressionCountExistance(String Expression) throws Exception
    {
        String queryCheck = "SELECT * from `expressionscounts` WHERE Expression = '"+Expression+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());

    }
   /* public static long GetWordID(String Word) throws Exception
    {
        String queryCheck = "SELECT Wid from `Word` WHERE word = '"+Word+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet result = statement.executeQuery();

        if(result.next())
            return (result.getLong("Wid"));
        else
            return -1;
    }*/
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
    	//long WID=GetWordID(Word);
    	String queryCheck = "SELECT `positions` from `word` WHERE word = '"+Word+"' and doc_id = '"+DOCID+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        String value="";
        if (resultSet.next()) {
            value= resultSet.getString(1);
        }
        return value;	
    }
    public static void DeleteNotUpdated(long DOCID) throws Exception
    {
    	Statement statement=conn.createStatement();
    	statement.executeUpdate("DELETE FROM `word` WHERE doc_id = '"+DOCID+"' and updated=0");
    }
    public static int GetImportance(String Word,long DOCID) throws Exception
    {
    	//long WID=GetWordID(Word);
    	String queryCheck = "SELECT `importance` from `word` WHERE word = '"+Word+"' and doc_id = '"+DOCID+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        int value=1;
        if (resultSet.next())
        {
            value= Integer.parseInt(resultSet.getString(1));
        }
        return value;	
    }
    public static int wordsCount() throws SQLException
    {
      String queryCheck = "SELECT COUNT(*) FROM word";
      PreparedStatement statement = conn.prepareStatement(queryCheck);
      ResultSet resultSet = statement.executeQuery();
      int value=1;
      if (resultSet.next())
      {
          value= Integer.parseInt(resultSet.getString(1));
      }
      return value;	
    }
    public static double getTF(String Word,long doc_id) throws SQLException
    {
      String queryCheck = "SELECT `TF` FROM `word` where word='"+Word+"' and doc_id='"+doc_id+"'";
      PreparedStatement statement = conn.prepareStatement(queryCheck);
      ResultSet resultSet = statement.executeQuery();
      double value=1;
      if (resultSet.next())
      {
          value= Double.parseDouble(resultSet.getString(1));
      }
      return value;	
    }
    public static void rstUpdated(long doc_id) throws SQLException
    {
    	Statement statement=conn.createStatement();
    	statement.executeUpdate("update word set updated = 0 where `doc_id` = '"+doc_id+"'");
    }
    public static void InsTitle(String title,long doc_id) throws SQLException
    {
    	Statement statement=conn.createStatement();
    	statement.executeUpdate("UPDATE `record` SET `Title`='"+title+"' where ID='"+doc_id+"'");
    	
    }
    public static void InsWords(Map<Pair<String,Integer>, Pair<String,Pair<String,Integer>>> words,long DocID,long docSize,String [] Importants) throws Exception
    { 
        Statement statement=conn.createStatement();
        Pair<String,Integer> Key;     //Key.getLeft()->word , //Key.getRight()->importance 
        Pair<String,Pair<String,Integer>> Value;    //Value.getLeft()->position , //Value.getRight().getLeft()->stemming //Value.getRight().getRight()->wordCount
        for (Map.Entry<Pair<String,Integer>, Pair<String,Pair<String,Integer>>> entry: words.entrySet())
        {
        	Key = entry.getKey();             
            Value = entry.getValue();
            String word=Key.getLeft();
            int importance=1;
    		// int wordCount;
    		 for (String title_element: Importants[0].split("\\P{Alpha}+")) ///If title -> 4
    			{
    				if(word.equals(title_element.toLowerCase()))
    				{
    					importance+= 3;
    					
    				}
    			}
    			for (String img_element: Importants[2].split("\\P{Alpha}+"))  ///If image -> 3
    			{
    				if(word.equals(img_element.toLowerCase()))
    				{
    					importance+= 2;
    				}
    			}
    			for (String header_element: Importants[1].split("\\P{Alpha}+")) ///If header -> 2
    			{
    				if(word.equals(header_element.toLowerCase()))
    				{
    					importance+= 1;
    				}
    			}
            	 if(CheckWordInDoc(Key.getLeft(),DocID))//check if the word was in the document before updating
                 {
     	           		 double TF=(double)Value.getRight().getRight()/docSize;
     	           		 double tf_importance=TF*0.7+importance*0.3;
     	           	     statement.executeUpdate("UPDATE `word` SET `positions`='"+Value.getLeft()+"',`importance`='"+importance+"',`updated`=1,`TF`='"+tf_importance+"' WHERE word='"+Key.getLeft()+"' and doc_id ='"+DocID+"'");
                 }
            	 else    //the word wasn't in the document
                 {
     	           	 if(!Value.getLeft().equals(""))
     	                {
     	           		    double TF=(double)Value.getRight().getRight()/docSize;
     	           		    double tf_importance=TF*0.7+importance*0.3;
     		     	        statement.executeUpdate("insert into `word` (`word`,`doc_id`,`positions`,`stemming`,`importance`,`updated`,`TF`) values('"+(String)Key.getLeft()+"','"+DocID+"','"+Value.getLeft()+"','"+Value.getRight()+"','"+importance+"',1,'"+TF+"')");
     	                }
                 }
            	 
       }	
 }
    public static String[] getDocs(String Word) throws SQLException
    {
    	List<String> l=new ArrayList();
    	Statement stmt = conn.createStatement();
    	ResultSet rs = stmt.executeQuery("select Doc_id from word where `word`='"+Word+"'");
    	while (rs.next()) {
    		System.out.println("Doc id");
    	    System.out.println(rs.getString(1));
    	    l.add(rs.getString(1));
    	}
    	 String[] Docs = (String[]) l.toArray(new String[l.size()]);
    	return Docs;
    }  
    public static void InsExpressions(Map<String, String> expressionsMap) throws Exception
    {
    	PreparedStatement statement;
    	for (Entry<String, String> entry: expressionsMap.entrySet())
	    { 
    		String Key=entry.getKey();
    		String Value=entry.getValue();
    			if(CheckExpressionCountExistance(Key)) //expression is in the table of expressionscount
        		{
        			statement = conn.prepareStatement("UPDATE `expressionscounts` SET `E_Details`='"+Value+"' WHERE Expression='"+Key+"'");
              	    statement.execute();
        		}	
        		else   //expression is not in the table of expressionscount
        		{

               	 statement = conn.prepareStatement("insert into `expressionscounts` (`Expression`,`E_Details`) values('"+Key+"','"+Value+"')");
               	 statement.execute();
        		}	
	    }
    	
    }
    public static int getwordDocsCount(String w) throws SQLException
    {
    	PreparedStatement statement = conn.prepareStatement("select count(distinct Doc_id) from word where word='"+w+"'");
        ResultSet resultSet = statement.executeQuery();
        int value=1;
        if (resultSet.next())
        {
            value= Integer.parseInt(resultSet.getString(1));
        }
        return value;	
    }
    public static int getTotalNumberOfDocs() throws SQLException
    {
    	 PreparedStatement statement = conn.prepareStatement("select count(distinct Doc_id) from word");
         ResultSet resultSet = statement.executeQuery();
         int value=1;
         if (resultSet.next())
         {
             value= Integer.parseInt(resultSet.getString(1));
         }
         return value;	
    } 
}