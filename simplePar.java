class Corners extends RecursiveAction {
	float minLong2;
	float maxLong2;
	float minLat2;
	float maxLat2;
	int lo;
	int hi;
	
	Corners(int l, int h) {
		this.lo = l;
		this.hi = h;
	}

	void compute() {
		if (hi-lo <= THRESHOLD) {
			minLong2 = 180;
			maxLong2 = -180;
			maxLat2 = -180;
			minLat2 = 180;
			for (int i=lo; i<hi; i++) {
				float curLong = CD.data[i].longitude;
				float curLat = CD.data[i].latitude;
				if (curLat > maxLat2)
					maxLat2 = curLat;
				if (curLat < minLat2)
					minLat2 = curLat;
				if (curLong > maxLong2)
					maxLong2 = curLong;
				if (curLong < minLong2)
					minLong2 = curLong;
			}
		}
		else {
			Corners left = new Corners(lo, (lo+hi) / 2);
			Corners right = new Corners((lo+hi) / 2, hi);
			left.fork();
			right.compute();
			left.join();
			maxLat2 = Math.max(left.maxLat2, right.maxLat2);
			maxLong2 = Math.max(left.maxLong2, right.maxLong2);
			minLat2 = Math.min(left.minLat2, right.minLat2);
			minLong2 = Math.min(left.minLong2, right.minLong2);
		}
	}
}

class ComputePop extends RecursiveTask<Pair<Integer, Integer>> {
	
}

public class simplePar extends AllVersion {
	static int THRESHOLD = 500;
	ForkJoinPool fjp = new ForkJoinPool();


	float minLong;
	float maxLong;
	float minLat;
	float maxLat;

	float xWidth;
	float yWidth;

	float x0;
	float y0;

	int totalPop = 0;
	int dataSize;	

	public simplePar(CensusData d, int x, int y) {
		super(d,x,y);
		
		Corners corners = new Corners(0, d.data_size);
		fjp.invoke(corners);
		x0 = corners.minLong2;
		y0 = corners.minLat2;
		xWidth = (corners.maxLong2 - corners.minLong2) / x;
		yWidth = (corners.maxLat2 - corners.minLat2) / y;		
	}

	public Pair<Integer, Float> pairGen(int w, int s, int e, int n) {
		minLong = x0 + xWidth * (w - 1);
		maxLong = minLong + xWidth * (e - w + 1);
		minLat = y0 + yWidth * (s - 1);
		maxLat = minLat + yWidth * (n - s + 1);
		
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
