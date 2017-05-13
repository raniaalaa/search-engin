package Project;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
public class PARSER {
    String query;
    boolean phrase;
    private DATABASE db = new DATABASE();
    private INDEXER ind=new INDEXER();
    private Set<Long> UnionDocs = new HashSet<Long>();
    ArrayList<Long> intersection = new ArrayList<Long>();
    ArrayList<Long> Diff = new ArrayList<Long>();
    PARSER() {

    }
    public boolean getphrase()
    {
    return phrase;
    }
    public ArrayList<Long> GetIntersection()
    {
        return intersection;
    }
    public ArrayList<Long> GetDiff()
    {
        return Diff;
    }
    public Set<Long> Union(String [] splited)
    {
        Set<Long> Docs = new HashSet<Long>();
        ResultSet resultSet;
        long id;
        for (int i = 0; i < splited.length; i++) {
            try {        
                resultSet = db.QueryProcessor(splited[i]);
                while (resultSet.next()) {
                    id=resultSet.getLong(1);
                    Docs.add(id);
                    //System.out.println("uuuuu  "+id);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PARSER.class.getName()).log(Level.SEVERE, null, ex);
            }
           }
        return Docs;
    }
    public String[] Parse(String query) throws SQLException, IOException 
    {
        intersection.clear();
               Diff.clear();
               UnionDocs.clear();
        query=query.toLowerCase();
        String[] splited = query.split("\\P{Alpha}+");
     /*   if (splited.length == 1) {
            if(query.endsWith("\"") && query.startsWith("\""))
            {
                query= query.replace("\""," ");
            }
            //String q = "SELECT url, title FROM record, word WHERE word.doc_id = record.id and word='" + query + "';";
            String q = "SELECT url FROM record, word WHERE word.doc_id = record.id and word='" + query + "';";

            Statement st = db.conn.createStatement();
            ResultSet resultset = st.executeQuery(q);
            ResultSetMetaData rsmd = resultset.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultset.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultset.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
            }
        }*/
        if (query.endsWith("\"")==false && query.startsWith("\"")==false)
        {
             phrase=false;
            String tmpinput="";
            String exp="";
            int tmpi=0;
            boolean stop=false;
            String ex="",q;
            for (int i=0;i< splited.length; i++)
            {
                if(!ind.stopwords.contains(splited[i]))
                {
                    tmpinput+=splited[i]+" ";
                    stop=true;
                    System.out.println("fff    "+splited[i]+"  "+splited[i]);

                }
                else
                {
                   ex+=splited[i]+" ";
                }
            }
            if(!stop)
            {
               q = "SELECT Doc_id FROM expressionscounts WHERE expression='" + ex + "'" ;
                               System.out.println("exxxxx    "+ex+"  "+q);
               Statement st = db.conn.createStatement();
               ResultSet resultSet = st.executeQuery(q);
               while (resultSet.next()) {
                 intersection.add(resultSet.getLong(1));
               }
                   return splited;
            }
            UnionDocs=Union(splited);
           long id;
           Iterator<Long> iterator = UnionDocs.iterator();
           boolean found=true;
           String content = "";
           while(iterator.hasNext()) {
        	   found=true;
        	   id=iterator.next();
                   if(id==1)
                       System.out.println(id);
                   File f=new File("C:\\Users\\user\\workspace\\Ph2\\html\\"+id+".html");
                   org.jsoup.nodes.Document doc=Jsoup.parse(f, "UTF-8");
                   content=content+" "+doc.text();
               //content = new Scanner(new File("C:\\Users\\mennna\\Documents\\NetBeansProjects\\Search\\"+id+".html")).useDelimiter("\\Z").next();
               content= content.toLowerCase();
               
               for (int i = 0; i < splited.length; i++) {
	               if(!content.contains(splited[i]))
	               {
	                   found=false;
	                   break;
	               }
               }    
               if(found)
               {
                   intersection.add(id);
                //   System.out.println("aaaaaaaaaa  "+id); 
                   iterator.remove();
               }
               else
                    Diff.add(id);
           }
          
         String q1 = "SELECT distinct ID FROM record, word WHERE word.doc_id = record.id and ( word.Stemming='" + porterStemmer.stemTerm(splited[0]) + "'";
                for (int i = 1; i < splited.length; i++) {
                    q1 += " or word.Stemming= '" + porterStemmer.stemTerm(splited[i])+ "'";
                }
                 q1 += " );";
                 Statement st=db.conn.createStatement();
                ResultSet resultSet1 = st.executeQuery(q1);
                while (resultSet1.next()) {
                        Long columnValue = resultSet1.getLong(1);
                        Diff.add(columnValue);
                }
                
        }
        else if(query.endsWith("\"")==true && query.startsWith("\"")==true)
        { 
            phrase=true;
            query= query.replace("\"","");
            String[] inputsplit=query.split("\\P{Alpha}+");
            String tmpinput="",ex="";
            String q;
            boolean stop=false;
            for (int i=0;i< inputsplit.length; i++)
            {
                if(!ind.stopwords.contains(inputsplit[i]))
                {
                    tmpinput+=inputsplit[i]+" ";
                    stop=true;
                                   //                System.out.println("fff    "+splited[i]+"  "+inputsplit[i]);

                }
                else
                {
                   ex+=inputsplit[i]+" ";
                }
            }
            if(!stop)
            {
               q = "SELECT Doc_id FROM expressionscounts WHERE expression='" + ex + "'" ;
                      //         System.out.println("exxxxx    "+ex+"  "+q);
               Statement st = db.conn.createStatement();
               ResultSet resultSet = st.executeQuery(q);
               while (resultSet.next()) {
                 intersection.add(resultSet.getLong(1));
               }
                   return splited;
            }
             String[] split=tmpinput.split("\\P{Alpha}+");
            q = "SELECT Doc_id FROM word WHERE word='" + split[1] + "'" ;
            //int result = Integer.parseInt(number);
            for (int i = 2; i < split.length; i++) {
                q += " AND EXISTS (SELECT Doc_id FROM word WHERE word='" + split[i] + "')";
            }
            Statement st = db.conn.createStatement();
            ResultSet resultSet = st.executeQuery(q);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            String[] files=new String[100000];
            String[] postion=null;
            int index=0;
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = resultSet.getString(i);
               //     System.out.print(columnValue + " " + rsmd.getColumnName(i));

                  files[index]=columnValue;
                  index++;

                }

                //System.out.println("");
            }


           // String secquery="SELECT distinct url, title FROM record, word WHERE word.doc_id = record.id and (";
            //String secquery="SELECT distinct doc_id FROM record, word WHERE word.doc_id = record.id and (";

            boolean firstenter=true;
            for(int j=0;j<index;j++)
            {
                 File f=new File("C:\\Users\\user\\workspace\\Ph2\\html\\"+files[j]+".html");
        org.jsoup.nodes.Document doc=Jsoup.parse(f, "UTF-8");
        String content="";
       content =content+" "+doc.text();
                //String content = new Scanner(new File(files[j]+".html")).useDelimiter("\\Z").next();
                content= content.toLowerCase();
                if(content.contains(query))
                {
                       intersection.add(Long.parseLong(files[j]));
              //      secquery+=" word.doc_id="+files[j]+ "";
                }
                
                //
            }
            /*secquery+=" )";
            //System.out.println(secquery);
            ResultSet resultSet1 = st.executeQuery(secquery);
            ResultSetMetaData rsmd1 = resultSet1.getMetaData();
            int columnsNumber1 = rsmd1.getColumnCount();
            while (resultSet1.next()) {
                for (int i = 1; i <= columnsNumber1; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet1.getString(i);
                   System.out.print(columnValue + " " + rsmd1.getColumnName(i));
                }
                //System.out.println("");

            }*/
        }
     return splited;
    }
}