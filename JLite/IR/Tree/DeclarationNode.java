package IR.Tree;

public class DeclarationNode extends BlockStatementNode
{
	private NameNode name;
	private TypeNode type;
	private ExpressionNode initializer;
	
	// constructor
	public DeclarationNode(n, t, i)
	{
		name = n;
		type = t;
		initializer = i;
	}
	
	// get methods
	public NameNode getName()
	{
		return name;
	}
	
	public TypeNode getType()
	{
		return type;
	}
	
	public ExpressionNode getInitilizer()
	{
		return initializer;
	}
}