/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

/**
 *
 * @author mennna
 */
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
	
	
	    public static Map<Long, Double> sortByValue(Map<Long, Double> unsortMap) {

	        // 1. Convert Map to List of Map
	        LinkedList<Entry<Long, Double>> list =
	                new LinkedList<Map.Entry<Long, Double>>(unsortMap.entrySet());

	        // 2. Sort list with Collections.sort(), provide a custom Comparator
	        //    Try switch the o1 o2 position for a different order
	        Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
	            public int compare(Map.Entry<Long, Double> o1,
	                               Map.Entry<Long, Double> o2) {
	                return (o2.getValue()).compareTo(o1.getValue());
	            }
	        });

	        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
	        Map<Long, Double> sortedMap = new LinkedHashMap<Long, Double>();
	        for (Map.Entry<Long, Double> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
	        return sortedMap;
	    }
	 
	public static double getIDF(String Word) throws SQLException 
	
	 { 
		double IDF;
		//edited a2olhm 3alih
	    IDF=Math.log10((double)DATABASE.getTotalNumberOfDocs()/(double)DATABASE.getwordDocsCount(Word));
		//IDF=(double)DATABASE.getTotalNumberOfDocs()/(double)DATABASE.getwordDocsCount(Word);
	   /* System.out.println("(double)DATABASE.getTotalNumberOfDocs()");
	    System.out.println((double)DATABASE.getTotalNumberOfDocs());
	    System.out.println("(double)DATABASE.getwordDocsCount(Word)");
	    System.out.println((double)DATABASE.getwordDocsCount(Word));
	    System.out.println("IDF");
	    System.out.println(IDF);*/
	    return IDF;
	 }
	public static double getTF_IDF(String Word,long doc_id) throws SQLException 
	 { 
		double TF_IDF;
		TF_IDF=DATABASE.getTF(Word,doc_id)*getIDF(Word);
		//System.out.println("TF_IDF");
	    System.out.println(TF_IDF);
	    //IDF=(double)DATABASE.getTotalNumberOfDocs()/(double)DATABASE.getwordDocsCount(Word);
	    return TF_IDF;
	 }
	//function to return sorted doc ids
        
        public static String temp(String query) throws SQLException
        {
            String s=" "+query;
            String[] Docs=DATABASE.getDocs(query);
            return s;
        }
public static Long[] Ranking(Long Docs[],String word[]) throws NumberFormatException, SQLException
{
	//String[] Docs=DATABASE.getDocs(word);
	Map<Long,Double> Docs_TF_IDF = new HashMap<Long,Double>(); 
        double acc=0;
	for(int i=0;i<Docs.length;i++)
	{
            acc=0;
            	for(int j=0;j<word.length;j++)
                {
                    acc+=getTF_IDF(word[j],Docs[i]);
                }
                acc=(0.8*acc)+(0.2*(DATABASE.GetRank(Docs[i])));
		/*System.out.println("Doc");
		System.out.println((Docs[i]));
		System.out.println(acc);*/
		Docs_TF_IDF.put(Docs[i],acc);
		System.out.println("end");
	}
	Map<Long,Double> SortedMap =RANKER.sortByValue(Docs_TF_IDF);
	for (Entry<Long,Double> entry: SortedMap.entrySet())
    { 
		Long  Key=entry.getKey();
		double Value=entry.getValue();
		/*System.out.println("key");
		System.out.println(Key);
		System.out.println("value");
		System.out.println(Value);*/
    }
	Set<Long> keys = SortedMap.keySet();
	Long[] array = keys.toArray(new Long[keys.size()]);
	return array;
	
	}
}

