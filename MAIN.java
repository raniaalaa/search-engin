///package project;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class MAIN {

    public static void main(String[] args) throws SQLException, IOException {

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

    }

}
