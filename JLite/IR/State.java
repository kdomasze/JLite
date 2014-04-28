package IR;
import IR.Tree.*;
import IR.*;
import Parse.*;

import java.util.*;

public class State {
  SymbolTable classes;
  Hashtable treemethodmap;
  public HashSet parsetrees;
  int numclasses;
  public String main;

  public State() {
    this.classes=new SymbolTable();
    this.treemethodmap=new Hashtable();
    this.parsetrees=new HashSet();
  }

  public static TypeDescriptor getTypeDescriptor(int t) {
    TypeDescriptor td=new TypeDescriptor(t);
    return td;
  }

  public static TypeDescriptor getTypeDescriptor(NameDescriptor n) {
    TypeDescriptor td=new TypeDescriptor(n);
    return td;
  }

  public static TypeDescriptor getTypeDescriptor(String n) {
    TypeDescriptor td=new TypeDescriptor(n);
    return td;
  }

  public void addClass(ClassDescriptor tdn) {
    if (classes.contains(tdn.getSymbol()))
      throw new Error("Class "+tdn.getSymbol()+" defined twice");
    classes.add(tdn);
    numclasses++;
  }

  public int numClasses() {
    return numclasses;
  }

  public BlockNode getMethodBody(MethodDescriptor md) {
    return (BlockNode)treemethodmap.get(md);
  }


  public SymbolTable getClassSymbolTable() {
    return classes;
  }

  public void addTreeCode(MethodDescriptor md, BlockNode bn) {
    treemethodmap.put(md,bn);
  }
}
