package IR.Tree;

public class FieldAccessNode extends ExpressionNode
{
	private NameNode Name;
	private TypeNode Type;
	private ExpressionNode Expression;

	// Constructor
	public FieldAccessNode(NameNode N, TypeNode T, ExpressionNode E)
	{
		Name = N;
		Type = T;
		Expression = E;
	}

	// get methods
	public String getName()
	{
		return Name.getName();
	}

	public String getType()
	{
		return Type.getType();
	}

	public ExpressionNode getExpression()
	{
		return Expression;
	}
}
