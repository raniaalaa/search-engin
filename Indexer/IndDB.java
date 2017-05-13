//package Project;
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
import java.util.*;
import org.apache.commons.lang3.tuple.Pair;

//import org.apache.commons.lang3.Pair;



import java.io.FileWriter;
import java.net.MalformedURLException;


public class IndDB {
    public static Connection conn = null;
    public IndDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/s";
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
    
  
   
    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }
    //////////////////////////////functions of indexer/////////////////////////////////////////////////
   
    public Long GetNotIndexed() throws SQLException {

        String sql="SELECT ID FROM record where indexed=0 and file=1 LIMIT 1";
      //  String sql="SELECT ID FROM record where indexed=0 and file=1 and id >3500 LIMIT 1";

        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        long value =0;
        if (rs.next()) {
            value= Long.parseLong(rs.getString(1));
        }
        Statement statement1=conn.createStatement();
    	statement1.executeUpdate("update record set indexed = 1 where `ID` = '"+value+"'");
             
        return value;
    }
    
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
        String queryCheck = "SELECT * from `expressionscounts` WHERE expression = '"+Expression+"' and Doc_id = '"+DocId+"'";
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
    public static Boolean CheckExpressionCountExistance(String Expression) throws Exception
    {
        String queryCheck = "SELECT * from `expressionscounts` WHERE Expression = '"+Expression+"'";
        PreparedStatement statement = conn.prepareStatement(queryCheck);
        ResultSet resultSet = statement.executeQuery();
        return (resultSet.next());

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
   
    public static void DeleteNotUpdated(long DOCID) throws Exception
    {
    	Statement statement1=conn.createStatement();
    	statement1.executeUpdate("DELETE FROM `word` WHERE doc_id = '"+DOCID+"' and updated=0");
        Statement statement2=conn.createStatement();
    	//statement2.executeUpdate("DELETE FROM `expressionscounts` WHERE doc_id = '"+DOCID+"' and updated=0");
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
    	Statement statement1=conn.createStatement();
    	statement1.executeUpdate("update word set updated = 0 where `doc_id` = '"+doc_id+"'");
        //////////////////////////
        Statement statement2=conn.createStatement();
    	//statement2.executeUpdate("update expressionscounts set updated = 0 where `doc_id` = '"+doc_id+"'");
        
    }
    public static void InsTitle(String title,long doc_id) throws SQLException
    {       String query="UPDATE `record` SET `Title`="+"(?)"+" where ID='"+doc_id+"'";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, title);
        ps.executeUpdate();
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
            if(Importants[3].contains(word))
                importance+= 30;
    		 for (String title_element: Importants[0].split("\\P{Alpha}+")) ///If title -> 4
    			{
    				if(word.equals(title_element.toLowerCase()))
    				{
    					importance+= 20;
    					
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
     	           	     statement.executeUpdate("UPDATE `word` SET `importance`='"+importance+"',`updated`=1,`TF`='"+tf_importance+"' WHERE word='"+Key.getLeft()+"' and doc_id ='"+DocID+"'");
                 }
            	 else    //the word wasn't in the document
                 {
     	           	 if(!Value.getLeft().equals(""))
     	                {
     	           		    double TF=(double)Value.getRight().getRight()/docSize;
     	           		    double tf_importance=TF*0.7+importance*0.3;
     		     	        statement.executeUpdate("insert into `word` (`word`,`doc_id`,`stemming`,`importance`,`updated`,`TF`) values('"+(String)Key.getLeft()+"','"+DocID+"','"+Value.getRight().getLeft()+"','"+importance+"',1,'"+TF+"')");
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
    public static void InsExpressions(Map<String, Pair<Integer,Integer>> expressionsMap,long doc_id,String [] Importants,long docSize) throws Exception
    {
    	 Statement statement=conn.createStatement();
    	for (Entry<String, Pair<Integer,Integer>> entry: expressionsMap.entrySet())
	    { 
    		String Key=entry.getKey();////////expression
    		Pair<Integer,Integer> Value=entry.getValue();//////////Value.getLeft()->importance   ////Value.getRight()->count
                int importance=1;
                               if(Importants[0].contains(Key))
    				{
    					importance+= 3;	
    				}
                                if(Importants[1].contains(Key))
                                {
                                    importance+= 2;
                                }
    			       if(Importants[2].contains(Key))
		                {
                                    importance+= 1;
                                }
    		               double TF=(double)Value.getRight()/docSize;
     	           	       double tf_importance=TF*0.7+importance*0.3;
    		 if(CheckExpressionInDoc(Key,doc_id))//check if the expression was in the document before updating
                 {
       	           	       statement.executeUpdate("UPDATE `expressionscounts` SET `tf_importance`='"+tf_importance+"',`updated`=1 where Doc_ID ="+doc_id);
                 }
            	 else    //the word wasn't in the document
                 {
     	           	 if(!Value.getLeft().equals(""))
     	                {
     		     	        statement.executeUpdate("insert into `expressionscounts` (`Expression`,`Doc_ID`,`tf_importance`,`updated`) values('"+Key+"',"+doc_id+","+tf_importance+",1)");
     	                }
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
    public static ResultSet QueryProcessor(String w) throws SQLException 
    {
    	PreparedStatement st = conn.prepareStatement("SELECT Doc_id FROM record, word WHERE word.doc_id = record.id and word='" + w + "'");
        ResultSet resultSet = st.executeQuery();	
        return resultSet;
    }
      public static String getDocTitle(long doc_id) throws SQLException
    {
        String sql="SELECT Title FROM record where ID='"+doc_id+"'";
        Statement sta = conn.createStatement();
        String value="";

        ResultSet resultSet=sta.executeQuery(sql);

        if (resultSet.next()) {
            value = resultSet.getString(1);
        }

        return value;
    }
       public static String getUrl(long doc_id) throws SQLException
    {
        String sql="SELECT url FROM record where ID='"+doc_id+"'";
        Statement sta = conn.createStatement();
        String value="";

        ResultSet resultSet=sta.executeQuery(sql);

        if (resultSet.next()) {
            value = resultSet.getString(1);
        }

        return value;
    }
}

