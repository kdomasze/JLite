package IR.Flat;
import IR.*;

public class TempDescriptor extends Descriptor {
  TypeDescriptor type;

  public TempDescriptor(String name, TypeDescriptor td) {
    super(name);
    type=td;
  }

  public TypeDescriptor getType() {
    return type;
  }
}
