package IR.Tree;

public class ReturnNode extends BlockStatementNode
{
	private ExpressionNode ReturnStatement;
	
	// constructor
	public ReturnNode(ExpressionNode rs)
	{
		ReturnStatement = rs;
	}
	
	// get method
	public ExpressionNode getReturnStatement()
	{
		return ReturnStatement;
	}
	
	public String toString()
	{
		return "[Return: " + ReturnStatement + "]";
	}
}
