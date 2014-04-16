package IR.Tree;

public class OpNode extends ExpressionNode
{
	private ExpressionNode Operand1;
	private ExpressionNode Operand2;
	private ExpressionNode Operator;

	// Constructor
	public OpNode(ExpressionNode O1, ExpressionNode O2, ExpressionNode Op)
	{
		Operand1 = O1;
		Operand2 = O2;
		Operator = Op;
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
