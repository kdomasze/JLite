package IR.Tree;

public class DeclarationNode extends BlockStatementNode
{
	private String name;
	private TypeNode type;
	private ExpressionNode initializer;
	
	// constructor
	public DeclarationNode(String name2, TypeNode t, ExpressionNode i)
	{
		name = name2;
		type = t;
		initializer = i;
	}
	
	// get methods
	public String getName()
	{
		return name;
	}
	
	public TypeNode getType()
	{
		return type;
	}
	
	public ExpressionNode getInitializer()
	{
		return initializer;
	}
	
	public String toString()
	{
		return "[Declaration: " + name + ": " + type + " -> " + initializer + "]";
	}
}