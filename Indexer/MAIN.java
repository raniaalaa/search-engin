
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;



 public class MAIN
{
	 
    public static void main(String[] args) throws SQLException {

    	IndDB db=new IndDB();
        INDEXER I=new INDEXER();
        File input;
        String [] Importance={"","","",""};
        FILER filer=new FILER();
        String FileName;
        long doc_id=0;
        try {
            doc_id = db.GetNotIndexed();
        } catch (SQLException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        while(doc_id!=0)
        {

            FileName=Long.toString(doc_id);//.replace(".html","");
            input = new File("C:/Users/user/workspace/Ph2/html/"+FileName+".html");
            System.out.println("ID "+doc_id+" "+input.getName());

            try {
               Importance = filer.Dealing_Files(input);
               Importance[3]=db.getUrl(doc_id);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                I.Run(FILER.GetText(),doc_id,Importance);
                Importance = filer.Dealing_Files(input);
                db.InsTitle(Importance[0],doc_id);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            System.out.println(doc_id+" "+input.getName());
             // break;
            try {
                doc_id=db.GetNotIndexed();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


}
