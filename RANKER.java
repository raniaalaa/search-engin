import java.awt.List;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RANKER {
	
	
	    public static Map<Integer, Double> sortByValue(Map<Integer, Double> unsortMap) {

	        // 1. Convert Map to List of Map
	        LinkedList<Entry<Integer, Double>> list =
	                new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());

	        // 2. Sort list with Collections.sort(), provide a custom Comparator
	        //    Try switch the o1 o2 position for a different order
	        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
	            public int compare(Map.Entry<Integer, Double> o1,
	                               Map.Entry<Integer, Double> o2) {
	                return (o2.getValue()).compareTo(o1.getValue());
	            }
	        });

	        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
	        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
	        for (Map.Entry<Integer, Double> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
	        return sortedMap;
	    }
	 
	public static double getIDF(String Word) throws SQLException 
	
	 { 
		double IDF;
	    IDF=Math.log10((double)DATABASE.getTotalNumberOfDocs()/(double)DATABASE.getwordDocsCount(Word));
	    System.out.println("(double)DATABASE.getTotalNumberOfDocs()");
	    System.out.println((double)DATABASE.getTotalNumberOfDocs());
	    System.out.println("(double)DATABASE.getwordDocsCount(Word)");
	    System.out.println((double)DATABASE.getwordDocsCount(Word));
	    System.out.println("IDF");
	    System.out.println(IDF);
	    return IDF;
	 }
	public static double getTF_IDF(String Word,long doc_id) throws SQLException 
	 { 
		double TF_IDF;
		TF_IDF=DATABASE.getTF(Word,doc_id)*getIDF(Word);
		System.out.println("TF_IDF");
	    System.out.println(TF_IDF);
	    //IDF=(double)DATABASE.getTotalNumberOfDocs()/(double)DATABASE.getwordDocsCount(Word);
	    return TF_IDF;
	 }
	//function to return sorted doc ids
public static Integer[] Ranking(String word) throws NumberFormatException, SQLException
{
	String[] Docs=DATABASE.getDocs(word);
	Map<Integer,Double> Docs_TF_IDF = new HashMap<Integer,Double>(); 
	for(int i=0;i<Docs.length;i++)
	{
		System.out.println("Doc");
		System.out.println(Integer.parseInt(Docs[i]));
		System.out.println(getTF_IDF(word,Integer.parseInt(Docs[i])));
		Docs_TF_IDF.put(Integer.parseInt(Docs[i]),getTF_IDF(word,Integer.parseInt(Docs[i])));
		System.out.println("end");
	}
	Map<Integer,Double> SortedMap =RANKER.sortByValue(Docs_TF_IDF);
	for (Entry<Integer,Double> entry: SortedMap.entrySet())
    { 
		int  Key=entry.getKey();
		double Value=entry.getValue();
    }
	Set<Integer> keys = SortedMap.keySet();
	Integer[] array = keys.toArray(new Integer[keys.size()]);
	return array;
	
	}
}
