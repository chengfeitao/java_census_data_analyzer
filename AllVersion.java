public abstract class AllVersion {
	CensusData CD;
	int x, y;

	public AllVersion(CensusData CD, int x, int y) {
		this.CD = CD;
		this.x = x;
		this.y = y;
	}

	public abstract Pair<Integer, Float> pairGen(int w, int s, int e, int n);
}
