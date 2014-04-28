package IR;
import java.util.*;
import IR.Tree.*;
import java.io.File;
import Main.Main;
import Parse.*;

public class TypeUtil {
  State state;
  BuildIR bir;

  public TypeUtil(State state, BuildIR bir) {
    this.state=state;
    this.bir=bir;
  }

  public ClassDescriptor getClass(String classname) {
    ClassDescriptor cd=(ClassDescriptor)state.getClassSymbolTable().get(classname);
    return cd;
  }

  public ClassDescriptor getMainClass() {
    return getClass(state.main);
  }

  public MethodDescriptor getMain() {
    ClassDescriptor cd=getMainClass();
    Set mainset=cd.getMethodTable().getSet("main");
    if (mainset.size()!=1)
      throw new Error("main method ambiguous or missing");
    return (MethodDescriptor) mainset.iterator().next();
  }


  public boolean isCastable(TypeDescriptor original, TypeDescriptor casttype) {
    return false;
  }

  public boolean isSuperorType(TypeDescriptor possiblesuper, TypeDescriptor cd2) {
    //Matching type are always okay
    if (possiblesuper.equals(cd2))
      return true;

    if (possiblesuper.isClass()&&
        cd2.isClass())
      return isSuperorType(possiblesuper.getClassDesc(), cd2.getClassDesc());
    else if (possiblesuper.isClass()&&
             cd2.isNull())
      return true;
    else if (possiblesuper.isNull())
      throw new Error();       //not sure when this case would occur
    else if (possiblesuper.isInt() && cd2.isInt()) {
      return true;
    } else if (possiblesuper.isInt()&&
               cd2.isPtr())
      return false;
    else if (cd2.isInt()&&
             possiblesuper.isPtr())
      return false;
    else
      throw new Error("Case not handled:"+possiblesuper+" "+cd2);
  }


  public boolean isSuperorType(ClassDescriptor possiblesuper, ClassDescriptor cd2) {
    if (possiblesuper==cd2)
      return true;
    else
      return isSuper(possiblesuper, cd2);
  }

  private boolean isSuper(ClassDescriptor possiblesuper, ClassDescriptor cd2) {
    while(cd2!=null) {
      cd2=cd2.getSuperDesc();
      if (cd2==possiblesuper)
        return true;
    }
    return false;
  }
}
