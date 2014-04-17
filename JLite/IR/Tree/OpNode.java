package IR.Tree;

public class OpNode extends ExpressionNode
{
	private ExpressionNode Operand1;
	private ExpressionNode Operand2;
	private ExpressionNode Operator;

	// Constructor
	public OpNode(ExpressionNode left, ExpressionNode right, ExpressionNode operation)
	{
		Operand1 = left;
		Operand2 = right;
		Operator = operation;
	}

	// get methods
	public ExpressionNode getOperand1()
	{
		return Operand1;
	}

	public ExpressionNode getOperand2()
	{
		return Operand2;
	}

	public ExpressionNode getOperator()
	{
		return Operator;
	}
	
	public String toString()
	{
		return "[Op: " + Operand1 + " " + Operator + " " + Operand2 + "]";
	}
}
