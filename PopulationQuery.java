
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

//TODO: You need to make this class implement the PopQueryAPI interface
public class PopulationQuery implements PopQueryAPI/* implements PopQueryAPI */ {
	

	// next four constants are relevant to parsing
	public static final int TOKENS_PER_LINE  = 7;
	public static final int POPULATION_INDEX = 4; // zero-based indices
	public static final int LATITUDE_INDEX   = 5;
	public static final int LONGITUDE_INDEX  = 6;

	// parse the input file into a large array held in a CensusData object
    public static CensusData parse(String filename) {
	CensusData result = new CensusData();
	
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filename));
            
            // Skip the first line of the file
            // After that each line has 7 comma-separated numbers (see constants above)
            // We want to skip the first 4, the 5th is the population (an int)
            // and the 6th and 7th are latitude and longitude (floats)
            // If the population is 0, then the line has latitude and longitude of +.,-.
            // which cannot be parsed as floats, so that's a special case
            //   (we could fix this, but noisy data is a fact of life, more fun
            //    to process the real data as provided by the government)
            
            String oneLine = fileIn.readLine(); // skip the first line

            // read each subsequent line and add relevant data to a big array
            while ((oneLine = fileIn.readLine()) != null) {
                String[] tokens = oneLine.split(",");
                if(tokens.length != TOKENS_PER_LINE)
                	throw new NumberFormatException();
                int population = Integer.parseInt(tokens[POPULATION_INDEX]);
                if(population != 0)
                	result.add(population,
                			   Float.parseFloat(tokens[LATITUDE_INDEX]),
                		       Float.parseFloat(tokens[LONGITUDE_INDEX]));
            }

            fileIn.close();
        } catch(IOException ioe) {
            System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
        } catch(NumberFormatException nfe) {
            System.err.println(nfe.toString());
            System.err.println("Error in file format");
            System.exit(1);
        }
        return result;
    }
	
	private static AllVersion preprocessResult;


	public void preprocess(String filename, int x, int y, int versionNum) {
		CensusData CD = parse(filename);
		if (versionNum == 1)
			preprocessResult = new simpleSeq(CD, x, y);
		if (versionNum == 3)
			preprocessResult = new smartSeq(CD, x, y);
		if (versionNum == 2)
			preprocessResult = new simplePar(CD, x, y);
		
	}

	public Pair<Integer, Float> singleInteraction(int w, int s, int e, int n) {
		Pair<Integer, Float> pairResult = preprocessResult.pairGen(w,s,e,n);		
		return pairResult;
		  
	}

	

/*	public static void main(String[] args) {
		int b = Integer.parseInt(args[1]);
		int c = Integer.parseInt(args[2]);
		int d = Integer.parseInt(args[3]);
		int f = Integer.parseInt(args[4]);
		int g = Integer.parseInt(args[5]);
		int h = Integer.parseInt(args[6]);
		int j = Integer.parseInt(args[7]);
		preprocess(args[0], b, c, d);
		Pair<Integer, Float> a = singleInteraction(f, g, h, j);
		System.out.println(a.fst());
		System.out.println(a.snd());
	}
*/
}
