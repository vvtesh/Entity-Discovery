# Entity-Discovery
Entity Discovery on Source Code

Assuming you have all titles in a file named "titles.txt" and seed entities in file named "KnownEntities.txt" in your project folder:
	1. Add jar "stanford-postagger.jar" from the jars folder.
	2. Run the file "TaggerDemo.java" from "main" package. (The file already contains the code to append "." at end of every title. I've also already included the tagger in project folder.)
	3. Set the "proximity" factor (currently set to 4) in the file "PatternMiningMostFrequent.java" in main function. Run the file "PatternMiningMostFrequent.java" from "main" package. The file "DiscoveredEntities.txt" will contain the entities in decreasing order.
	
For demo:
	1. Move "titles.txt" and "KnownEntities.txt" from the "demo" folder to project folder (i.e. just move the files outside demo folder).
	2. Compare the remaining files in "demo" folder with the ones generated after running the code (mainly discovered entities).
