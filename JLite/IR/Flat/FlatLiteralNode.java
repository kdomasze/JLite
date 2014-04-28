package IR.Flat;
import IR.TypeDescriptor;

public class FlatLiteralNode extends FlatNode {
  Integer value;
  TypeDescriptor type;
  TempDescriptor dst;

  public FlatLiteralNode(TypeDescriptor type, Integer o, TempDescriptor dst) {
    this.type=type;
    value=o;
    this.dst=dst;
  }

  public String toString() {
    String str = "FlatLiteralNode_"+dst;
    str += "="+value.toString();
    return str;
  }
}
