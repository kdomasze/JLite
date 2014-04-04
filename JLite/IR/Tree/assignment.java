package IR.Tree;

public class assignment extends expression
{
    String identifier;
    int literal;

	// Constructor
	public assignment(String i, int l)
	{
		identifier = i;
		literal = l;
	}
}
