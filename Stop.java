import java.util.Arrays;
import java.util.List;


public class Stop {
	//3ayza asheel not
	static List<String> stopwords = Arrays.asList("a", "able", "about",
			"across", "after", "all", "almost", "also", "am", "among", "an",
			"and", "any", "are", "as","not", "at", "be", "because", "been", "but",
			"by", "can", "cannot", "could", "dear", "did", "do", "does",
			"either", "else", "ever", "every", "for", "from", "get", "got",
			"had", "has", "have", "he", "her", "hers", "him", "his", "how",
			"however", "i", "if", "in", "into", "is", "it", "its", "just",
			"least", "let", "like", "likely", "may", "me", "might", "most",
			"must", "my", "neither", "no", "nor", "of", "off", "often",
			"on", "only", "or", "other", "our", "own", "rather", "said", "say",
			"says", "she", "should", "since", "so", "some", "than", "that",
			"the", "their", "them", "then", "there", "these", "they", "this",
			"tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
			"what", "when", "where", "which", "while", "who", "whom", "why",
			"will", "with", "would", "yet", "you", "your");
	
	public static void main(String[] args) {
		int pos = 0;
		// TODO Auto-generated method stub
		String Str="menna to be. or not to be";
		String Expression = "";
		String[] parts =Str.split("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})");
		while(pos<parts.length)
			{
			String Phrase="";
			Expression = "";
			String First_Stop_Word="";
			if (pos<parts.length&&stopwords.contains(parts[pos]))
					{
				First_Stop_Word=parts[pos];
				Str = Str.replace(parts[pos], "");
				pos++;
					}
		while (pos<parts.length&&stopwords.contains(parts[pos]))
			{
			Phrase=Phrase+" "+parts[pos];
			Str = Str.replace(parts[pos], "");
			pos++;
			}
		if(Phrase.length()>1)
		{
			Expression=First_Stop_Word+Phrase;
					//store 
		}
		else 
			Phrase="";
		pos++;
		}
		System.out.println(Expression);
	}
			}

	


