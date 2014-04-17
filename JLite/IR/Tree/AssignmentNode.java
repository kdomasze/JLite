package IR.Tree;

public class AssignmentNode extends ExpressionNode
{
	private ExpressionNode leftHandSide;
	private ExpressionNode rightHandSide;
	
	public AssignmentNode(ExpressionNode left, ExpressionNode right)
	{
		leftHandSide = left;
		rightHandSide = right;
	}
	
	// get methods
	public ExpressionNode getLeftHandSide()
	{
		return leftHandSide;
	}
	
	public ExpressionNode getRightHandSide()
	{
		return rightHandSide;
	}
	
	public String toString()
	{
		return "[Assignment: " + leftHandSide + " = " + rightHandSide + "]";
	}
}
