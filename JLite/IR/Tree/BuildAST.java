package IR.Tree;
import Parse.*;

public class BuildAST {
  public BuildAST() {

  }

  public TreeNode buildAST(ParseNode pn) {
    String label=pn.getLabel();
    ParseNodeVector pnv=pn.getChildren();
    if (label.equals("literal")) {
      ParseNode c=pn.getChild("integer");
      return new IntLit(((Integer)c.getLiteral()).intValue());
    } else if (label.equals("add")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.ADD));
    } else if (label.equals("sub")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.SUB));
    } else if (label.equals("mult")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.MULT));
    } else if (label.equals("div")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.DIV));
    } else if (label.equals("div")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.DIV));
    } else if (label.equals("not")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      return new OpNode(left,null,new Operation(Operation.NOT));
    } else if (label.equals("comp_lt")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.LT));
    } else if (label.equals("comp_gt")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.GT));
    } else if (label.equals("equal")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.EQ));
    } else if (label.equals("not_equal")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.NEQ));
    } else if (label.equals("bitwise_and")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.BIT_AND));
    } else if (label.equals("bitwise_or")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.BIT_OR));
    } else if (label.equals("bitwise_xor")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.BIT_XOR));
    } else if (label.equals("logical_and")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.LOG_AND));
    } else if (label.equals("logical_or")) {
      TreeNode left=buildAST(pnv.elementAt(0));
      TreeNode right=buildAST(pnv.elementAt(1));
      return new OpNode(left,right,new Operation(Operation.LOG_OR));
    } 
    else if(label.equals("name"))
    {
    	ParseNode c = pn.getFirstChild();
    	return new NameNode((String)pn.getLabel());
    }
    else if(label.equals("classbody"))
    {
    	
    }
    else if(label.equals("field_declaration"))
    {
    	
    }
    else if(label.equals("field"))
    {
    	
    }
    else if(label.equals("type"))
    {
    	
    }
    else if(label.equals("variables"))
    {
    	
    }
    else if(label.equals("variable_declarator"))
    {
    	
    }
    else if(label.equals("single"))
    {
    	
    }
    else if(label.equals("class_body_declaration_list"))
    {
    	
    }
    else if(label.equals("class_declaration"))
    {
    	
    }
    else if(label.equals("type_declaration_list"))
    {
    	
    }
    else if(label.equals("compilation_unit"))
    {
    	
    }
    else if(label.equals("method"))
    {
    	
    }
    else if(label.equals("method_declaration"))
    {
    	
    }
    else if(label.equals("method_header"))
    {
    	
    }
    else if(label.equals("returntype"))
    {
    	
    }
    else if(label.equals("identifier"))
    {
    	
    }
    else if(label.equals("parameters"))
    {
    	
    }
    else if(label.equals("formal_parameter_list"))
    {
    	
    }
    else if(label.equals("formal_parameter"))
    {
    	
    }
    else if(label.equals("body"))
    {
    	
    }
    else if(label.equals("block_statement_list"))
    {
    	
    }
    else if(label.equals("expression"))
    {
    	
    }
    else if(label.equals("assignment"))
    {
    	
    }
    else if(label.equals("args"))
    {
    	
    }
    else if(label.equals("return"))
    {
    	
    }
    else if(label.equals("method_declarator"))
    {
    	
    }
    else if(label.equals("local_variable_declaration"))
    {
    	
    }
    else if(label.equals("initializer"))
    {
    	
    }
    else if(label.equals("methodinvoke1"))
    {
    	
    }
    else if(label.equals("argument_list"))
    {
    	
    }
      else {
      throw new Error();
    }
  }


}
