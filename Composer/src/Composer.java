import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Composer {
	
	static String filename;
	static String readInPath = " /Users/jzhaoaf/Desktop/";
	static String outputPath;
	int MIN = 3;
	int MAX = 20;
	static int THRESHOLD = 15;
	
	public static void main(String[] args) {

		try {
			singleTest();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//batchTest(Folder);
		
	}

	private static void singleTest() throws FileNotFoundException {
		// TODO Auto-generated method stub
		readInPatterns("/Users/jzhaoaf/Desktop/allPattern15.txt");
	}

	private static void readInPatterns(String filename) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner s = new Scanner(new File(filename));
		String line;
		while(s.hasNextLine()) {
			line = s.nextLine();
			//System.out.println(line);
		}
		
	}
	
}
