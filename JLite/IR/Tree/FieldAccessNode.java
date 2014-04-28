package IR.Tree;
import IR.FieldDescriptor;
import IR.TypeDescriptor;

public class FieldAccessNode extends ExpressionNode {
  ExpressionNode left;
  String fieldname;
  FieldDescriptor field;
  boolean issuper;

  public FieldAccessNode(ExpressionNode l, String field) {
    fieldname=field;
    left=l;
    this.issuper = false;
  }

  public void setField(FieldDescriptor fd) {
    field=fd;
  }

  public String getFieldName() {
    return fieldname;
  }
  
  public void setFieldName(String fieldname) {
      this.fieldname = fieldname;
  }

  public FieldDescriptor getField() {
    return field;
  }

  public ExpressionNode getExpression() {
    return left;
  }

  public void setExpression( ExpressionNode en ) {
	left = en;  
  }
  public String printNode(int indent) {
    return left.printNode(indent)+"."+fieldname;
  }

  public int kind() {
    return Kind.FieldAccessNode;
  }

  public TypeDescriptor getType() {
    return getField().getType();
  }
  
  public void setIsSuper() {
      this.issuper = true;
  }
  
  public boolean isSuper() {
      return issuper;
  }
}
