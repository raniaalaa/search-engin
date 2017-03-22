///package project;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class MAIN {

    public static void main(String[] args) throws Exception {
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
        DATABASE db=new DATABASE();
    	db.Restaet();
    	////////////////////////////////////////////
      Scanner reader = new Scanner(System.in);  // Reading from System.in
       System.out.println("Enter the number of threads between 1 and 10: ");
        int size=0; ;
        while(true)
        {
        	size=reader.nextInt();
        	if(size>20||size<1)
                System.out.println("Enter the number of threads between 1 and 10: ");
        	else
        		break;
        }
        SPIDER sp=new SPIDER (size);

        Thread myThreads[] = new Thread[size];
        for (int j = 0; j < size; j++) {
            myThreads[j] = new Thread(sp);
            myThreads[j].setName(Integer.toString(j));
            myThreads[j].start();
        }
        reader.close(); 
        for (int j = 0; j < size; j++)
            myThreads[j].join();

        INDEXER I=new INDEXER();
        File input = new File("C:/Users/user/workspace/Spider");  // pages in folder "html" // 
        File[] st = input.listFiles();
        System.out.println(st.length);
        String [] Importance={"","",""};
        Filer filer=new Filer();
        for (int i = 0; i < st.length; i++) 
        {
            if(st[i].isFile())
            {
             if(!st[i].getName().contains(".html"))
            	continue;
             String FileName=st[i].getName().replace(".html","");
             long doc_id=Long.parseLong(FileName);
             Importance = filer.Dealing_Files(st[i]);
             ///////////////////////////edited///////////////////////////////////
             I.Run(Filer.GetText(),doc_id,Importance);
            System.out.println(Filer.GetText());
          }
        }        
        }
}
