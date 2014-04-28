package IR;

/**
 * Descriptor
 *
 * represents a symbol in the language (var name, function name, etc).
 */

public class FieldDescriptor extends Descriptor {
  protected TypeDescriptor td;
  protected String identifier;
  private ClassDescriptor cn;

  public FieldDescriptor(TypeDescriptor t, String identifier) {
    super(identifier);
    this.td=t;
  }

  public ClassDescriptor getClassDescriptor() {
    return this.cn;
  }

  public void setClassDescriptor(ClassDescriptor cn) {
    this.cn = cn;
  }

  public TypeDescriptor getType() {
    return td;
  }

  public String toString() {
    return td.toString()+" "+getSymbol()+";";
  }

  public String toStringBrief() {
    return td.toPrettyString()+" "+getSymbol();
  }

  public String toPrettyStringBrief() {
    return td.toPrettyString()+" "+getSymbol();
  }
}
