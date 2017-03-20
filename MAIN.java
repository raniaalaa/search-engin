import java.io.File;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class MAIN {
	public static void main(String[] args) throws Exception {
		
	    Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the number of threads: ");
        int size = reader.nextInt();
        SPIDER sp=new SPIDER (size);
        Thread myThreads[] = new Thread[size];
        for (int j = 0; j < size; j++) {
            myThreads[j] = new Thread(sp);
            myThreads[j].setName(Integer.toString(j));
            myThreads[j].start();
        }

        reader.close();	
    INDEXER I=new INDEXER();
    File input = new File("C:/Users/mennna/Documents/Indexer1");  // pages in folder "html" // 
    File[] st = input.listFiles();
    System.out.println(st.length);
    String [] Importance={"",""};
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
         I.Run(Filer.GetText(),3,Importance[0],Importance[1]);
         System.out.println(Filer.GetText());
      }
    }

    
     }
}
