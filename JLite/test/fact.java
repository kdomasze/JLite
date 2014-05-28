class fact {

	int n;

	void setNumber(int num) {
		n = num;
	}

	int factRec(int num) {
		if (num == 1) {
			return 1;
		}
		return (factRec(num-1) * num);
	}

	int factIter() {
		int i;
		int f;

		i=1;
		f=1;

		while (i < (n +1)) {
			f = f*i;
			i = i +1;
		}

		return f;
	}

	void main(System x) {
		fact f=new fact();
		int i = x.input();
		f.setNumber(i);
		x.output(f.factIter());
		
	}

}

class factorial
{
	int x;
}
