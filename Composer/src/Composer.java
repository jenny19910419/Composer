import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.Sequence;


public class Composer {
	
	static String filename; //the filename for generating patterns
	static String readInPath = " /Users/jzhaoaf/Desktop/";
	static String outputPath;
	static int MIN = 3;
	static int MAX = 20;
	static int THRESHOLD = 15;
	static ArrayList <Map<ArrayList<Integer>, ArrayList<Integer> > > patternPools; 
	static Sequence sequence;  
	static MidiPlayer myPlayer;
	static String lrcFilename; 
	
	public static void main(String[] args) {

		//initialize the patternPools
		patternPools = new  ArrayList<Map<ArrayList<Integer>, ArrayList<Integer> > >();
		for(int i = 0 ; i < MIN; ++i) {
			patternPools.add(new HashMap<ArrayList<Integer>, ArrayList<Integer> >());
		}
		
		
		try {
			singleTest();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//batchTest(new Folder());
		sequence = generateSequence(filename);
		
		//play the generate sequence
		myPlayer = new MidiPlayer();
		myPlayer.play(sequence, false);
		
	}

	private static Sequence generateSequence(String filename) {
		// TODO Auto-generated method stub
		//generate sequence for lrc files
		ArrayList<Integer> lrc = new ArrayList<Integer>();
		ArrayList<Integer> melo = new ArrayList<Integer>();
		
		//suppose given lrc sequence; change later
		Random random = new Random();
		for(int i = 0 ; i < 100; ++i) {
			lrc.add(new Integer(random.nextInt(10)));
		}
		
		
		
		
		
		
		
		return null;
	}
	
	public static String lcs(String a, String b) {
	    int[][] lengths = new int[a.length()+1][b.length()+1];
	 
	    // row 0 and column 0 are initialized to 0 already
	 
	    for (int i = 0; i < a.length(); i++)
	        for (int j = 0; j < b.length(); j++)
	            if (a.charAt(i) == b.charAt(j))
	                lengths[i+1][j+1] = lengths[i][j] + 1;
	            else
	                lengths[i+1][j+1] =
	                    Math.max(lengths[i+1][j], lengths[i][j+1]);
	 
	    // read the substring out from the matrix
	    StringBuffer sb = new StringBuffer();
	    for (int x = a.length(), y = b.length();
	         x != 0 && y != 0; ) {
	        if (lengths[x][y] == lengths[x-1][y])
	            x--;
	        else if (lengths[x][y] == lengths[x][y-1])
	            y--;
	        else {
	            assert a.charAt(x-1) == b.charAt(y-1);
	            sb.append(a.charAt(x-1));
	            x--;
	            y--;
	        }
	    }
	 
	    return sb.reverse().toString();
	}

	private static void singleTest() throws FileNotFoundException {
		// TODO Auto-generated method stub
		readInPatterns("/Users/jzhaoaf/Desktop/allPattern15.txt");
	}

	private static void readInPatterns(String filename) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		Scanner s = new Scanner(new File(filename));
		String line;
		ArrayList<Integer> lrc = new ArrayList<Integer>();
		ArrayList<Integer> melo = new ArrayList<Integer>();
		//SequencePair currentSeq = null;
		Map<ArrayList<Integer>, ArrayList<Integer> > currentMap = new HashMap<ArrayList<Integer>, ArrayList<Integer> > ();
		
		int size = MIN;
		
		while(s.hasNextLine()) {
			
			lrc = new ArrayList<Integer>();
			melo = new ArrayList<Integer>();
			
			
			line = s.nextLine();
			if(line.charAt(0)!='{') {
				if(line.indexOf("size:")!=-1) {
					int index = line.indexOf(':');
					size = Integer.parseInt(line.substring(index+1));
					//System.out.println("Currently Checking " + size);
					//System.out.println(currentMap);
					patternPools.add(currentMap);
					currentMap = new HashMap<ArrayList<Integer>, ArrayList<Integer> > ();
					
				}
					
				continue;
			}
			line = line.substring(1, line.length()-1);
			//System.out.println(line);
			int beginIndex, endIndex,comma;
			String currentPair, first, second;
			
			
			while(true) {
				
				beginIndex = line.indexOf('{');
				endIndex = line.indexOf('}');
				if(beginIndex == -1) break;
				currentPair = line.substring(beginIndex+1,endIndex);
				//System.out.println(currentPair+ " ");
				comma = currentPair.indexOf(',');
				first = currentPair.substring(0,comma);
				second = currentPair.substring(comma+1);
				lrc.add(new Integer(Integer.parseInt(first)));
				melo.add(new Integer(Integer.parseInt(second)));
				line = line.substring(endIndex+1); 
			}
			
			//currentSeq = new SequencePair(lrc,melo);
			//System.out.println(currentSeq.lrcSeq);
			//System.out.println(currentSeq.meloSeq);
			
			//if use maps there will overlap
			currentMap.put(lrc, melo);	
		
			
		}
		
		//test 
		/*System.out.println("final");
		for(int i = 0 ; i <= MAX; ++i) {
			System.out.println("size" + (i-1) + "count");
			Map<ArrayList<Integer>, ArrayList<Integer> > map = patternPools.get(i);
			System.out.println(map);
			System.out.println(map.size());
			
		}*/
	}
	
}
