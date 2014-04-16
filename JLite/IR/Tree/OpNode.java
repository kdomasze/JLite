package IR.Tree;

public class OpNode extends ExpressionNode
{
	private TreeNode Operand1;
	private TreeNode Operand2;
	private TreeNode Operator;

	// Constructor
	public OpNode(TreeNode left, TreeNode right, TreeNode operation)
	{
		Operand1 = left;
		Operand2 = right;
		Operator = operation;
	}

	// get methods
	public TreeNode getOperand1()
	{
		return Operand1;
	}

	public TreeNode getOperand2()
	{
		return Operand2;
	}

	public TreeNode getOperator()
	{
		return Operator;
	}
	
	public String toString()
	{
		return "[Op: " + Operand1 + " " + Operator + " " + Operand2 + "]";
	}
}
