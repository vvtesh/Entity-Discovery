package extras;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class EntityToTags {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Set<String> knownEntities = new HashSet<>();
			BufferedReader br = new BufferedReader(new FileReader("KnownEntities.txt"));
			String line;
			while((line = br.readLine())!=null) {
				knownEntities.add(line.toLowerCase().trim());
			}
			br.close();
			br = new BufferedReader(new FileReader("convertedTags.txt"));
			BufferedReader br2 = new BufferedReader(new FileReader("convertedWords.txt"));
			//HashMap<String,HashMap<String,Integer>> tagToMap = new HashMap<>();
			HashMap<String, HashSet<String>> entityToTags = new HashMap<String, HashSet<String>>();
			
			String line2;
			while (((line = br.readLine()) != null) && ((line2 = br2.readLine()) != null) ) {
				String[] words = line2.split(" ");
				String[] tags = line.split(" ");
				for(int i=0;i<words.length;i++) {
					if(knownEntities.contains(words[i].toLowerCase().trim())) {
						if(entityToTags.containsKey(words[i].toLowerCase().trim())) {
							HashSet<String> temp = entityToTags.get(words[i].toLowerCase().trim());
							temp.add(tags[i].trim());
							entityToTags.put(words[i].toLowerCase().trim(), temp);
						}
						else {
							HashSet<String> temp = new HashSet<String>();
							temp.add(tags[i].trim());
							entityToTags.put(words[i].toLowerCase().trim(), temp);
						}
					}
				}
			}
			
			br.close();
			br2.close();
			//System.out.println(entityToTags);
			
			File write = new File("entityToTags.txt");
			write.createNewFile();
			FileWriter fw = new FileWriter(write.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw); 
			
			
			Iterator<String> it = entityToTags.keySet().iterator();
			while(it.hasNext()) {
				String entity = (String) it.next();
				bw.write(entity+" --> "+entityToTags.get(entity)+"\n");
				
			}
			
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
