package IR.Tree;

public class AssignmentNode extends ExpressionNode
{
	private ExpressionNode leftHandSide;
	private ExpressionNode rightHandSide;
	
	public AssignmentNode(ExpressionNode lhs, ExpressionNode rhs)
	{
		leftHandSide = lhs;
		rightHandSide = rhs;
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
}
