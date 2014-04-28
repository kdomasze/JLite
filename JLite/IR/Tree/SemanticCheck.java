package IR.Tree;

import java.util.*;
import IR.*;

public class SemanticCheck {
  State state;
  TypeUtil typeutil;

  public SemanticCheck(State state, TypeUtil tu) {
    this.state=state;
    this.typeutil=tu;
  }

  public void semanticCheck() {
    SymbolTable classtable=state.getClassSymbolTable();
    Iterator it=classtable.getDescriptorsIterator();
    // Do descriptors first
    while(it.hasNext()) {
      ClassDescriptor cd=(ClassDescriptor)it.next();

      if (cd.getSuper()!=null) {
        cd.setSuperDesc(typeutil.getClass(cd.getSuper()));
        cd.getFieldTable().setParent(cd.getSuperDesc().getFieldTable());
        cd.getMethodTable().setParent(cd.getSuperDesc().getMethodTable());
      }

      /* Check to see that fields are well typed */
      for(Iterator field_it=cd.getFields(); field_it.hasNext();) {
        FieldDescriptor fd=(FieldDescriptor)field_it.next();
        //System.out.println("Checking field: "+fd);
        checkField(cd,fd);
      }
      
      for(Iterator method_it=cd.getMethods(); method_it.hasNext();) {
        MethodDescriptor md=(MethodDescriptor)method_it.next();
        checkMethod(cd,md);
      }
    }
    
    it=classtable.getDescriptorsIterator();
    // Do descriptors first
    while(it.hasNext()) {
      ClassDescriptor cd=(ClassDescriptor)it.next();
      for(Iterator method_it=cd.getMethods(); method_it.hasNext();) {
        MethodDescriptor md=(MethodDescriptor)method_it.next();
        checkMethodBody(cd,md);
      }
    }
  }
  
  public void checkTypeDescriptor(TypeDescriptor td) {
    if (td.isInt())
      return;       /* Done */
    else if (td.isClass()) {
      String name=td.toString();
      ClassDescriptor field_cd=(ClassDescriptor)state.getClassSymbolTable().get(name);
      if (field_cd==null)
        throw new Error("Undefined class "+name);
      td.setClassDescriptor(field_cd);
      return;
    } else
      throw new Error();
  }

  public void checkField(ClassDescriptor cd, FieldDescriptor fd) {
    checkTypeDescriptor(fd.getType());
  }

  public void checkMethod(ClassDescriptor cd, MethodDescriptor md) {
    /* Check return type */
    if (!md.getReturnType().isVoid())
      checkTypeDescriptor(md.getReturnType());
    
    for(int i=0; i<md.numParameters(); i++) {
      TypeDescriptor param_type=md.getParamType(i);
      checkTypeDescriptor(param_type);
    }
    /* Link the naming environments */
    md.getParameterTable().setParent(cd.getFieldTable());
    md.setClassDesc(cd);
    VarDescriptor thisvd=new VarDescriptor(new TypeDescriptor(cd),"this");
    md.setThis(thisvd);
  }

  public void checkMethodBody(ClassDescriptor cd, MethodDescriptor md) {
    ClassDescriptor superdesc=cd.getSuperDesc();
    if (superdesc!=null) {
      Set possiblematches=superdesc.getMethodTable().getSet(md.getSymbol());
      for(Iterator methodit=possiblematches.iterator(); methodit.hasNext();) {
        MethodDescriptor matchmd=(MethodDescriptor)methodit.next();
      }
    }
    BlockNode bn=state.getMethodBody(md);
    checkBlockNode(md, md.getParameterTable(),bn);
  }

  public void checkBlockNode(Descriptor md, SymbolTable nametable, BlockNode bn) {
    /* Link in the naming environment */
    bn.getVarTable().setParent(nametable);
    for(int i=0; i<bn.size(); i++) {
      BlockStatementNode bsn=bn.get(i);
      checkBlockStatementNode(md, bn.getVarTable(),bsn);
    }
  }
  
  public void checkBlockStatementNode(Descriptor md, SymbolTable nametable, BlockStatementNode bsn) {
    switch(bsn.kind()) {
    case Kind.BlockExpressionNode:
      checkBlockExpressionNode(md, nametable,(BlockExpressionNode)bsn);
      return;

    case Kind.DeclarationNode:
      checkDeclarationNode(md, nametable, (DeclarationNode)bsn);
      return;

    case Kind.IfStatementNode:
      checkIfStatementNode(md, nametable, (IfStatementNode)bsn);
      return;

    case Kind.LoopNode:
      checkLoopNode(md, nametable, (LoopNode)bsn);
      return;

    case Kind.ReturnNode:
      checkReturnNode(md, nametable, (ReturnNode)bsn);
      return;

    case Kind.SubBlockNode:
      checkSubBlockNode(md, nametable, (SubBlockNode)bsn);
      return;
    }
    throw new Error();
  }

  void checkBlockExpressionNode(Descriptor md, SymbolTable nametable, BlockExpressionNode ben) {
    checkExpressionNode(md, nametable, ben.getExpression(), null);
  }

  void checkDeclarationNode(Descriptor md, SymbolTable nametable,  DeclarationNode dn) {
    VarDescriptor vd=dn.getVarDescriptor();
    checkTypeDescriptor(vd.getType());
    Descriptor d=nametable.get(vd.getSymbol());
    if ((d==null)||
        (d instanceof FieldDescriptor)) {
      nametable.add(vd);
    } else
      throw new Error(vd.getSymbol()+" in "+md+" defined a second time");
    if (dn.getExpression()!=null)
      checkExpressionNode(md, nametable, dn.getExpression(), vd.getType());
  }

  void checkSubBlockNode(Descriptor md, SymbolTable nametable, SubBlockNode sbn) {
    checkBlockNode(md, nametable, sbn.getBlockNode());
  }

  void checkReturnNode(Descriptor d, SymbolTable nametable, ReturnNode rn) {
    MethodDescriptor md=(MethodDescriptor)d;
    if (rn.getReturnExpression()!=null)
      if (md.getReturnType()==null)
        throw new Error("Constructor can't return something.");
      else if (md.getReturnType().isVoid())
        throw new Error(md+" is void");
      else
        checkExpressionNode(md, nametable, rn.getReturnExpression(), md.getReturnType());
    else
      if (md.getReturnType()!=null&&!md.getReturnType().isVoid())
        throw new Error("Need to return something for "+md);
  }

  void checkIfStatementNode(Descriptor md, SymbolTable nametable, IfStatementNode isn) {
    checkExpressionNode(md, nametable, isn.getCondition(), new TypeDescriptor(TypeDescriptor.INT));
    checkBlockNode(md, nametable, isn.getTrueBlock());
    if (isn.getFalseBlock()!=null)
      checkBlockNode(md, nametable, isn.getFalseBlock());
  }

  void checkExpressionNode(Descriptor md, SymbolTable nametable, ExpressionNode en, TypeDescriptor td) {
    switch(en.kind()) {
    case Kind.AssignmentNode:
      checkAssignmentNode(md,nametable,(AssignmentNode)en,td);
      return;

    case Kind.CastNode:
      checkCastNode(md,nametable,(CastNode)en,td);
      return;

    case Kind.CreateObjectNode:
      checkCreateObjectNode(md,nametable,(CreateObjectNode)en,td);
      return;

    case Kind.FieldAccessNode:
      checkFieldAccessNode(md,nametable,(FieldAccessNode)en,td);
      return;

    case Kind.LiteralNode:
      checkLiteralNode(md,nametable,(LiteralNode)en,td);
      return;

    case Kind.MethodInvokeNode:
      checkMethodInvokeNode(md,nametable,(MethodInvokeNode)en,td);
      return;

    case Kind.NameNode:
      checkNameNode(md,nametable,(NameNode)en,td);
      return;

    case Kind.OpNode:
      checkOpNode(md,nametable,(OpNode)en,td);
      return;
    }
    throw new Error();
  }

  void checkCastNode(Descriptor md, SymbolTable nametable, CastNode cn, TypeDescriptor td) {
    /* Get type descriptor */
    if (cn.getType()==null) {
      NameDescriptor typenamed=cn.getTypeName().getName();
      String typename=typenamed.toString();
      TypeDescriptor ntd=new TypeDescriptor(typeutil.getClass(typename));
      cn.setType(ntd);
    }

    /* Check the type descriptor */
    TypeDescriptor cast_type=cn.getType();
    checkTypeDescriptor(cast_type);

    /* Type check */
    if (td!=null) {
      if (!typeutil.isSuperorType(td,cast_type))
        throw new Error("Cast node returns "+cast_type+", but need "+td);
    }

    ExpressionNode en=cn.getExpression();
    checkExpressionNode(md, nametable, en, null);
    TypeDescriptor etd=en.getType();
    if (typeutil.isSuperorType(cast_type,etd))     /* Cast trivially succeeds */
      return;

    if (typeutil.isSuperorType(etd,cast_type))     /* Cast may succeed */
      return;
    if (typeutil.isCastable(etd, cast_type))
      return;

    /* Different branches */
    /* TODO: change if add interfaces */
    throw new Error("Cast will always fail\n"+cn.printNode(0));
  }

  void checkFieldAccessNode(Descriptor md, SymbolTable nametable, FieldAccessNode fan, TypeDescriptor td) {
    ExpressionNode left=fan.getExpression();
    checkExpressionNode(md,nametable,left,null);
    TypeDescriptor ltd=left.getType();
    String fieldname=fan.getFieldName();
    
    FieldDescriptor fd=(FieldDescriptor) ltd.getClassDesc().getFieldTable().get(fieldname);
    if (fd==null)
      throw new Error("Unknown field "+fieldname + " in "+fan.printNode(0)+" in "+md);
    fan.setField(fd);
    if (td!=null)
      if (!typeutil.isSuperorType(td,fan.getType()))
        throw new Error("Field node returns "+fan.getType()+", but need "+td);
  }

  void checkLiteralNode(Descriptor md, SymbolTable nametable, LiteralNode ln, TypeDescriptor td) {
    /* Resolve the type */
    Object o=ln.getValue();
    if (ln.getTypeString().equals("null")) {
      ln.setType(new TypeDescriptor(TypeDescriptor.NULL));
    } else if (o instanceof Integer) {
      ln.setType(new TypeDescriptor(TypeDescriptor.INT));
    }

    if (td!=null)
      if (!typeutil.isSuperorType(td,ln.getType()))
        throw new Error("Field node returns "+ln.getType()+", but need "+td);
  }

  void checkNameNode(Descriptor md, SymbolTable nametable, NameNode nn, TypeDescriptor td) {
    NameDescriptor nd=nn.getName();
    if (nd.getBase()!=null) {
      /* Big hack */
      /* Rewrite NameNode */
      ExpressionNode en=translateNameDescriptorintoExpression(nd);
      nn.setExpression(en);
      checkExpressionNode(md,nametable,en,td);
    } else {
      String varname=nd.toString();
      Descriptor d=(Descriptor)nametable.get(varname);
      if (d==null) {
        throw new Error("Name "+varname+" undefined in: "+md);
      }
      if (d instanceof VarDescriptor) {
        nn.setVar(d);
      } else if (d instanceof FieldDescriptor) {
        nn.setField((FieldDescriptor)d);
        nn.setVar((VarDescriptor)nametable.get("this"));        /* Need a pointer to this */
      } else throw new Error("Wrong type of descriptor");
      if (td!=null)
        if (!typeutil.isSuperorType(td,nn.getType()))
          throw new Error("Field node returns "+nn.getType()+", but need "+td);
    }
  }

  void checkAssignmentNode(Descriptor md, SymbolTable nametable, AssignmentNode an, TypeDescriptor td) {
    checkExpressionNode(md, nametable, an.getSrc(),td);
    //TODO: Need check on validity of operation here
    if (!((an.getDest() instanceof FieldAccessNode)||
          (an.getDest() instanceof NameNode)))
      throw new Error("Bad lside in "+an.printNode(0));
    checkExpressionNode(md, nametable, an.getDest(), null);
    
    if (!typeutil.isSuperorType(an.getDest().getType(),an.getSrc().getType())) {
      throw new Error("Type of rside ("+an.getSrc().getType()+") not compatible with type of lside ("+an.getDest().getType()+")"+an.printNode(0));
    }
  }

  void checkLoopNode(Descriptor md, SymbolTable nametable, LoopNode ln) {
    checkExpressionNode(md, nametable, ln.getCondition(), new TypeDescriptor(TypeDescriptor.INT));
    checkBlockNode(md, nametable, ln.getBody());
  }

  void checkCreateObjectNode(Descriptor md, SymbolTable nametable, CreateObjectNode con, TypeDescriptor td) {
    TypeDescriptor typetolookin=con.getType();
    checkTypeDescriptor(typetolookin);

    if (td!=null&&!typeutil.isSuperorType(td, typetolookin))
      throw new Error(typetolookin + " isn't a "+td);

    if ((!typetolookin.isClass()))
      throw new Error("Can't allocate primitive type:"+con.printNode(0));
  }

  /** Check to see if md1 is the same specificity as md2.*/

  boolean match(MethodDescriptor md1, MethodDescriptor md2) {
    /* Checks if md1 is more specific than md2 */
    if (md1.numParameters()!=md2.numParameters())
      throw new Error();
    for(int i=0; i<md1.numParameters(); i++) {
      if (!md2.getParamType(i).equals(md1.getParamType(i)))
        return false;
    }
    if (!md2.getReturnType().equals(md1.getReturnType()))
      return false;
    
    if (!md2.getClassDesc().equals(md1.getClassDesc()))
      return false;
    
    return true;
  }

  ExpressionNode translateNameDescriptorintoExpression(NameDescriptor nd) {
    String id=nd.getIdentifier();
    NameDescriptor base=nd.getBase();
    if (base==null)
      return new NameNode(nd);
    else
      return new FieldAccessNode(translateNameDescriptorintoExpression(base),id);
  }

  void checkMethodInvokeNode(Descriptor md, SymbolTable nametable, MethodInvokeNode min, TypeDescriptor td) {
    /*Typecheck subexpressions
       and get types for expressions*/

    TypeDescriptor[] tdarray=new TypeDescriptor[min.numArgs()];
    for(int i=0; i<min.numArgs(); i++) {
      ExpressionNode en=min.getArg(i);
      checkExpressionNode(md,nametable,en,null);
      tdarray[i]=en.getType();
    }
    TypeDescriptor typetolookin=null;
    if (min.getExpression()!=null) {
      checkExpressionNode(md,nametable,min.getExpression(),null);
      typetolookin=min.getExpression().getType();
    } else if (min.getBaseName()!=null) {
      String rootname=min.getBaseName().getRoot();
      if (nametable.get(rootname)!=null) {
        //we have an expression
        min.setExpression(translateNameDescriptorintoExpression(min.getBaseName()));
        checkExpressionNode(md, nametable, min.getExpression(), null);
        typetolookin=min.getExpression().getType();
      }
    } else {
      typetolookin=new TypeDescriptor(((MethodDescriptor)md).getClassDesc());
    }
    if (!typetolookin.isClass())
      throw new Error("Error with method call to "+min.getMethodName());
    ClassDescriptor classtolookin=typetolookin.getClassDesc();
    
    Set methoddescriptorset=classtolookin.getMethodTable().getSet(min.getMethodName());
    MethodDescriptor bestmd=null;
    NextMethod:
    for(Iterator methodit=methoddescriptorset.iterator(); methodit.hasNext();) {
      MethodDescriptor currmd=(MethodDescriptor)methodit.next();
      /* Need correct number of parameters */
      if (min.numArgs()!=currmd.numParameters())
        continue;
      for(int i=0; i<min.numArgs(); i++) {
        if (!typeutil.isSuperorType(currmd.getParamType(i),tdarray[i]))
          continue NextMethod;
      }
      /* Method okay so far */
      if (bestmd==null)
        bestmd=currmd;
      else {
        throw new Error("Multiple methods work -- no overloading allowed");
        /* Is this more specific than bestmd */
      }
    }
    if (bestmd==null)
      throw new Error("No method found for :"+min.printNode(0)+" in class: " + classtolookin+" in "+md);
    min.setMethod(bestmd);
    
    if ((td!=null)&&(min.getType()!=null)&&!typeutil.isSuperorType(td,  min.getType()))
      throw new Error(min.getType()+ " is not equal to or a subclass of "+td);
    /* Check whether we need to set this parameter to implied this */
    if (min.getExpression()==null) {
      ExpressionNode en=new NameNode(new NameDescriptor("this"));
      min.setExpression(en);
      checkExpressionNode(md, nametable, min.getExpression(), null);
    }
  }


  void checkOpNode(Descriptor md, SymbolTable nametable, OpNode on, TypeDescriptor td) {
    checkExpressionNode(md, nametable, on.getLeft(), null);
    if (on.getRight()!=null)
      checkExpressionNode(md, nametable, on.getRight(), null);
    TypeDescriptor ltd=on.getLeft().getType();
    TypeDescriptor rtd=on.getRight()!=null ? on.getRight().getType() : null;
    TypeDescriptor lefttype=null;
    TypeDescriptor righttype=null;
    Operation op=on.getOp();

    switch(op.getOp()) {
    case Operation.LOGIC_NOT:
    case Operation.BIT_OR:
    case Operation.BIT_XOR:
    case Operation.BIT_AND:
    case Operation.EQUAL:
    case Operation.NOTEQUAL:
    case Operation.ADD:
    case Operation.LT:
    case Operation.GT:
    case Operation.SUB:
    case Operation.MULT:
    case Operation.DIV:
      righttype=lefttype=new TypeDescriptor(TypeDescriptor.INT);
      on.setLeftType(lefttype);
      on.setRightType(righttype);
      on.setType(new TypeDescriptor(TypeDescriptor.INT));
      break;

    default:
      throw new Error(op.toString());
    }
    
    if (td!=null)
      if (!typeutil.isSuperorType(td, on.getType())) {
        System.out.println(td);
        System.out.println(on.getType());
        throw new Error("Type of rside not compatible with type of lside"+on.printNode(0));
      }
  }
}
