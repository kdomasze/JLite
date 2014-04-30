package IR.Tree;
import IR.*;
import java.util.*;
import Parse.*;


public class BuildIR {
  State state;
  public BuildIR(State state) {
    this.state=state;
  }
  public void buildtree(ParseNode pn) {
    parseFile(pn);
  }

  /** Parse the classes in this file */
  public void parseFile(ParseNode pn) {
    ParseNode tpn=pn.getChild("type_declaration_list");
    if (tpn!=null) {
      ParseNodeVector pnv=tpn.getChildren();
      for(int i=0; i<pnv.size(); i++) {
        ParseNode type_pn=pnv.elementAt(i);
        if (isEmpty(type_pn))         /* Skip the semicolon */
          continue;
        if (isNode(type_pn,"class_declaration")) {
          ClassDescriptor cn=parseTypeDecl(type_pn);
          state.addClass(cn);
        } else {
          throw new Error(type_pn.getLabel());
        }
      }
    }
  }

  public ClassDescriptor parseTypeDecl(ParseNode pn) {
    ClassDescriptor cn=new ClassDescriptor(pn.getChild("name").getTerminal());
    if (!isEmpty(pn.getChild("super").getTerminal())) {
      /* parse superclass name */
      ParseNode snn=pn.getChild("super").getChild("type").getChild("class").getChild("name");
      NameDescriptor nd=parseName(snn);
      cn.setSuper(nd.toString());
    }
    parseClassBody(cn, pn.getChild("classbody"));
    return cn;
  }

  private void parseClassBody(ClassDescriptor cn, ParseNode pn) {
    ParseNode decls=pn.getChild("class_body_declaration_list");
    if (decls!=null) {
      ParseNodeVector pnv=decls.getChildren();
      for(int i=0; i<pnv.size(); i++) {
        ParseNode decl=pnv.elementAt(i);

        if (isNode(decl, "field")) {
          parseFieldDecl(cn,decl.getChild("field_declaration"));
        } else if (isNode(decl, "method")) {
          parseMethodDecl(cn,decl.getChild("method_declaration"));
        } else if (isNode(decl,"member")) {
          parseClassMember(cn,decl);
        } else if (isNode(decl,"block")) {
        } else throw new Error();
      }
    }
  }
  
  private void parseClassMember(ClassDescriptor cn, ParseNode pn) {

    throw new Error();
  }

  private TypeDescriptor parseTypeDescriptor(ParseNode pn) {
    ParseNode tn=pn.getChild("type");

    String type_st=tn.getTerminal();
    if(type_st.equals("int")) {
      return state.getTypeDescriptor(TypeDescriptor.INT);
    } else if(type_st.equals("class")) {
      ParseNode nn=tn.getChild("class");
      return state.getTypeDescriptor(parseName(nn.getChild("name")));
    } else {
      throw new Error();
    }
  }

  private NameDescriptor parseName(ParseNode nn) {
    ParseNode base=nn.getChild("base");
    ParseNode id=nn.getChild("identifier");
    if (base==null)
      return new NameDescriptor(id.getTerminal());
    return new NameDescriptor(parseName(base.getChild("name")),id.getTerminal());
  }

  private void parseFieldDecl(ClassDescriptor cn,ParseNode pn) {
    ParseNode tn=pn.getChild("type");
    TypeDescriptor t=parseTypeDescriptor(tn);
    ParseNode vn=pn.getChild("variables");

    ParseNode vardecl=vn.getChild("variable_declarator");
    String identifier=vardecl.getChild("single").getTerminal();
    cn.addField(new FieldDescriptor(t, identifier));
  }

  private ExpressionNode parseExpression(ParseNode pn) {
    if (isNode(pn,"assignment"))
      return parseAssignmentExpression(pn);
    else if (isNode(pn,"bitwise_or")||isNode(pn,"bitwise_xor")||
             isNode(pn,"bitwise_and")||isNode(pn,"equal")||
             isNode(pn,"not_equal")||isNode(pn,"comp_lt")||
             isNode(pn,"comp_gt")||
             isNode(pn,"add")||isNode(pn,"mult")||
             isNode(pn,"sub")||isNode(pn,"div")) {
      ParseNodeVector pnv=pn.getChildren();
      ParseNode left=pnv.elementAt(0);
      ParseNode right=pnv.elementAt(1);
      Operation op=new Operation(pn.getLabel());
      return new OpNode(parseExpression(left),parseExpression(right),op);
    } else if (isNode(pn,"not")||
               isNode(pn,"comp")) {
      ParseNode left=pn.getFirstChild();
      Operation op=new Operation(pn.getLabel());
      return new OpNode(parseExpression(left),op);
    } else if (isNode(pn,"literal")) {
      String literaltype=pn.getTerminal();
      ParseNode literalnode=pn.getChild(literaltype);
      Object literal_obj=literalnode.getLiteral();
      return new LiteralNode(literaltype, literal_obj);
    } else if (isNode(pn,"createobject")) {
      TypeDescriptor td=parseTypeDescriptor(pn);
      CreateObjectNode con=new CreateObjectNode(td);
      return con;
    } else if (isNode(pn,"name")) {
      NameDescriptor nd=parseName(pn);
      return new NameNode(nd);
    } else if (isNode(pn,"this")) {
      NameDescriptor nd=new NameDescriptor("this");
      return new NameNode(nd);
    } else if (isNode(pn,"methodinvoke1")) {
      NameDescriptor nd=parseName(pn.getChild("name"));
      Vector args=parseArgumentList(pn);
      MethodInvokeNode min=new MethodInvokeNode(nd);
      for(int i=0; i<args.size(); i++) {
        min.addArgument((ExpressionNode)args.get(i));
      }
      return min;
    } else if (isNode(pn,"methodinvoke2")) {
      String methodid=pn.getChild("id").getTerminal();
      ExpressionNode exp=parseExpression(pn.getChild("base").getFirstChild());
      Vector args=parseArgumentList(pn);
      MethodInvokeNode min=new MethodInvokeNode(methodid,exp);
      for(int i=0; i<args.size(); i++) {
        min.addArgument((ExpressionNode)args.get(i));
      }
      return min;
    } else if (isNode(pn,"fieldaccess")) {
      ExpressionNode en=parseExpression(pn.getChild("base").getFirstChild());          String fieldname=pn.getChild("field").getTerminal();
      return new FieldAccessNode(en,fieldname);
    } else if (isNode(pn,"cast1")) {
      return new CastNode(parseTypeDescriptor(pn.getChild("type")),parseExpression(pn.getChild("exp").getFirstChild()));
    } else if (isNode(pn,"cast2")) {
      return new CastNode(parseExpression(pn.getChild("type").getFirstChild()),parseExpression(pn.getChild("exp").getFirstChild()));
    } else {
      System.out.println("---------------------");
      System.out.println(pn.PPrint(3,true));
      throw new Error();
    }
  }

  private Vector parseArgumentList(ParseNode pn) {
    Vector arglist=new Vector();
    ParseNode an=pn.getChild("argument_list");
    if (an==null)       /* No argument list */
      return arglist;
    ParseNodeVector anv=an.getChildren();
    for(int i=0; i<anv.size(); i++) {
      arglist.add(parseExpression(anv.elementAt(i)));
    }
    return arglist;
  }

  private ExpressionNode parseAssignmentExpression(ParseNode pn) {
    ParseNodeVector pnv=pn.getChild("args").getChildren();

    AssignmentNode an=new AssignmentNode(parseExpression(pnv.elementAt(0)),parseExpression(pnv.elementAt(1)));
    return an;
  }

  private void parseMethodDecl(ClassDescriptor cn, ParseNode pn) {
    ParseNode headern=pn.getChild("method_header");
    ParseNode bodyn=pn.getChild("body");
    MethodDescriptor md=parseMethodHeader(headern);
    BlockNode bn=parseBlock(bodyn);
    cn.addMethod(md);
    state.addTreeCode(md,bn);
  }

  public BlockNode parseBlock(ParseNode pn) {
    if (pn==null||isEmpty(pn.getTerminal()))
      return new BlockNode();
    if (pn.getChild("no_method_body")!=null) {
      BlockNode bn=new BlockNode();
      bn.setNoCode();
      return bn;
    }
    ParseNode bsn=pn.getChild("block_statement_list");
    return parseBlockHelper(bsn);
  }

  private BlockNode parseBlockHelper(ParseNode pn) {
    ParseNodeVector pnv=pn.getChildren();
    BlockNode bn=new BlockNode();
    for(int i=0; i<pnv.size(); i++) {
      Vector bsv=parseBlockStatement(pnv.elementAt(i));
      for(int j=0; j<bsv.size(); j++) {
	bn.addBlockStatement((BlockStatementNode)bsv.get(j));
      }
    }
    return bn;
  }

  public BlockNode parseSingleBlock(ParseNode pn) {
    BlockNode bn=new BlockNode();
    Vector bsv=parseBlockStatement(pn);
    for(int j=0; j<bsv.size(); j++) {
      bn.addBlockStatement((BlockStatementNode)bsv.get(j));
    }
    bn.setStyle(BlockNode.NOBRACES);
    return bn;
  }

  public Vector parseBlockStatement(ParseNode pn) {
    Vector blockstatements=new Vector();
    if (isNode(pn,"local_variable_declaration")) {
      TypeDescriptor t=parseTypeDescriptor(pn);
      ParseNode lvd=pn.getChild("variable_declarator");
      String identifier=lvd.getChild("single").getTerminal();
      ParseNode epn=lvd.getChild("initializer");
      ExpressionNode en=null;
      if (epn!=null)
        en=parseExpression(epn.getFirstChild());
      
      blockstatements.add(new DeclarationNode(new VarDescriptor(t, identifier),en));
    } else if (isNode(pn,"nop")) {
      /* Do Nothing */
    } else if (isNode(pn,"expression")) {
      blockstatements.add(new BlockExpressionNode(parseExpression(pn.getFirstChild())));
    } else if (isNode(pn,"ifstatement")) {
      blockstatements.add(new IfStatementNode(parseExpression(pn.getChild("condition").getFirstChild()),
                                              parseSingleBlock(pn.getChild("statement").getFirstChild()),
                                              pn.getChild("else_statement")!=null ? parseSingleBlock(pn.getChild("else_statement").getFirstChild()) : null));
    } else if (isNode(pn,"return")) {
      if (isEmpty(pn.getTerminal()))
        blockstatements.add(new ReturnNode());
      else {
        ExpressionNode en=parseExpression(pn.getFirstChild());
        blockstatements.add(new ReturnNode(en));
      }
    } else if (isNode(pn,"block_statement_list")) {
      BlockNode bn=parseBlockHelper(pn);
      blockstatements.add(new SubBlockNode(bn));
    } else if (isNode(pn,"empty")) {
      /* nop */
    } else if (isNode(pn,"statement_expression_list")) {
      ParseNodeVector pnv=pn.getChildren();
      BlockNode bn=new BlockNode();
      for(int i=0; i<pnv.size(); i++) {
        ExpressionNode en=parseExpression(pnv.elementAt(i));
        blockstatements.add(new BlockExpressionNode(en));
      }
      bn.setStyle(BlockNode.EXPRLIST);
    } else if (isNode(pn,"whilestatement")) {
      ExpressionNode condition=parseExpression(pn.getChild("condition").getFirstChild());
      BlockNode body=parseSingleBlock(pn.getChild("statement").getFirstChild());
      blockstatements.add(new LoopNode(condition,body));
    } else {
      System.out.println("---------------");
      System.out.println(pn.PPrint(3,true));
      throw new Error();
    }
    return blockstatements;
  }

  public MethodDescriptor parseMethodHeader(ParseNode pn) {
    ParseNode tn=pn.getChild("returntype");
    TypeDescriptor returntype;
    if (tn!=null)
      returntype=parseTypeDescriptor(tn);
    else
      returntype=new TypeDescriptor(TypeDescriptor.VOID);

    ParseNode pmd=pn.getChild("method_declarator");
    String name=pmd.getChild("name").getTerminal();
    MethodDescriptor md=new MethodDescriptor(returntype, name);

    ParseNode paramnode=pmd.getChild("parameters");
    parseParameterList(md,paramnode);
    return md;
  }

  public void parseParameterList(MethodDescriptor md, ParseNode pn) {
    ParseNode paramlist=pn.getChild("formal_parameter_list");
    if (paramlist==null)
      return;
    ParseNodeVector pnv=paramlist.getChildren();
    for(int i=0; i<pnv.size(); i++) {
      ParseNode paramn=pnv.elementAt(i);

      TypeDescriptor type=parseTypeDescriptor(paramn);
      
      ParseNode tmp=paramn;
      String paramname=tmp.getChild("single").getTerminal();
      
      md.addParameter(type, paramname);
    }
  }
  
  private boolean isNode(ParseNode pn, String label) {
    if (pn.getLabel().equals(label))
      return true;
    else return false;
  }

  private static boolean isEmpty(ParseNode pn) {
    if (pn.getLabel().equals("empty"))
      return true;
    else
      return false;
  }

  private static boolean isEmpty(String s) {
    if (s.equals("empty"))
      return true;
    else
      return false;
  }

  /** Throw an exception if something is unexpected */
  private void check(ParseNode pn, String label) {
    if (pn == null) {
      throw new Error(pn+ "IE: Expected '" + label + "', got null");
    }
    if (!pn.getLabel().equals(label)) {
      throw new Error(pn+ "IE: Expected '" + label + "', got '"+pn.getLabel()+"'");
    }
  }
}
