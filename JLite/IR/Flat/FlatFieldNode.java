package IR.Flat;
import IR.FieldDescriptor;

public class FlatFieldNode extends FlatNode {
  TempDescriptor src;
  TempDescriptor dst;
  FieldDescriptor field;

  public FlatFieldNode(FieldDescriptor field, TempDescriptor src, TempDescriptor dst) {
    this.field=field;
    this.src=src;
    this.dst=dst;
  }

  public String toString() {
    return "FlatFieldNode_"+dst.toString()+"="+src.toString()+"."+field.getSymbol();
  }
}
