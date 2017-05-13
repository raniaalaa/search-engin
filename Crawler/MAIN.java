import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;



public class MAIN  {
  
    public static void main(String[] args) {
    
    	DATABASE db=new DATABASE();
	 	Thread rank = new Thread (new UpdateRank());
	 	rank.setName ("Ranking");
	 	rank.start();
	 	
        ////////////////////////////////////////////
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter number of threads between 1 and 20: ");
        int size=0; ;
        while(true)
        {
            size=reader.nextInt();
            if(size>20||size<1)
                System.out.println("Enter number of threads between 1 and 20: ");
            else
                break;
        }
        CRAWLER crawler;

        try {
            int counter=db.GetCount();
            if (counter<5000&&counter!=0)
            	crawler = new CRAWLER (size,false);
            else 
            	crawler = new CRAWLER (size,true);
            Thread spiderThreads[] = new Thread[size];
            for (int j = 0; j < size; j++) {
                spiderThreads[j] = new Thread(crawler);
                spiderThreads[j].setName(Integer.toString(j));
                spiderThreads[j].start();
            }
            
            
            for (int j = 0; j < size; j++)
            {

                spiderThreads[j].join();

            }
        } catch (SQLException |IOException | InterruptedException  e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

  

    }
}
