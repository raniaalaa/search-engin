import org.jsoup.Jsoup;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.io.File;
public class PARSER {
    String query;
    private DATABASE db = new DATABASE();
    private INDEXER ind=new INDEXER();
    PARSER() {

    }


    public void Parse() throws SQLException, IOException {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        query=reader.nextLine();
        query=query.toLowerCase();
        String[] splited = query.split("\\P{Alpha}+");
        if (splited.length == 1) {
            if(query.endsWith("\"") && query.startsWith("\""))
            {
                query= query.replace("\""," ");
            }
            String q = "SELECT url, title FROM record, word WHERE word.doc_id = record.id and word='" + query + "';";
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
        }
        else if (splited.length > 1 && query.endsWith("\"")==false && query.startsWith("\"")==false)
        {

            String tmpinput="";
            String exp="";
            int tmpi=0;
            boolean setex=false;
            for (int i=0;i< splited.length; i++)
            {
                if(!ind.stopwords.contains(splited[i]))
                {
                    tmpinput+=splited[i]+" ";
                }
                else
                { if(!setex && i-tmpi==1)
                {   tmpi=i;
                    exp+=splited[i];
                    setex=true;
                }
                }
            }
            String[] inputsplit=tmpinput.split(" ");
            String q;
            if(inputsplit.length >0) {
                 q= "SELECT url, title FROM record, word WHERE word.doc_id = record.id and word='" + inputsplit[0] + "'";

                for (int i = 1; i < splited.length; i++) {
                    q += " AND EXISTS (SELECT url, title FROM record, word WHERE word.doc_id = record.id and word='" + inputsplit[i] + "')";
                }
                System.out.println(q);
                Statement st = db.conn.createStatement();
                ResultSet resultSet = st.executeQuery(q);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = resultSet.getString(i);
                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
                    System.out.println("");

                }
            }

        }
        else if(query.endsWith("\"")==true && query.startsWith("\"")==true)
        {
           query= query.replace("\"","");
            String[] inputsplit=query.split("\\P{Alpha}+");
            String tmpinput="";
            for (int i=1;i< inputsplit.length; i++)
            {
                if(!ind.stopwords.contains(splited[i]))
                {
                    tmpinput+=inputsplit[i]+" ";
                }
            }
            String q = "SELECT Doc_id FROM word WHERE word='" + inputsplit[1] + "'" ;
            //int result = Integer.parseInt(number);
            for (int i = 2; i < inputsplit.length; i++) {
                q += " AND EXISTS (SELECT Doc_id FROM word WHERE word='" + inputsplit[i] + "')";
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
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));

                  files[index]=columnValue;
                  index++;

                }

                System.out.println("");
            }


            String secquery="SELECT distinct url, title FROM record, word WHERE word.doc_id = record.id and (";
            boolean firstenter=true;
            for(int j=0;j<index;j++)
            {
                String content = new Scanner(new File(files[j]+".html")).useDelimiter("\\Z").next();
                content= content.toLowerCase();
                if(content.contains(query)&& firstenter==true)
                {
                    secquery+=" word.doc_id="+files[j]+ "";
                    firstenter=false;
                }
                else if(content.contains(query)&& firstenter==false)
                {
                    secquery+=" or word.doc_id="+files[j]+"";
                }

                //
            }
            secquery+=" )";
            System.out.println(secquery);
            ResultSet resultSet1 = st.executeQuery(secquery);
            ResultSetMetaData rsmd1 = resultSet1.getMetaData();
            int columnsNumber1 = rsmd1.getColumnCount();
            while (resultSet1.next()) {
                for (int i = 1; i <= columnsNumber1; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet1.getString(i);
                   System.out.print(columnValue + " " + rsmd1.getColumnName(i));
                }
                System.out.println("");

            }
        }
    }
}