package IR.Flat;
import IR.FieldDescriptor;

public class FlatSetFieldNode extends FlatNode {
  TempDescriptor src;
  TempDescriptor dst;
  FieldDescriptor field;

  public FlatSetFieldNode(TempDescriptor dst, FieldDescriptor field, TempDescriptor src) {
    this.field=field;
    this.src=src;
    this.dst=dst;
  }

  public String toString() {
    return "FlatSetFieldNode_"+dst.toString()+"."+field.getSymbol()+"="+src.toString();
  }
}
