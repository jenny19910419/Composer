import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

import Phonetic.WordStruct;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;


public class Composer {
	
	static String filename; //the filename for generating patterns
	static String readInPath = "/Users/jzhaoaf/Desktop/";
	static String outputPath;
	static int MIN = 2;
	static int MAX = 20;
	static int THRESHOLD = 5;
	static ArrayList <Map<ArrayList<Integer>, ArrayList<Integer> > > patternPools; 
	static ArrayList <Map<ArrayList<Integer>, ArrayList<Double> > > newPatternPools; 
	static Sequence sequence;  
	static MidiPlayer myPlayer;
	static String lrcFilename; 
	static int resolution = 480;
	private static HTree hashtable;
	public static  HashMap<String, Integer> beatsMap;
	static String beatFirst;
	static String beatSecond;
	static ArrayList<Integer> beatArray;
	static String jasonResult;
	
	static void beatMap() {
		beatsMap = new HashMap<String,Integer>();
		beatsMap.put("2+2+1", 3);
		beatsMap.put("2+2+2", 3);
		
		beatsMap.put("4+1+1", 3);
		
		beatsMap.put("4+2+1", 3);
		beatsMap.put("4+2+2", 1);
		
		beatsMap.put("4+3+1", 3);
		beatsMap.put("4+3+2", 1);
		beatsMap.put("4+3+3", 1);
		
		beatsMap.put("4+4+1", 3);
		beatsMap.put("4+4+2", 1);
		beatsMap.put("4+4+3", 2);
		beatsMap.put("4+4+4", 1);
		
		beatsMap.put("4+5+1", 3);
		beatsMap.put("4+5+2", 1);
		beatsMap.put("4+5+3", 3);
		beatsMap.put("4+5+4", 1);
		beatsMap.put("4+5+5", 1);
		
		beatsMap.put("4+6+1", 3);
		beatsMap.put("4+6+2", 1);
		beatsMap.put("4+6+3", 1);
		beatsMap.put("4+6+4", 2);
		beatsMap.put("4+6+5", 1);
		beatsMap.put("4+6+6", 1);
		
		beatsMap.put("8+3+1", 3);
		beatsMap.put("8+3+2", 1);
		beatsMap.put("8+3+3", 1);
		
		beatsMap.put("8+4+1", 3);
		beatsMap.put("8+4+2", 1);
		beatsMap.put("8+4+3", 2);
		beatsMap.put("8+4+4", 1);
		
		beatsMap.put("8+6+1", 3);
		beatsMap.put("8+6+2", 1);
		beatsMap.put("8+6+3", 1);
		beatsMap.put("8+6+4", 2);
		beatsMap.put("8+6+5", 1);
		beatsMap.put("8+6+6", 1);
		
		beatsMap.put("8+12+1", 3);
		beatsMap.put("8+12+2", 1);
		beatsMap.put("8+12+3", 1);
		beatsMap.put("8+12+4", 2);
		beatsMap.put("8+12+5", 1);
		beatsMap.put("8+12+6", 1);
		beatsMap.put("8+12+7", 2);
		beatsMap.put("8+12+8", 1);
		beatsMap.put("8+12+9", 1);
		beatsMap.put("8+12+10", 2);
		beatsMap.put("8+12+11", 1);
		beatsMap.put("8+12+12", 1);
	}
	public static void main(String[] args) throws IOException, InvalidMidiDataException {
		//prepare beatMap
		beatMap();
		new Composer("4", "6");
	}
	
	Composer(String beatFirst, String beatSecond) throws IOException, InvalidMidiDataException {

		//initialize the patternPools
		patternPools = new  ArrayList<Map<ArrayList<Integer>, ArrayList<Integer> > >();
		newPatternPools = new ArrayList<Map<ArrayList<Integer>, ArrayList<Double> > >();
		
		for(int i = 0 ; i < MAX; ++i) {
			patternPools.add(new HashMap<ArrayList<Integer>, ArrayList<Integer> >());
			newPatternPools.add(new HashMap<ArrayList<Integer>, ArrayList<Double> >());
		}
		
		//ini
		this.beatFirst = beatFirst;
		this.beatSecond = beatSecond;
		//take use of beat information
				beatArray = new ArrayList<Integer>();
				
				for(int i = 1; i <= Integer.parseInt(beatSecond); ++i) {
					if(beatsMap.get(beatFirst+"+"+beatSecond+"+"+i)==null) 
						System.out.println("does not exist this type");
					else 
						beatArray.add(beatsMap.get(beatFirst+"+"+beatSecond+"+"+i));
					
				}
		
		try {
			singleTest();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*System.out.println("final");
		for(int i = 0 ; i <= MAX; ++i) {
			System.out.println("size" + (i-1) + "count");
			Map<ArrayList<Integer>, ArrayList<Integer> > map = patternPools.get(i);
			System.out.println(map);
			System.out.println(map.size());
			
		}*/
		int start = 0;
		//batchTest(new Folder());
		ArrayList<Integer> sequenceArray = generateSequenceArray(filename);
		//System.out.println(sequenceArray);
        //ArrayList<Integer> newSequenceArray = sequenceArray; //fake here change later
        
       jasonResult = "[";
 
       
       for(int i = 0; i < sequenceArray.size(); ++i) {
    	   jasonResult = jasonResult +  "{keys:" + sequenceArray.get(i)+"," +"duration:" + 8 +"},";
 
       }
       jasonResult = jasonResult.substring(0, jasonResult.length()-1);
       jasonResult+="]";
       System.out.println(jasonResult);
       
		
		
		
		sequence = generateSequence(sequenceArray);
		
		//test
		//sequence = MidiSystem.getSequence(new File("/Users/jzhaoaf/Desktop/2_hearts.mid"));
		//System.out.println(sequence.getResolution());  //Resolution is 480
		
		//play the generate sequence
		myPlayer = new MidiPlayer();
		myPlayer.play(sequence, false);
		
	}

	private static Sequence generateSequence(ArrayList<Integer> array) throws InvalidMidiDataException, IOException {
		// TODO Auto-generated method stub
		Sequence sequence = new Sequence(Sequence.PPQ, resolution);
		Track t = sequence.createTrack();
		
		//Turn on General MIDI sound set
		byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
		SysexMessage sm = new SysexMessage();
		sm.setMessage(b, 6);
		MidiEvent me = new MidiEvent(sm,(long)0);
		t.add(me);
		
		//set tempo
		MetaMessage mt = new MetaMessage();
        byte[] bt = {0x02, (byte)0x00, 0x00};
		mt.setMessage(0x51 ,bt, 3);
		me = new MidiEvent(mt,(long)0);
		t.add(me);
		
		//set trackname
		mt = new MetaMessage();
		String TrackName = new String("midifile track");
		mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
		me = new MidiEvent(mt,(long)0);
		t.add(me);
		
		//set omni on
		ShortMessage mm = new ShortMessage();
		mm.setMessage(0xB0, 0x7D,0x00);
		me = new MidiEvent(mm,(long)0);
		t.add(me);
		
		//set ploy on
		mm = new ShortMessage();
		mm.setMessage(0xB0, 0x7F,0x00);
		me = new MidiEvent(mm,(long)0);
		t.add(me);
		
		//set Instrument to Piano
		mm = new ShortMessage();
		mm.setMessage(0xC0, 0x00, 0x00);
		me = new MidiEvent(mm,(long)0);
		t.add(me);
		
		//middle part
		int pitch = 0x3C;  //start with middle C
		int start = 0;
		for(int i = 0; i < array.size(); ++i) {
			
			//System.out.println("get into here" + i);
			//note on
			mm = new ShortMessage();
			pitch += array.get(i); 
			mm.setMessage(0x90 ,pitch + beatArray.get(start), 0x60);
			//System.out.println(pitch);
			//System.out.println(pitch + beatArray.get(start));
			start = (1+ start) % Integer.parseInt(beatSecond);
			me = new MidiEvent(mm,(long) (i) * 1000);
			t.add(me);
			///note off
			mm = new ShortMessage();
			mm.setMessage(0x80 ,pitch + beatArray.get(start) , 0x40);
			start = (1+ start) % Integer.parseInt(beatSecond);
			me = new MidiEvent(mm,(long) (i+0.5) * 1000);
			t.add(me);
			
			//test using middle C
			/*mm = new ShortMessage();
			mm.setMessage(0x90,0x3C,0x60);
			me = new MidiEvent(mm,(long)1);
			t.add(me);
			
			mm = new ShortMessage();
			mm.setMessage(0x80,0x3C,0x40);
			me = new MidiEvent(mm,(long)121);
			t.add(me);*/
			
			
			
		}
		
		
		//set end of track
		mt = new MetaMessage();
        byte[] bet = {}; // empty array
		mt.setMessage(0x2F,bet,0);
		me = new MidiEvent(mt, (long)140);
		t.add(me);
		
		//write the MIDI sequence to a MIDI file
		File f = new File("/Users/jenny/Desktop/generatedFile.mid");
		MidiSystem.write(sequence,1,f);
		return sequence;
	}

	private static ArrayList<Integer> parseLrc(String str) throws IOException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		str.replace("[^a-zA-Z]", "");
		
		
		RecordManager recman = RecordManagerFactory.createRecordManager("Dictionary");
		long recid = recman.getNamedObject("Dictionary");
		if(recid!=0) {
          	hashtable  = HTree.load(recman, recid);
          	System.out.println("can open dictionary");
          }
          else {
          	hashtable = HTree.createInstance(recman);
          	recman.setNamedObject("Dictionary", hashtable.getRecid());
          }
		
		String[] strArray = str.split(" ");   //to be modified here
		
		for(String current: strArray) {
			WordStruct ws = (WordStruct) hashtable.get(current.toUpperCase());
			if(ws==null)
				result.add(new Integer(99));
			else {
				String stress = ws.stress;
				for(int i = 0;i < stress.length();++i) {
					result.add(Integer.parseInt(stress.substring(i, i+1)));
				}
			}
		}
		
		return result;
		
	}
	
	private static ArrayList<Integer> generateSequenceArray(String filename) throws IOException {
		// TODO Auto-generated method stub
		//generate sequence for lrc files
		ArrayList<Integer> lrc = new ArrayList<Integer>();
		ArrayList<Integer> melo = new ArrayList<Integer>();
		
		
		
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		//suppose given lrc sequence; change later
		/*Random random = new Random();
		for(int i = 0 ; i < 100; ++i) {
			lrc.add(new Integer(random.nextInt(8)));
		}*/
		
		ArrayList<Integer> lrcTemp = verify("/Users/jenny/Desktop/output/50_cent-in_da_club.txt");
		ArrayList<Integer> verifyArray = new ArrayList<Integer>();
		//verifyArray = parseLrc("apple is good");
		
		//calculate the relative value
		//System.out.println(lrcTemp.size());
		for(int i = 0; i < lrcTemp.size()-1;++i) {
			lrc.add(new Integer(lrcTemp.get(i+1) - lrcTemp.get(i)));
		}
		//System.out.println(lrc.size());
		//System.out.println(lrc);
	
		int startPos = 0;
		int sum = 0;
		int beatIndex=0;
		
		while(startPos < lrc.size() - MIN) {
			
			//int newPos = find(lrc,startPos);
			boolean find = false;
			for(int i = MAX; i >= MIN; i--) {
				
				ArrayList<Integer> searchResult;
				//System.out.print("test size " + i);
				ArrayList<Integer> temp = new ArrayList<Integer>();
				for(int j = startPos; j < startPos + i && j < lrc.size(); ++j) {
					temp.add(lrc.get(j));	
				}
				
				//System.out.println("target" + temp);
				//System.out.println("pool " + patternPools.get(i+1));
				
				if(patternPools.get(i+1).get(temp)!=null) {
					searchResult = patternPools.get(i+1).get(temp);
					startPos += i;
					//System.out.println("find" + searchResult +  " for" + temp);
					verifyArray.addAll(temp);
					find = true;
					sum+=i+1;
					/*for(int k=0;k<searchResult.size();++k) {  
						int tempNum = searchResult.get(k);
						searchResult.set(k,	tempNum + beatArray.get(beatIndex));
						beatIndex = (beatIndex+1) % Integer.parseInt(beatSecond);
					}*/
					result.addAll(searchResult);
					break;
				}
				else {
					//System.out.println("not found for pattern size " + i + temp);
					
				}
					
			}
			if(find == false) {
				//System.out.println("not found for all size");
				startPos++;
				//result.add(new Integer(0) + beatArray.get(beatIndex));
				//beatIndex = (beatIndex+1) % Integer.parseInt(beatSecond);
				result.add(new Integer(0));
				verifyArray.add(new Integer(0));
			}
		}
		//System.out.println("the original  array is "+ lrc);
		//System.out.println("the recombine array is " + verifyArray);
		//System.out.println("the genrated aray is" + result);
		//System.out.println(verifyArray.size());
		//System.out.println(lrc.size());
		//System.out.println("result size" + result.size());
		
		
		return result;
	}
	
	private static ArrayList<Integer> verify(String filename) throws FileNotFoundException {
		// TODO Auto-generated method stub
		ArrayList<Integer> result = new ArrayList<Integer>();
		Scanner s = new Scanner(new File(filename));
		String line;
		while(s.hasNextLine()) {
			
			line = s.nextLine();//System.out.println(line);
			int pos = line.indexOf(',');
			int nextPos = line.indexOf(',', pos+1);
			int first = Integer.parseInt(line.substring(pos+1,nextPos));//System.out.println("first" + line.substring(pos+1,nextPos));
			pos = line.lastIndexOf(',');//System.out.println("second" + line.substring(pos+1));
			int second = Integer.parseInt(line.substring(pos+1));
			//System.out.println(first+ " " + second);
			first = first * 3 + second;
			result.add(new Integer(first));
		}
		return result;
	}

	
	private static void singleTest() throws FileNotFoundException {
		// TODO Auto-generated method stub
		readInPatterns("/Users/jenny/Desktop/allPattern.txt");
		readInNewPatterns("/Users/jenny/Desktop/allPatternDuration.txt");
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
		//System.out.println("final");
		//System.out.println(patternPools.get(4));
		/*for(int i = 0 ; i <= 4; ++i) {
			System.out.println("size is " + i);
			Map<ArrayList<Integer>, ArrayList<Integer> > map = patternPools.get(i);
			System.out.println(map);
			System.out.println(map.size());
			
		}*/
	}
	
	private static void readInNewPatterns(String filename) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		Scanner s = new Scanner(new File(filename));
		String line;
		ArrayList<Integer> melo = new ArrayList<Integer>();
		ArrayList<Double> dur = new ArrayList<Double>();
		//SequencePair currentSeq = null;
		Map<ArrayList<Integer>, ArrayList<Double> > currentMap = new HashMap<ArrayList<Integer>, ArrayList<Double> > ();
		
		int size = MIN;
	
		
		while(s.hasNextLine()) {
			
			melo = new ArrayList<Integer>();
			dur = new ArrayList<Double>();
			
			
			line = s.nextLine();
			if(line.charAt(0)!='{') {
				if(line.indexOf("size:")!=-1) {
					int index = line.indexOf(':');
					size = Integer.parseInt(line.substring(index+1));
					//System.out.println("Currently Checking " + size);
					//System.out.println(currentMap);
					newPatternPools.add(currentMap);
					currentMap = new HashMap<ArrayList<Integer>, ArrayList<Double> > ();
					
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
				melo.add(new Integer(Integer.parseInt(first)));
				dur.add(new Double(Double.parseDouble(second)));
				line = line.substring(endIndex+1); 
			}
			
			//currentSeq = new SequencePair(lrc,melo);
			//System.out.println(currentSeq.lrcSeq);
			//System.out.println(currentSeq.meloSeq);
			
			//if use maps there will overlap
			currentMap.put(melo, dur);	
		}
		
		//test 
		//System.out.println("final");
		//System.out.println(patternPools.get(4));
		/*for(int i = 0 ; i <= MAX; ++i) {
			Map<ArrayList<Integer>, ArrayList<Double> > map = newPatternPools.get(i);
			System.out.println(map);
			System.out.println(map.size());
			
		}*/
	}
	
}
