package IR;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Descriptor
 *
 * represents a symbol in the language (var name, function name, etc).
 */

public class TypeDescriptor extends Descriptor {
  public static final int INT=1;
  public static final int VOID=2;
  public static final int NULL=3;
  public static final int CLASS=4;

  private int type;
  ClassDescriptor class_desc;

  public boolean equals(Object o) {
    if (o instanceof TypeDescriptor) {
      TypeDescriptor t=(TypeDescriptor)o;
      if (t.type!=type)
        return false;
      if ((type==CLASS)&&(!t.getSymbol().equals(getSymbol())))
        return false;
      return true;
    }
    return false;
  }

  public int hashCode() {
    int hashcode=type;
    if (type==CLASS)
      hashcode^=getSymbol().hashCode();
    return hashcode;
  }

  public boolean isVoid() {
    return type==VOID;
  }

  public boolean isPtr() {
    return isClass()||isNull();
  }

  public void setClassDescriptor(ClassDescriptor cd) {
    class_desc=cd;
  }

  public boolean isInt() {
    return type==INT;
  }

  public boolean isNull() {
    return type==NULL;
  }

  public boolean isClass() {
    return type==CLASS;
  }

  public TypeDescriptor(NameDescriptor name) {
    super(name.toString());
    this.type=CLASS;
    this.class_desc=null;
  }

  public TypeDescriptor(String st) {
    super(st);
    this.type=CLASS;
    this.class_desc=null;
  }

  public ClassDescriptor getClassDesc() {
    return class_desc;
  }

  public TypeDescriptor(ClassDescriptor cd) {
    super(cd.getSymbol());
    this.type=CLASS;
    this.class_desc=cd;
  }

  public TypeDescriptor(int t) {
    super(decodeInt(t));
    this.type=t;
  }

  public String toString() {
    if (type==CLASS) {
      return name;
    } else
      return decodeInt(type);
  }

  public String toPrettyString() {
    String str=name;
    if (type!=CLASS) {
      str=decodeInt(type);
    }
    return str;
  }

  private static String decodeInt(int type) {
    if (type==INT)
      return "int";
    else if (type==VOID)
      return "void";
    else if (type==NULL)
      return "NULL";
    else throw new Error();
  }
}
