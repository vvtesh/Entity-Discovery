package extras;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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


public class PatternMiningWeighted {

	private HashMap<String,Integer> ExtractPattern(int proximity) {
		HashMap<String,Integer> patterns = new HashMap<>();
		Set<String> knownEntities = new HashSet<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("KnownEntities.txt"));
			String line;
			while((line = br.readLine())!=null) {
				knownEntities.add(line.toLowerCase().trim());
			}
			br.close();
			
			BufferedReader brTag = new BufferedReader(new FileReader("convertedTags.txt"));
			BufferedReader brWord = new BufferedReader(new FileReader("convertedWords.txt"));
			
			String line2;
			while (((line = brTag.readLine()) != null) && ((line2 = brWord.readLine()) != null) ) {
				String[] words = line2.split(" ");
				String[] tags = line.split(" ");
				for(int i=0;i<words.length;i++) {
					
					//if (words[i].toLowerCase().trim().equals("array")) {
					if(knownEntities.contains(words[i].toLowerCase().trim())) {
						String temp = "ENTITY";
						int count = 0;
						int start = (i-1), end = (i+1);
						while(count<=proximity) {
							if(start>=0 && end<tags.length) {
								temp = tags[start] + " " + temp + " " + tags[end];
								start = start-1;
								end+=1;
								count+=2;
							}
							else if(start>=0) {
								temp = tags[start] + " "+temp;
								start-=1;
								count+=1;
							}
							else if(end<tags.length) {
								temp = temp + " "+tags[end];
								end+=1;
								count+=1;
							}
							else {
								break;
							}
						}
						
						//"if" needed if you want exact count, otherwise it'll get smaller patterns too. 
						if(count>=proximity) {
							temp = temp.replace(".", "").trim();
							
							if(patterns.containsKey(temp)) {
								patterns.put(temp, 1+patterns.get(temp));
							}
							else {
								patterns.put(temp, 1);
							}
						}
						
					}
				}
			}
			
			brTag.close();
			brWord.close();
			
			Iterator<String> it = patterns.keySet().iterator();
			while(it.hasNext()) {
				if(patterns.get(it.next())<2) {
					it.remove();
				}
			}
			
			HashMap<String,Integer> sortedPatterns = new LinkedHashMap<>();
			List list = new LinkedList(patterns.entrySet());
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Comparable) ((Map.Entry) (o2)).getValue())
					.compareTo(((Map.Entry) (o1)).getValue());
					}});
			for (Iterator it2 = list.iterator(); it2.hasNext();) {
				Map.Entry mapentry = (Map.Entry) it2.next();
			    sortedPatterns.put((String)mapentry.getKey(), (Integer)mapentry.getValue());
			} 
			
			File write = new File("MinedPatterns.txt");
			write.createNewFile();
			FileWriter fw = new FileWriter(write.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw); 
			
			Iterator<String> it2 = sortedPatterns.keySet().iterator();
			while(it2.hasNext()) {
				String key = it2.next();
				bw.write(key+":"+sortedPatterns.get(key)+System.lineSeparator());
			}
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return patterns;
	}
	
	public void DiscoverEntity(int proximity) {
		HashMap<String,Integer> patterns = ExtractPattern(proximity);
		HashMap<String[],Integer> divPatterns = new HashMap<>();
		HashMap<String,Integer> entities = new HashMap<>();
		
		Iterator<String> it = patterns.keySet().iterator();
		while(it.hasNext()) {
			String pattern = it.next();
			int index = pattern.indexOf("ENTITY");
			String[] temp = new String[2];
			temp[0] = pattern.substring(0, index).trim();
			temp[1] = pattern.substring(index+6).trim();
			divPatterns.put(temp, patterns.get(pattern));
		}
		
		try {
			BufferedReader brTag = new BufferedReader(new FileReader("convertedTags.txt"));
			BufferedReader brWord = new BufferedReader(new FileReader("convertedWords.txt"));
			
			ArrayList<String> matchedPatterns = new ArrayList<>();
			
			String line,line2;
			long xyzzzz=1;
			while (((line = brTag.readLine()) != null) && ((line2 = brWord.readLine()) != null) ) {
				xyzzzz+=1;
				System.out.println(xyzzzz);
				String[] words = line2.split(" ");
				String[] tags = line.split(" ");
				for(int i=0;i<words.length;i++) {
					
					//minimum size of entity constraint.
					if(words[i].toLowerCase().trim().length()>3) {	
						Iterator<String[]> it2 = divPatterns.keySet().iterator();
						while(it2.hasNext()) {
							String[] pattern = it2.next();
							int leftSize = pattern[0].split(" ").length;
							int rightSize;
							if(pattern[1].isEmpty()) {
								rightSize = 0;
							} else {
								rightSize = pattern[1].split(" ").length;
							}
							int leftIndex = (i-leftSize);
							int rightIndex = i+1;
							if(leftIndex >= 0 && (rightIndex+rightSize-1)<words.length) {
								String left = "";
								for(int x = leftIndex;x<(leftIndex+leftSize);x++) {
									left = left+" "+tags[x];
								}
								if(left.trim().equals(pattern[0])) {
									String right = "";
									for(int x = rightIndex;x<(rightIndex+rightSize);x++) {
										right = right+" "+tags[x];
									}
									if(right.trim().equals(pattern[1])) {
										//if(tags[i].trim().equals("NN") || tags[i].trim().equals("NNS")) {
											//Found entity! Pattern matched!
											if(entities.containsKey(words[i].toLowerCase().trim())) {
												entities.put(words[i].toLowerCase().trim(), divPatterns.get(pattern)+entities.get(words[i].toLowerCase().trim()));
												//entities.put(words[i].toLowerCase().trim(), 1+entities.get(words[i].toLowerCase().trim()));
											}
											else {
												entities.put(words[i].toLowerCase().trim(), divPatterns.get(pattern));
												//entities.put(words[i].toLowerCase().trim(), 1);
											}
											matchedPatterns.add(left.trim()+" "+tags[i].trim()+" "+right.trim());
											break;
										//}
									}
								}
							}
						}
					}
					
				}
			}
			
			brTag.close();
			brWord.close();
			
			//sort and write entities
			
			HashMap<String,Integer> sortedEntities = new LinkedHashMap<>();
			List list = new LinkedList(entities.entrySet());
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Comparable) ((Map.Entry) (o2)).getValue())
					.compareTo(((Map.Entry) (o1)).getValue());
					}});
			for (Iterator it2 = list.iterator(); it2.hasNext();) {
				Map.Entry mapentry = (Map.Entry) it2.next();
			    sortedEntities.put((String)mapentry.getKey(), (Integer)mapentry.getValue());
			} 
			
			//English stopword list available from http://http://snowball.tartarus.org/algorithms/english/stop.txt
			String stopWordsString = "I me my myself we us our ours ourselves you your yours yourself yourselves he him his himself she her hers herself it its itself they them their theirs themselves what which who whom this that these those am is are was were be been being have has had having do does did doing will would shall should can could may might must ought i'm you're he's she's it's we're they're i've you've we've they've i'd you'd he'd she'd we'd they'd i'll you'll he'll she'll we'll they'll isn't aren't wasn't weren't hasn't haven't hadn't doesn't don't didn't won't wouldn't shan't shouldn't can't cannot couldn't mustn't let's that's who's what's here's there's when's where's why's how's daren't needn't oughtn't mightn't a an the is so and but if or because as until while of at by for with about against between into through during before after above below to from up down in out on off over under again further then once here there when where why how all any both each few more most other some such no nor not only own same so than too very one every least less many now ever never say says said also get go goes just made make put see seen whether like well back even still way take since another however two three four five first second new old high long";
			
			//Java keywords as here: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html + a few additions
			String JavaKeywordsString = "abstract assert boolean break byte case catch char class const continue"
					+ " default do double else enum extends final finally float for goto"
					+ " if implements import instanceof int interface long native new package private protected public"
					+ " return short static strictfp super switch synchronized this throw throws transient try void"
					+ " volatile while true false null"
					+ " list class";
			
			String[] stopWordsArray = stopWordsString.split(" ");
			String[] JavaKeywordsArray = JavaKeywordsString.split(" ");
			for(int i=0;i<stopWordsArray.length;i++) {
				if(sortedEntities.containsKey(stopWordsArray[i].toLowerCase().trim())) {
					sortedEntities.remove(stopWordsArray[i].toLowerCase().trim());
				}
			}
			for(int i=0;i<JavaKeywordsArray.length;i++) {
				if(sortedEntities.containsKey(JavaKeywordsArray[i].toLowerCase().trim())) {
					sortedEntities.remove(JavaKeywordsArray[i].toLowerCase().trim());
				}
			}
			
			/*Set<String> foreignWord = new HashSet<>();
			BufferedReader br = new BufferedReader(new FileReader("Tags\\12.txt"));
			while((line = br.readLine())!=null) {
				foreignWord.add(line.split(":")[0].toLowerCase().trim());
			}
			br.close();
			
			Iterator<String> it2 = foreignWord.iterator();
			while(it2.hasNext()) {
				String key = it2.next();
				if(sortedEntities.containsKey(key)) {
					sortedEntities.remove(key);
				}
			}*/
			
			File write = new File("DiscoveredEntities.txt");
			write.createNewFile();
			FileWriter fw = new FileWriter(write.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw); 
			
			Iterator<String> it2 = sortedEntities.keySet().iterator();
			while(it2.hasNext()) {
				String key = it2.next();
				bw.write(key+":"+sortedEntities.get(key)+System.lineSeparator());
			}
			bw.close();
			
			write = new File("MatchedPatterns.txt");
			write.createNewFile();
			fw = new FileWriter(write.getAbsoluteFile());
			bw = new BufferedWriter(fw); 
			
			it2 = matchedPatterns.iterator();
			while(it2.hasNext()) {
				bw.write(it2.next()+System.lineSeparator());
			}
			bw.close();
			
			//Random50
			ArrayList<String> random = new ArrayList<>();
			it2 = sortedEntities.keySet().iterator();
			int i=1;
			while(it2.hasNext() && i<=200) {
				random.add(it2.next());
				i+=1;
			}
			Collections.shuffle(random);
			
			write = new File("Random50.txt");
			write.createNewFile();
			fw = new FileWriter(write.getAbsoluteFile());
			bw = new BufferedWriter(fw); 
			
			it2 = random.iterator();
			i=1;
			while(it2.hasNext() && i<=50) {
				bw.write(it2.next()+System.lineSeparator());
				i+=1;
			}
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PatternMiningWeighted pm = new PatternMiningWeighted();
		pm.DiscoverEntity(7);
	}

	/*
	 * Possible additions:- 
	 * Done: Line 44: Add more entities instead of just array. 
	 * Done: Line 264 (while calling the function): Change proximity value.
	 * Done: Line 205: Remove stop words and java keywords.
	 * Done, removes "array" too!!!: Line 228: Remove foreign words.
	 * Done, better result: Line 137: Keep a minimum size constraint for entity.
	 * Done, better result: Line 70: Make pattern size at least as long as proximity size.
	 * Check if substring of word contains the seed entity instead of exact match.
	 * Take mined patterns with weight greater than 1
	 */
	
}
