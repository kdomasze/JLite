package IR.Flat;
import IR.TypeDescriptor;

public class FlatNew extends FlatNode {
  TempDescriptor dst;
  TypeDescriptor type;

  public FlatNew(TypeDescriptor type, TempDescriptor dst) {
    if (type==null)
      throw new Error();
    this.type=type;
    this.dst=dst;
  }

  public String toString() {
    String str = "FlatNew_"+dst.toString()+"= NEW "+type.toString();

    return str;
  }
}
