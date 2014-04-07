package IR.Tree;

public class assignment extends expression
{
    String identifier;
    expression Expression;

	// Constructor
	public assignment(String i, expression e)
	{
		identifier = i;
		Expression = e;
	}
}
