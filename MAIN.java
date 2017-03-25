//main
///package project;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MAIN extends TimerTask {

    public void run() {
       DATABASE db=new DATABASE();
        ////////////////////////////////////////////
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the number of threads between 1 and 20: ");
        int size=0; ;

        while(true)
        {
            size=reader.nextInt();
            if(size>20||size<1)
                System.out.println("Enter the number of threads between 1 and 20: ");
            else
                break;
        }
        CRAWLER crawler;

        try {
            int counter=db.GetCount();
            if (counter<5000)
            crawler = new CRAWLER (size,false);
            else crawler = new CRAWLER (size,true);
            Thread spiderThreads[] = new Thread[size];
            for (int j = 0; j < size; j++) {
                spiderThreads[j] = new Thread(crawler);
                spiderThreads[j].setName(Integer.toString(j));
                spiderThreads[j].start();
            }
            reader.close();
            for (int j = 0; j < size; j++)
            {

                spiderThreads[j].join();

            }
        } catch (SQLException |IOException |InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        INDEXER I=new INDEXER();
        File input = new File("E://c,p3//search-engin-master//html");  // pages in folder "html" //
        File[] st = input.listFiles();
        System.out.println(st.length);
        String [] Importance={"","",""};
        FILER filer=new FILER();
        for (int i = 0; i < st.length; i++)
        {
            if(st[i].isFile())
            {
                if(!st[i].getName().contains(".html"))
                    continue;
                String FileName=st[i].getName().replace(".html","");
                long doc_id=Long.parseLong(FileName);
                try {
                    Importance = filer.Dealing_Files(st[i]);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                ///////////////////////////edited///////////////////////////////////
                try {
                    I.Run(FILER.GetText(),doc_id,Importance);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(FILER.GetText());
            }
        }

    }

}
class MAINAPP
{

    /*
     DELETE FROM `expressionspositions` WHERE 1;
     DELETE FROM `expressions` WHERE 1;
     DELETE FROM `wordpositions` WHERE 1;
     DELETE FROM `word` WHERE 1;
     DELETE FROM `record` WHERE 1;
     DELETE FROM `restrictedurls` WHERE 1;
     ALTER TABLE `record` AUTO_INCREMENT = 1;
     INSERT INTO `searchengine`.`record` (`ID`, `URL`, `document`, `Visted`, `file`) VALUES (NULL, 'https://en.wikipedia.org/wiki/Main_Page', '', '0', '0'), (NULL, 'http://stackoverflow.com/', '', '0', '0'), (NULL, 'http://nature.com/index.html', '', '0', '0'), (NULL, 'https://w3.org/', '', '0', '0'), (NULL, 'http://dmoztools.net/', '', '0', '0'), (NULL, 'http://unicef.org/', '', '0', '0') ,(NULL, 'http://ibm.com/us-en/', '', '0', '0');
     */
    /////////////////////////////////////////////
    public static void main(String[] args) {
        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        date.set(
                Calendar.DAY_OF_WEEK,
                Calendar.FRIDAY
        );
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        //date.getTime()
        timer.schedule(
                new MAIN(),date.getTime()
                ,
                1000 * 60 * 60 * 24 * 2 );


    }//Main method ends
}

