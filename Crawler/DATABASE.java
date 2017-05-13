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
        sql="ALTER TABLE record AUTO_INCREMENT = 1";
        sta = conn.createStatement();
        sta.execute(sql);
        return;
    }
    public static String Normalize(String url) throws MalformedURLException
    {   url=url.replace("www.", "");
        URL u = new URL(url.toLowerCase());
        url = u.getNormalizedUrl();
        if(url.equals(""))
            return url;
        else
            url = "http://"+url;
        return url;
    }
    public synchronized static boolean Check_Exist(String url) throws SQLException, MalformedURLException {
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

            String sql="INSERT INTO `record`(`ID`, `URL`,  `Visted`, `file`, `invalid` ,`title`, `rank`,`updated`,`indexed`,`To_Rank`,`Diff`) VALUES (NULL,'"+url+"','0','0','0','Doc Title',"+NewRank+",'0','0','0','0');";
            Statement sta = conn.createStatement();
            sta.execute(sql);
        }
    }

    public synchronized static void update_rank(String urlOutLink,String urlInlink,int OutLinks) throws SQLException, MalformedURLException {

        if(urlInlink.equals(""))
            return;
        urlInlink=Normalize(urlInlink);
        if(urlInlink.equals(""))
            return;
        double Diff=((GetRank(urlOutLink))/OutLinks);
        double NewRank=(GetRank(urlInlink)+(0.85*Diff));
        String q="Select visted from `record` where `url`= '"+urlInlink+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(q);
        int r=0;
        double d=0;
        if (rs.next()) {
            if(rs.getBoolean(1))
            {
                r=1;
                d=Diff;
            }
        }

        //  System.out.println(urlOutLink+" : "+OutLinks+"  "+GetRank(urlInlink)+" "+urlInlink+"  "+NewRank);
        String sql="UPDATE `record` SET `rank`= "+NewRank+",`To_Rank`="+r+" , `Diff`="+d+" WHERE URL ='"+urlInlink+"'";
        sta = conn.createStatement();
        sta.execute(sql);
    }
    public synchronized static void ConRanking(String urlInlink,double Diff,int OutLinks) throws SQLException, MalformedURLException {

        double NewRank=(GetRank(urlInlink)+(0.85*(Diff/OutLinks)));
        //  System.out.println(urlOutLink+" : "+OutLinks+"  "+GetRank(urlInlink)+" "+urlInlink+"  "+NewRank);
        String sql="UPDATE `record` SET `rank`= "+NewRank+" WHERE URL ='"+urlInlink+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }
    public  synchronized static double GetRank(String url) throws SQLException {

        String sql="SELECT rank FROM `record` WHERE url='"+url+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        double value=0;
        if (rs.next()) {
            value= Double.parseDouble(rs.getString(1));

        }
        return value;
    }
    public  synchronized static ResultSet Get_TORank() throws SQLException {

        String sql="SELECT ID,URL,Diff FROM record where To_Rank=1 and visted=1 LIMIT 1";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        long value=0;
        if (rs.next()) {
            value= rs.getLong(1);
            sql="UPDATE `record` SET `To_Rank`=0 , Diff=0 WHERE ID ='"+value+"'";
            sta = conn.createStatement();
            sta.execute(sql);
        }
        return rs;
    }
    public synchronized void UpdateVisted(String url) throws SQLException, MalformedURLException {
        String sql="UPDATE `record` SET `Visted`=1 WHERE URL ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
    }
    public synchronized void ResetUrl(String url) throws SQLException, MalformedURLException {
        String sql="UPDATE `record` SET `Visted`=0 WHERE URL ='"+url+"'";
        Statement sta = conn.createStatement();
        sta.execute(sql);
        sql="UPDATE `record` SET `file`=0 WHERE URL ='"+url+"'";
        sta.execute(sql);
        sql="UPDATE `record` SET `updated`=1 WHERE URL ='"+url+"'";
        sta.execute(sql);
        sql="UPDATE `record` SET `indexed`=1 WHERE URL ='"+url+"'";
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
    public  synchronized boolean GetUpdated(String url) throws SQLException {

        String sql="SELECT updated FROM `record` WHERE url='"+url+"'";
        Statement sta = conn.createStatement();
        ResultSet rs = sta.executeQuery(sql);
        long value=0;
        if (rs.next()) {
            value= Long.parseLong(rs.getString(1));
            if (value==1)
                return true;
        }
        return false;
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















}

