package IR.Tree;

public class OpNode extends ExpressionNode
{
	private ExpressionNode Operand1;
	private ExpressionNode Operand2;
	private Operation Operator;

	// Constructor
	public OpNode(ExpressionNode left, ExpressionNode right, Operation operation)
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

	public Operation getOperator()
	{
		return Operator;
	}
	
	public String toString()
	{
		return "[Op: " + Operand1 + " " + Operator + " " + Operand2 + "]";
	}
}
