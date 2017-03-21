///package project;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class MAIN {

    public static void main(String[] args) throws Exception {
/*
 * DELETE FROM `expressions` WHERE 1;
 * DELETE FROM `expressionspositions` WHERE 1;
 * DELETE FROM `record` WHERE 1;
 * DELETE FROM `word` WHERE 1;
 * ALTER TABLE `record` AUTO_INCREMENT = 1;
 * INSERT INTO `record`(`ID`, `URL`, `document`, `Visted`, `file`) VALUES ('NULL','http://www.quackit.com/html','NULL','0','0');
 */
      Scanner reader = new Scanner(System.in);  // Reading from System.in
       System.out.println("Enter the number of threads between 1 and 10: ");
        int size=0; ;
        while(true)
        {
        	size=reader.nextInt();
        	if(size>10||size<1)
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
        File input = new File("C:/Users/mennna/Documents/Indexer1");  // pages in folder "html" // 
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
             int doc_id=Integer.parseInt(FileName);
             Importance = filer.Dealing_Files(st[i]);
             ///////////////////////////edited///////////////////////////////////
             I.Run(Filer.GetText(),doc_id,Importance);
            // System.out.println(Filer.GetText());
          }
        }        
        }
}
