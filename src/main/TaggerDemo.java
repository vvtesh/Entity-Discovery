package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

//import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

class TaggerDemo {

  private TaggerDemo() {}

  public static void main(String[] args) throws Exception {
	//URL to tagger (I think it was the English one under "Download" section of the page, not sure though, anyway have included the tagger in folder already): http://nlp.stanford.edu/software/tagger.shtml
    MaxentTagger tagger = new MaxentTagger("english-bidirectional-distsim.tagger");
    
    //Appending "." at the end of every title.
    BufferedReader br = new BufferedReader(new FileReader("titles.txt"));
    File write = new File("titles2.txt");
	write.createNewFile();
	FileWriter fw = new FileWriter(write.getAbsoluteFile());
	BufferedWriter bw = new BufferedWriter(fw); 
    String line;
    while((line = br.readLine()) != null) {
    	bw.write(line+"."+System.lineSeparator());
    }
    bw.close();
    br.close();
    
    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader("titles2.txt")));
	write = new File("convertedWords.txt");
	File write2 = new File("convertedTags.txt");
	write.createNewFile();
	write2.createNewFile();
	fw = new FileWriter(write.getAbsoluteFile());
	FileWriter fw2 = new FileWriter(write2.getAbsoluteFile());
	bw = new BufferedWriter(fw);
	BufferedWriter bw2 = new BufferedWriter(fw2);
    for (List<HasWord> sentence : sentences) {
      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
      //System.out.println(Sentence.listToString(tSentence, false));
      //bw.write(Sentence.listToString(tSentence, false)+"\n");
      Iterator<TaggedWord> it = tSentence.iterator();
      while(it.hasNext()) {
    	  TaggedWord temp = it.next();
    	  //System.out.println(temp.value()+" : "+temp.tag());
    	  bw.write(temp.value()+" ");
    	  bw2.write(temp.tag()+" ");
      }
      bw.write(System.lineSeparator());
      bw2.write(System.lineSeparator());
	}
    bw.close();
    bw2.close();
  }
}
