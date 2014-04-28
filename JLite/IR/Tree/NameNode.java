package IR.Tree;
import IR.NameDescriptor;
import IR.Descriptor;
import IR.VarDescriptor;
import IR.TypeDescriptor;
import IR.FieldDescriptor;
import IR.ClassDescriptor;

public class NameNode extends ExpressionNode {
  NameDescriptor name;
  Descriptor vd;
  FieldDescriptor fd;
  ExpressionNode en;
  ClassDescriptor cd;
  boolean issuper;

  public NameNode(NameDescriptor nd) {
    this.name=nd;
    this.vd=null;
    this.fd=null;
    this.cd = null;
    this.issuper= false;
  }
  
  public boolean isSuper() {
      return this.issuper;
  }
  
  public void setIsSuper() {
      this.issuper = true;
  }

  public ExpressionNode getExpression() {
    return en;
  }

  public ClassDescriptor getClassDesc() {
    return this.cd;
  }

  public void setClassDesc(ClassDescriptor cd) {
    this.cd = cd;
  }

  /* Gross hack */
  public void setExpression(ExpressionNode en) {
    this.en=en;
  }

  public void setVar(Descriptor vd) {
    this.vd=vd;
  }

  public void setField(FieldDescriptor fd) {
    this.fd=fd;
  }

  public FieldDescriptor getField() {
    return fd;
  }

  public VarDescriptor getVar() {
    return (VarDescriptor) vd;
  }

  public TypeDescriptor getType() {
    if (en!=null)
      return en.getType();
    else if (fd!=null) {
      return fd.getType();
    } else if(vd != null) {
      return ((VarDescriptor)vd).getType();
    }
    if(cd != null) {
      TypeDescriptor tp = new TypeDescriptor(cd);
      return tp;
    } else {
      return null;
    }

  }

  public TypeDescriptor getClassType() {
    if(cd != null) {
      TypeDescriptor tp = new TypeDescriptor(cd);
      return tp;
    } else
      return null;
  }

  public NameDescriptor getName() {
    return name;
  }

  public String printNode(int indent) {
    return name.toString();
  }

  public int kind() {
    return Kind.NameNode;
  }
}
