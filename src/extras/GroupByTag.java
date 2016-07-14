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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class GroupByTag {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("convertedTags.txt"));
			BufferedReader br2 = new BufferedReader(new FileReader("convertedWords.txt"));
			HashMap<String,HashMap<String,Integer>> tagToMap = new HashMap<>();
			
			String line,line2;
			while (((line = br.readLine()) != null) && ((line2 = br2.readLine()) != null) ) {
				String[] words = line2.split(" ");
				String[] tags = line.split(" ");
				for(int i=0;i<words.length;i++) {
					if(tagToMap.containsKey(tags[i].trim())) {
						HashMap<String,Integer> temp = tagToMap.get(tags[i]);
						if(temp.containsKey(words[i].toLowerCase().trim())) {
							temp.put(words[i].toLowerCase().trim(), temp.get(words[i].toLowerCase().trim())+1);
						}
						else {
							temp.put(words[i].toLowerCase().trim(), 1);
						}
						tagToMap.put(tags[i].trim(), temp);
					}
					else {
						HashMap<String,Integer> temp = new HashMap<>();
						temp.put(words[i].toLowerCase().trim(), 1);
						tagToMap.put(tags[i].trim(), temp);
					}
				}
			}
			
			br.close();
			br2.close();
			
			File write = new File("Tags");
			if (!write.exists()) {
				write.mkdir();
			}
			int i=1;
			Iterator<String> it = tagToMap.keySet().iterator();
			while(it.hasNext()) {
				String tag = (String) it.next();
				//write = new File("Tags\\"+tag+".txt");
				write = new File("Tags\\"+i+".txt");
				write.createNewFile();
				FileWriter fw = new FileWriter(write.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw); 
				bw.write(tag);
				bw.newLine(); bw.newLine();
				HashMap<String,Integer> temp = tagToMap.get(tag);
				
				HashMap<String,Integer> sortedTemp = new LinkedHashMap<>();
				List list = new LinkedList(temp.entrySet());
				Collections.sort(list, new Comparator() {
					public int compare(Object o1, Object o2) {
						return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
						}});
				for (Iterator it2 = list.iterator(); it2.hasNext();) {
					Map.Entry mapentry = (Map.Entry) it2.next();
				    sortedTemp.put((String)mapentry.getKey(), (Integer)mapentry.getValue());
				} 
				
				Iterator<String> it2 = sortedTemp.keySet().iterator();
				while(it2.hasNext()) {
					String key = it2.next();
					bw.write(key+":"+sortedTemp.get(key));
					bw.newLine();
				}
				bw.close();
				
				i+=1;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
