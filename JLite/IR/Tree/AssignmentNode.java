package IR.Tree;
import IR.TypeDescriptor;

public class AssignmentNode extends ExpressionNode {
  ExpressionNode left;
  ExpressionNode right;

  public AssignmentNode(ExpressionNode l, ExpressionNode r) {
    left=l;
    right=r;
  }

  public ExpressionNode getDest() {
    return left;
  }

  public ExpressionNode getSrc() {
    return right;
  }

  public String printNode(int indent) {
    if (right==null)
      return left.printNode(indent)+" =";
    else
      return left.printNode(indent)+" = "+right.printNode(indent);
  }

  public TypeDescriptor getType() {
    return left.getType();
  }

  public int kind() {
    return Kind.AssignmentNode;
  }
}
