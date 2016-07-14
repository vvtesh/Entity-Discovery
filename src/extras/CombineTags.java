package extras;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CombineTags {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		try {
			
			File write = new File("convertedTags.txt");
			write.createNewFile();
			FileWriter fw = new FileWriter(write.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw); 
			File write2 = new File("convertedWords.txt");
			write2.createNewFile();
			FileWriter fw2 = new FileWriter(write2.getAbsoluteFile());
			BufferedWriter bw2 = new BufferedWriter(fw2); 
			
			for(int i=0;i<395;i++) {
				BufferedReader br = new BufferedReader(new FileReader("convertedTags\\convertedTags" +i+".txt"));
				BufferedReader br2 = new BufferedReader(new FileReader("convertedWords\\convertedWords" +i+".txt"));
			    String line;
			    while((line = br.readLine()) != null) {
			    	bw.write(line+"\n");
			    }
			    while((line = br2.readLine()) != null) {
			    	bw2.write(line+"\n");
			    }
			    br.close();
			    br2.close();
			}
			bw.close();
			bw2.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
