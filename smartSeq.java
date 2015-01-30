public class smartSeq extends AllVersion {
	float minLong;
	float maxLong;
	float minLat;
	float maxLat;

	float xWidth;
	float yWidth;

	int totalPop = 0;
	int dataSize;	

	int[][] stepOne;
	int[][] stepTwo;

	public smartSeq(CensusData d, int x, int y) {
		super(d,x,y);
		
		minLong = 180;
		maxLong = -180;
		maxLat = -180;
		minLat = 180;

		dataSize = CD.data_size;
	
		for (int i=0;i<dataSize;i++) {
			totalPop += CD.data[i].population;
			float curLong = CD.data[i].longitude;
			float curLat = CD.data[i].latitude;
			if (curLat > maxLat)
				maxLat = curLat;
			if (curLat < minLat)
				minLat = curLat;
			if (curLong > maxLong)
				maxLong = curLong;
			if (curLong < minLong)
				minLong = curLong;
		}

		xWidth = (maxLong - minLong) / x;
		yWidth = (maxLat - minLat) / y;

		/*additional preprocess*/
		stepOne = new int[y][x];
		for (int i=0; i<dataSize; i++) {
			float curLong = CD.data[i].longitude;
			float curLat = CD.data[i].latitude;
			int tempPop = CD.data[i].population;
			int xi = Math.round((curLong - minLong) / xWidth) - 1;
			int yi = Math.round((maxLat - curLat) / yWidth) - 1;
			
			stepOne[yi][xi] += tempPop;
		}
		
		stepTwo = new int[y][x];
		for (int i=0; i<y; i++) {
			for (int j=0; j<x; j++) {
				if (i == 0 && j == 0)
					stepTwo[i][j] = stepOne[i][j];
				if (i == 0 && j > 0)
					stepTwo[i][j] = stepTwo[i][j-1] + stepOne[i][j];
				if (i > 0 && j == 0)
					stepTwo[i][j] = stepTwo[i-1][j] + stepOne[i][j];
				if (i > 0 && j > 0)
					stepTwo[i][j] = stepTwo[i-1][j] - stepTwo[i-1][j-1] + stepTwo[i][j-1] + stepOne[i][j];
			}
		}				
	}

	public Pair<Integer, Float> pairGen(int w, int s, int e, int n) {
		
		int queryRectPop = stepTwo[y-s][e-1] - stepTwo[y-n-1][e-1] - stepTwo[y-s][w-2] + stepTwo[y-n-1][w-2];
	
		return new Pair<Integer, Float>(queryRectPop, (float) 100 * queryRectPop / totalPop);						
	}
}
