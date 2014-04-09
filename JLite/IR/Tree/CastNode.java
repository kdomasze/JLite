package IR.Tree;

public class CastNode extends ExpressionNode
{
	private TypeNode Type;
	private ExpressionNode expression;
	
	//Constructor
	CastNode(TypeNode t, ExpressionNode e)
	{
		Type = t;
		expression = e;
	}
	
	//Get Methods
	public TypeNode getType()
	{
		return Type;
	}
	
	public ExpressionNode getExpression()
	{
		return expression;
	}
}
