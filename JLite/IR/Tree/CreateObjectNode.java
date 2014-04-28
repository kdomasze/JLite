package IR.Tree;
import java.util.Vector;
import IR.TypeDescriptor;
import IR.MethodDescriptor;
import IR.Tree.ExpressionNode;

public class CreateObjectNode extends ExpressionNode {
  TypeDescriptor td;
  
  public CreateObjectNode(TypeDescriptor type) {
    td=type;
  }

  public TypeDescriptor getType() {
    return td;
  }

  public String printNode(int indent) {
    String st="new "+td.toString()+"()";
    return st;
  }

  public int kind() {
    return Kind.CreateObjectNode;
  }
}

