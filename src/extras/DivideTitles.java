package extras;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class DivideTitles {

	/**
	 * @param args
	 */
	public static void divide() {
		// TODO Auto-generated method stub
		int i=0;
		BufferedReader br;
		try {
			
			
			
			br = new BufferedReader(new FileReader("titles2.txt"));
			File write = new File("titles\\titles2_" +i+".txt");
			write.createNewFile();
			FileWriter fw = new FileWriter(write.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw); 
		    String line;
		    long j=1;
		    while((line = br.readLine()) != null) {
		    	bw.write(line+"\n");
	    		j++;
		    	if(j%2500==0) {
		    		bw.close();
		    		i++;
		    		write = new File("titles\\titles2_" +i+".txt");
					write.createNewFile();
					fw = new FileWriter(write.getAbsoluteFile());
					bw = new BufferedWriter(fw);
		    	}
		    	//bw.write(line+"."+System.lineSeparator());
		    }
		    bw.close();
		    br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		
	}

}
