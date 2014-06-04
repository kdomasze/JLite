class rng 
{
	int randomize(int seed)
	{
		int randnum = 0;
		int somenum = 58903;
		while (randnum < seed)
		{
			randnum = somenum / (randnum + seed * 3) * seed;
		}
		return (randnum + 6);
	}
	
	void main(System x)
	{
		rng r = new rng();
		int seed = x.input();
		x.output(r.randomize(seed));
	}
}
