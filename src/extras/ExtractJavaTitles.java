package extras;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ExtractJavaTitles {

	public void extract(String readFrom) {
		
		try {
			
			
			BufferedReader br = new BufferedReader(new FileReader(readFrom));
			
			
			//FileWriter and BufferWriter to write a file.
			
			FileWriter fw = null;
			BufferedWriter bw = null;
			File titles = new File("titles.txt");
			
			titles.createNewFile();
			
			fw = new FileWriter(titles.getAbsoluteFile());
			
			bw = new BufferedWriter(fw);
			
			String line;
			//Skipping first two lines in xml file.
			for(int j=0;j<2;j++) {
				line = br.readLine();
				//System.out.println(line);
			}
			//System.out.println("Skipped");
			int number = 0;
			
			//Reading each row
			while ((line = br.readLine()) != null) {
				
				//System.out.println(line);
				
				String[] elements = line.split("\"");
				
				String title = "";
				String tagsNode = "";
				String[] tags = null;
				String body = "";
				
				for(int j=0;j<elements.length;j++) {
					if(elements[j].equals(" Title=")) {
						title = elements[j+1];
						title = title.replace("&amp;", "&");
						title = title.replace("&#xD;&#xA;", System.lineSeparator());
						title = title.replace("&#xA;","\n");
						title = title.replace("&gt;", ">");
						title = title.replace("&lt;","<");
						title = title.replace("&quot;", "\"");
						title = title.replace("&apos;", "\'");
						title = title.replace("&amp;", "&");
						break;
					}
				}
				
				int codep=0;
				
				for(int j=0;j<elements.length;j++) {
					if(elements[j].equals(" Body=")) {
						body = elements[j+1];
						body = body.replace("&gt;", ">");
						body = body.replace("&lt;","<");
						
						if(body.contains("<code>") && body.contains("</code>")) {
							codep=1;
						}
						
						break;
					}
				}
				
				for(int j=0;j<elements.length;j++) {
					if(elements[j].equals(" Tags=")) {
						tagsNode = elements[j+1];
						tagsNode = tagsNode.replace("&gt;", ">");
						tagsNode = tagsNode.replace("&lt;","<");
						tags = tagsNode.split("<");
						//System.out.println("Here: x" + tagsNode);
						for(int tagIndex=0; tagIndex<tags.length;tagIndex++) {
							//System.out.print(tag+" xxxx ");
							if(!tags[tagIndex].isEmpty())
								tags[tagIndex] = tags[tagIndex].substring(0, tags[tagIndex].length()-1); 
						}
						
						break;
					}
					
				}
				
				//System.out.println("Title is: "+title+" --- Tags are: "+tagsNode);
				if(tags!=null && codep==1) {
					for(String tag:tags) {
						if(tag.toLowerCase().equals("java")) {
							number++;
							System.out.println(number);
							//System.out.println(tag);
							bw.write(title+"\n");
						}
					}
				}
			}

			//Close BufferedWriter
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
	
	public static void main(String[] args) {
		String readFrom = "E:\\stackoverflow.com-Posts\\Posts.xml";
		ExtractJavaTitles u = new ExtractJavaTitles();
		u.extract(readFrom);
		System.out.println("Done");
	}

}
