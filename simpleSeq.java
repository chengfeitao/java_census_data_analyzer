public class simpleSeq extends AllVersion {
	float minLong;
	float maxLong;
	float minLat;
	float maxLat;

	float xWidth;
	float yWidth;

	int totalPop = 0;
	int dataSize;	

	public simpleSeq(CensusData d, int x, int y) {
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
	}

	public Pair<Integer, Float> pairGen(int w, int s, int e, int n) {
		float queryRectMinLong = minLong + xWidth * (w - 1);
		float queryRectMaxLong = queryRectMinLong + xWidth * (e - w + 1);
		float queryRectMinLat = minLat + yWidth * (s - 1);
		float queryRectMaxLat = queryRectMinLat + yWidth * (n - s + 1);
		
		int queryRectPop = 0;
	
		for (int i=0; i<dataSize;i++) {
			int groupPop = CD.data[i].population;
			float groupLong = CD.data[i].longitude;
			float groupLat = CD.data[i].latitude;

			if (queryRectMinLong<=groupLong && groupLong<=queryRectMaxLong && queryRectMinLat<=groupLat && groupLat<=queryRectMaxLat)
				queryRectPop += groupPop;
			
		}
	
	
		return new Pair<Integer, Float>(queryRectPop, (float) 100 * queryRectPop / totalPop);						
	}
}
