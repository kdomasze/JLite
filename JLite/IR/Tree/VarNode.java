package IR.Tree;

public class VarNode extends ExpressionNode
{
	private NameNode name;
	private LiteralNode literal;
	
	public VarNode(NameNode n, LiteralNode l)
	{
		name = n;
		literal = l;
	}
	
	public NameNode getName()
	{
		return name;
	}
	
	public LiteralNode getLiteral()
	{
		return literal;
	}
}
