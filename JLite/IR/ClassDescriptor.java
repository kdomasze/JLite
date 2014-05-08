package IR;
import java.util.*;
import IR.Tree.*;

public class ClassDescriptor extends Descriptor {
  String superclass;
  ClassDescriptor superdesc;
  String classname;

  SymbolTable fields;
  Vector fieldvec;
  SymbolTable flags;
  SymbolTable methods;

  public ClassDescriptor(String classname) {
    this("", classname);
  }

  public ClassDescriptor(String packagename, String classname) {
    //make the name canonical by class file path (i.e. package)
    super(classname);
    this.classname=classname;
    superclass=null;
    fields=new SymbolTable();
    fieldvec=new Vector();
    methods=new SymbolTable();
  }

  public Iterator getMethods() {
    return methods.getDescriptorsIterator();
  }

  public Iterator getFields() {
    return fields.getDescriptorsIterator();
  }

  public SymbolTable getFieldTable() {
    return fields;
  }

  public Vector getFieldVec() {
    return fieldvec;
  }

  public String getClassName() {
    return classname;
  }

  public SymbolTable getMethodTable() {
    return methods;
  }

  public String printTree(State state) {
    int indent;
    String st="class "+getSymbol();
    if (superclass!=null)
      st+="extends "+superclass.toString();
    st+=" {\n";
    indent=TreeNode.INDENT;
    
    for(Iterator it=getFields(); it.hasNext(); ) {
      FieldDescriptor fd=(FieldDescriptor)it.next();
      st+=TreeNode.printSpace(indent)+fd.toString()+"\n";
    }

    for(Iterator it=getMethods(); it.hasNext(); ) {
      MethodDescriptor md=(MethodDescriptor)it.next();
      st+=TreeNode.printSpace(indent)+md.toString()+" ";
      BlockNode bn=state.getMethodBody(md);
      st+=bn.printNode(indent)+"\n\n";
    }
    st+="}\n";
    return st;
  }

  public void addField(FieldDescriptor fd) {
    if (fields.contains(fd.getSymbol()))
      throw new Error(fd.getSymbol()+" already defined");
    fields.add(fd);
    fieldvec.add(fd);
    fd.setClassDescriptor(this);
  }

  public void addMethod(MethodDescriptor md) {
    methods.add(md);
  }

  public void setSuper(String superclass) {
    this.superclass=superclass;
  }

  public ClassDescriptor getSuperDesc() {
    return superdesc;
  }

  public void setSuperDesc(ClassDescriptor scd) {
    this.superdesc=scd;
  }

  public String getSuper() {
    return superclass;
  }
  
  public String toString()
  {
	  return "Methods: " + methods;
  }
}
