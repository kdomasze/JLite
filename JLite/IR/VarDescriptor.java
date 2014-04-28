package IR;
import IR.Tree.ExpressionNode;

/**
 * Descriptor
 *
 * represents a symbol in the language (var name, function name, etc).
 */

public class VarDescriptor extends Descriptor {
  protected TypeDescriptor td;
  protected String identifier;

  public VarDescriptor(TypeDescriptor t, String identifier) {
    super(identifier);
    this.td=t;
    this.identifier=identifier;
  }

  public String getName() {
    return identifier;
  }

  public TypeDescriptor getType() {
    return td;
  }

  public String toString() {
    return td.toString()+" "+identifier;
  }
}
