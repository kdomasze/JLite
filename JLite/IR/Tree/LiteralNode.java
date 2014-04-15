package IR.Tree;

public class LiteralNode extends ExpressionNode
{
	private int literal;
	
	//Constructor
	public LiteralNode(int l)
	{
		literal = l;
	}
	
	//get
	public int getLiteral()
	{
		return literal;
	}
}
