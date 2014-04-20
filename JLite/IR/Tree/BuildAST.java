package IR.Tree;

import java.util.Vector;

import IR.*;
import Parse.ParseNode;
import Parse.ParseNodeVector;

public class BuildAST
{
	Program program;

	// constructor
	public BuildAST()
	{
		program = new Program();
	}

	// build the AST tree for the program
	public Program parse(ParseNode pn)
	{
		ParseNodeVector pnv = pn.getFirstChild().getChildren();
		for(int i = 0; i < pnv.size(); i++)
		{
			parseClassDeclaration(pnv.elementAt(i));
		}

		return program;
	}

	// parses starting from "class_declaration"
	public void parseClassDeclaration(ParseNode node)
	{
		NameNode name = parseNameNode(node.getChild("name"));
		NameNode superClass = parseSuperClass(node.getChild("super"));
		
		ClassDescriptor Class = new ClassDescriptor(name.getName(), null, superClass.getName());
 
		parseClassBody(Class, node.getChild("classbody"));
		
		program.addClass(Class);
	}
	
	// parses starting from "name" or "single"
	public NameNode parseNameNode(ParseNode node)
	{
		if(node.getChildren().size()==0)
		{
			return new NameNode(node.getLabel());
		}
		else
		{
			return parseNameNode(node.getFirstChild());
		}
	}
	
	// parses starting from "super"
	public NameNode parseSuperClass(ParseNode node)
	{
		return new NameNode(node.getFirstChild().getLabel());
	}
	
	// parses starting from "classbody"
	public void parseClassBody(ClassDescriptor Class, ParseNode node)
	{
		ParseNode pn = node.getFirstChild();
		ParseNodeVector pnv = pn.getChildren();
		
		for(int i = 0; i < pnv.size(); i++)
		{
			if(pnv.elementAt(i).getLabel().equals("field"))
			{
				FieldDescriptor f = ParseField(pnv.elementAt(i));
				Class.addFieldDescriptor(f);
			}
			else if(pnv.elementAt(i).getLabel().equals("method"))
			{
				MethodDescriptor m = parseMethod(pnv.elementAt(i));
				Class.addMethodDescriptor(m);
			}
		}
	}
	
	// parses starting from "field"
	public FieldDescriptor ParseField(ParseNode node)
	{
		ParseNode pn = node.getFirstChild();
		TypeNode typeNode = parseTypeNode(pn.getChild("type"));
		VarNode varNode = parseVarNode(pn.getChild("variables"));
		
		TypeDescriptor type = new TypeDescriptor(null, typeNode);
		
		return new FieldDescriptor(varNode.getName().getName(), type);
	}
	
	// parses starting from "type"
	public TypeNode parseTypeNode(ParseNode node)
	{
		if(node.getChildren().size() == 0)
		{
			return new TypeNode(node.getLabel());
		}
		else
		{
			return parseTypeNode(node.getFirstChild());
		}
		/*ParseNode pn = node.getFirstChild();
		
		return new TypeNode(pn.getFirstChild().getLabel());*/
	}
	
	// parses starting from "variables"
	public VarNode parseVarNode(ParseNode node)
	{
		if(node.getLabel().equals("variables")) // for fields
		{
			ParseNode pn = node.getFirstChild().getFirstChild();
			NameNode name = parseNameNode(pn);
			
			return new VarNode(name, null);
		}
		
		return new VarNode(null, null);
	}
	
	// parses starting from "method"
	public MethodDescriptor parseMethod(ParseNode node)
	{
		ParseNode pn = node.getFirstChild();
		String s = null;
		TypeNode typeNode = new TypeNode("void");
		BlockNode blockNode;
		TypeDescriptor test = new TypeDescriptor(s,typeNode);
		MethodDescriptor method = null;
		ParseNode mhn = pn.getChild("method_header");
		ParseNodeVector pnv = mhn.getChildren();
		ParseNode bn = pn.getChild("body");
		
		for(int i = 0; i < pnv.size(); i++)
		{
			if(pnv.elementAt(i).getLabel().equals("returntype"))
			{
				typeNode = parseTypeNode(pnv.elementAt(i));
				//typeNode = new TypeNode(pnv.elementAt(i).getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getLabel());
			}
			else if(pnv.elementAt(i).getLabel().equals("method_declarator"))
			{
				s = pnv.elementAt(i).getChild("name").getFirstChild().getLabel();
				TypeDescriptor type = new TypeDescriptor(s, typeNode);
				method = new MethodDescriptor(s,type,null);
				ParseNode parn = pnv.elementAt(i).getChild("parameters");
				ParseNodeVector pnc = parn.getFirstChild().getChildren();
				for(int j = 0; j < pnc.size(); j++)
				{
					FieldDescriptor field = parseParameters(pnc.elementAt(j));
					method.addParameter(field);
				}
			}
		}
		TreeNode tnode = parseBlockNode(bn);
		method.setASTTree(tnode);
		return method;
	}
	
	// parses starting from "parameters"
	public FieldDescriptor parseParameters(ParseNode node)
	{
		ParseNode pn = node.getChild("type");
		TypeNode typeNode =  parseTypeNode(node);
		
		TypeDescriptor type = new TypeDescriptor(null, typeNode);
		
		return new FieldDescriptor(node.getChild("single").getFirstChild().getLabel(), type);
	}
	
	// parses starting from " "
	public BlockNode parseBlockNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getFirstChild().getChildren();
		BlockNode blockNode = new BlockNode();
		for(int i = 0; i < pnv.size(); i++)
		{
			BlockStatementNode s = parseBlockStatementNode(pnv.elementAt(i));
			blockNode.addblockStatement(s);
		}

		return blockNode;
	}
	
	// parses starting from " "
	public 	BlockStatementNode parseBlockStatementNode(ParseNode node)
	{
		if(node.getLabel().equals("type"))
		{
			TypeNode typeNode = parseTypeNode(node);
			return typeNode;
		}
		else if(node.getLabel().equals("ifstatement"))
		{
			IfStatementNode ifstatementNode = parseIfStatementNode(node);
			return ifstatementNode;
		}
		else if(node.getLabel().equals("whilestatement"))
		{
			WhileStatementNode whilestatementNode = parseWhileStatementNode(node);
			return whilestatementNode;
		}
		else if(node.getLabel().equals("assignment"))
		{
			AssignmentNode assignmentNode = parseAssignmentNode(node);
			return assignmentNode;
		}
		else if(node.getLabel().equals("expression"))
		{
			ExpressionNode expressionNode = parseExpressionNode(node);
			return expressionNode;
		}
		else if(node.getLabel().equals("return"))
		{
			ReturnNode returnNode = parseReturnNode(node);
			return returnNode;
		}
		else if(node.getLabel().equals("variable_declarator"))
		{
			NameNode nameNode = parseNameNode(node.getChild("single"));
			ExpressionNode expressionNode = parseExpressionNode(node.getChild("initializer").getFirstChild());
			return new AssignmentNode(nameNode, expressionNode);

		}
		else if(node.getLabel().equals("local_variable_declaration"))
		{
			NameNode nameNode = parseNameNode(node.getChild("variable_declarator").getChild("single"));
			TypeNode typeNode = parseTypeNode(node.getChild("type"));
			ExpressionNode expressionNode = parseExpressionNode(node.getChild("variable_declarator").getChild("initializer").getFirstChild());
			return new DeclarationNode(nameNode.getName(), typeNode, expressionNode);
		}
		throw new Error();

	}
	
	// parses starting from "ifstatement"
	public IfStatementNode parseIfStatementNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getChildren();
		OpNode opNode = parseOpNode(pnv.elementAt(0).getFirstChild());
		BlockNode blockNode = parseBlockNode(pnv.elementAt(1).getChild("block_statement_list"));

		return new IfStatementNode(opNode, blockNode);
	}

	// parses starting from "whilestatement"
	public WhileStatementNode parseWhileStatementNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getChildren();

		OpNode opNode = parseOpNode(pnv.elementAt(0).getFirstChild());
		BlockNode blockNode = parseBlockNode(pnv.elementAt(1).getChild("block_statement_list"));

		return new WhileStatementNode(opNode, blockNode);
	}
	
	// parses starting from " "
	public AssignmentNode parseAssignmentNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getFirstChild().getChildren();
		
		ExpressionNode lhs = parseExpressionNode(pnv.elementAt(0));
		ExpressionNode rhs = parseExpressionNode(pnv.elementAt(1));
		
		return new AssignmentNode(lhs, rhs);
		
	}

	public ReturnNode parseReturnNode(ParseNode node)
	{
		ExpressionNode expressionNode = parseExpressionNode(node);
		return new ReturnNode(expressionNode);
	}
	
	// parses starting from " "
	public OpNode parseOpNode(ParseNode node)
	{
		String label = node.getLabel();
		ParseNodeVector pnv = node.getChildren();
		if (label.equals("add"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.ADD));
		}
		else if (label.equals("sub"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.SUB));
		}
		else if (label.equals("mult"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.MULT));
		}
		else if (label.equals("div"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.DIV));
		}
		else if (label.equals("not"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			return new OpNode(left, null, new Operation(Operation.NOT));
		}
		else if (label.equals("comp_lt"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.LT));
		}
		else if (label.equals("comp_gt"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.GT));
		}
		else if (label.equals("equal"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.EQ));
		}
		else if (label.equals("not_equal"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.NEQ));
		}
		else if (label.equals("bitwise_and"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.BIT_AND));
		}
		else if (label.equals("bitwise_or"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.BIT_OR));
		}
		else if (label.equals("bitwise_xor"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.BIT_XOR));
		}
		else if (label.equals("logical_and"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.LOG_AND));
		}
		else if (label.equals("logical_or"))
		{
			ExpressionNode left = parseExpressionNode(pnv.elementAt(0));
			ExpressionNode right = parseExpressionNode(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.LOG_OR));
		}
		else
		{
			throw new Error();
		}
	}
	
	public LiteralNode parseLiteralNode(ParseNode node)
	{
		return new LiteralNode((int) node.getLiteral());
	}
	
	// parses starting from " "
	public ExpressionNode parseExpressionNode(ParseNode node)
	{
		String label = node.getLabel();
		if (node.getLabel().equals("literal"))
		{
			LiteralNode literalNode = parseLiteralNode(node.getFirstChild());
			return literalNode;
		}
		if (node.getLabel().equals("name"))
		{
			NameNode nameNode = parseNameNode(node.getFirstChild());
			return nameNode;
		}
		else if(node.getFirstChild().getLabel().equals("assignment"))
		{
			AssignmentNode assignmentNode = parseAssignmentNode(node.getFirstChild());
			return assignmentNode;
		}
		else if (label.equals("add") || label.equals("sub") || label.equals("mult") || label.equals("div") || label.equals("not") || label.equals("comp_lt") || label.equals("comp_gt" ) || label.equals("comp_lt") || label.equals("equal") || label.equals("not_equal") || label.equals("bitwise_and") || label.equals("bitwise_or") || label.equals("bitwise_xor") || label.equals("logical_or") || label.equals("logical_and"))
		{
			OpNode opNode = parseOpNode(node);
			return opNode;
		}

		else if(node.getFirstChild().getLabel().equals("methodinvoke1"))
		{
			MethodInvokeNode invokeNode = parseMethodInvokeNode(node.getFirstChild());
			return invokeNode;
		}
		else
		{
			return new TypeNode("hello");
			//throw new Error();
		}
	}
	
	public MethodInvokeNode parseMethodInvokeNode(ParseNode node)
	{
		NameNode name = parseNameNode(node.getChild("name"));
		ParseNodeVector pnv = node.getChild("argument_list").getChildren();
		MethodInvokeNode min = new MethodInvokeNode(name);
		
		for(int i = 0; i < pnv.size(); i++)
		{
			TreeNode n = parseNode(pnv.elementAt(i));
			//min.setArgumentSet(n);
		}
		
		return min;
	}
	
	public TreeNode parseNode(ParseNode node)
	{
		if(node.getLabel().equals("name"))
		{
			return parseNameNode(node);
		}
		else if(node.getLabel().equals("none"))
		{
			return null;
		}
		return null;
	}

	public String toString()
	{
		return program.toString();
	}
/*
	public TreeNode buildAST(ParseNode pn)
	{
		String label = pn.getLabel();
		ParseNodeVector pnv = pn.getChildren();
		if (label.equals("literal"))
		{
			ParseNode c = pn.getChild("integer");
			return new LiteralNode(((Integer) c.getLiteral()).intValue());
		}
		else if (label.equals("add"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.ADD));
		}
		else if (label.equals("sub"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.SUB));
		}
		else if (label.equals("mult"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.MULT));
		}
		else if (label.equals("div"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.DIV));
		}
		else if (label.equals("not"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			return new OpNode(left, null, new Operation(Operation.NOT));
		}
		else if (label.equals("comp_lt"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.LT));
		}
		else if (label.equals("comp_gt"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.GT));
		}
		else if (label.equals("equal"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.EQ));
		}
		else if (label.equals("not_equal"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.NEQ));
		}
		else if (label.equals("bitwise_and"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.BIT_AND));
		}
		else if (label.equals("bitwise_or"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.BIT_OR));
		}
		else if (label.equals("bitwise_xor"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.BIT_XOR));
		}
		else if (label.equals("logical_and"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.LOG_AND));
		}
		else if (label.equals("logical_or"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new OpNode(left, right, new Operation(Operation.LOG_OR));
		}
		else if (label.equals("name"))
		{
			ParseNode c = pn.getFirstChild();
			return new NameNode(((String) c.getLabel()));
		}
		else if (label.equals("classbody"))
		{
			TreeNode cb = buildAST(pnv.elementAt(0));
			return cb;
		}
		else if (label.equals("field_declaration"))
		{
			TreeNode type = buildAST(pnv.elementAt(0));
			String name = pnv.elementAt(1).getFirstChild().getFirstChild()
					.getFirstChild().getLabel();

			TreeNode fieldReturn = new TreeNode();
			TypeDescriptor typeReturn = new TypeDescriptor(name, type);

			fieldReturn.Field = new FieldDescriptor(name, typeReturn);

			return fieldReturn;
		}
		else if (label.equals("type"))
		{
			String t = pnv.elementAt(0).getFirstChild().getLabel();

			return new TypeNode(t);
		}
		else if (label.equals("variables"))
		{
			TreeNode v = buildAST(pnv.elementAt(0));
			return v;
		}
		else if (label.equals("variable_declarator"))
		{
			String name = pnv.elementAt(0).getFirstChild().getLabel();
			TreeNode expression = new LiteralNode((int) pnv.elementAt(1)
					.getFirstChild().getLiteral());
			return new DeclarationNode(name, null, expression);
		}
		else if (label.equals("single"))
		{
			ParseNode c = pn.getFirstChild();
			return new NameNode(((String) c.getLabel()));
		}
		else if (label.equals("class_body_declaration_list"))
		{
			TreeNode tempClass = new TreeNode();
			tempClass.Class = new ClassDescriptor(null, null, null);

			for (int i = 0; i < pnv.size(); i++)
			{
				if (label.equals("field"))
				{
					TreeNode f = buildAST(pnv.elementAt(i));
					tempClass.Class.getFieldDescriptorTable().add(f.Field);
				}
				else if (label.equals("method"))
				{
					TreeNode m = buildAST(pnv.elementAt(i));
					tempClass.Class.getMethodDescriptorTable().add(m.Method);
				}
			}
			return tempClass;
		}
		else if (label.equals("class_declaration"))
		{
			String name = pnv.elementAt(0).getFirstChild().getLabel();
			String superClass = pnv.elementAt(1).getFirstChild().getLabel();
			TreeNode classBody = buildAST(pnv.elementAt(2));

			TreeNode classReturn = new TreeNode();

			classReturn.Class = new ClassDescriptor(name, null, superClass);

			classReturn.Class
					.overrideFieldDescriptorSymbolTable(classBody.Class
							.getFieldDescriptorTable());
			classReturn.Class
					.overrideMethodDescriptorSymbolTable(classBody.Class
							.getMethodDescriptorTable());

			return classReturn;
		}
		else if (label.equals("type_declaration_list"))
		{
			TreeNode tdl = buildAST(pnv.elementAt(0));
			return tdl;
		}
		else if (label.equals("compilation_unit"))
		{
			TreeNode cu = buildAST(pnv.elementAt(0));
			return cu;
		}
		else if (label.equals("method"))
		{
			TreeNode m = buildAST(pnv.elementAt(0));
			return m;
		}
		
		 else if(label.equals("method_declaration")) { TreeNode mh =
		 buildAST(pnv.elementAt(0)); TreeNode body =
		 buildAST(pnv.elementAt(1)); return }
		 
		else if (label.equals("method_header"))
		{
			TreeNode mh = buildAST(pnv.elementAt(0));
			return mh;
		}
		else if (label.equals("returntype"))
		{
			TreeNode rt = buildAST(pnv.elementAt(0));
			return rt;
		}
		else if (label.equals("identifier"))
		{
			ParseNode c = pn.getFirstChild();
			return new NameNode(((String) c.getLabel()));
		}
		else if (label.equals("parameters"))
		{
			TreeNode p = buildAST(pnv.elementAt(0));
			return p;
		}
		else if (label.equals("formal_parameter_list"))
		{
			TreeNode fpl = buildAST(pnv.elementAt(0));
			return fpl;
		}
		
		else if(label.equals("formal_parameter")) { TreeNode t =
		buildAST(pnv.elementAt(0)); TreeNode s = buildAST(pnv.elementAt(1));
		return }
		 
		else if (label.equals("body"))
		{
			TreeNode b = buildAST(pnv.elementAt(0));
			return b;
		}
		else if (label.equals("block_statement_list"))
		{
			TreeNode bsl = buildAST(pnv.elementAt(0));
			return bsl;
		}
		else if (label.equals("expression"))
		{
			TreeNode exp = buildAST(pnv.elementAt(0));
			return exp;
		}
		else if (label.equals("assignment"))
		{
			TreeNode a = buildAST(pnv.elementAt(0));
			return a;
		}
		else if (label.equals("args"))
		{
			TreeNode left = buildAST(pnv.elementAt(0));
			TreeNode right = buildAST(pnv.elementAt(1));
			return new AssignmentNode(left, right);
		}
		else if (label.equals("return"))
		{
			TreeNode rs = buildAST(pnv.elementAt(0));
			return new ReturnNode(rs);
		}
		
		 else if(label.equals("method_declarator")) { TreeNode name =
		 buildAST(pnv.elementAt(0)); TreeNode parameters =
		 buildAST(pnv.elementAt(0)); return }
		 
		else if (label.equals("local_variable_declaration"))
		{
			TreeNode t = buildAST(pnv.elementAt(0));
			TreeNode vd = buildAST(pnv.elementAt(1));
			return new DeclarationNode(((DeclarationNode) vd).getName(),
					(TypeNode) t, ((DeclarationNode) vd).getInitializer());
		}
		else if (label.equals("initializer"))
		{
			TreeNode i = buildAST(pnv.elementAt(0));
			return i;
		}
		else if (label.equals("methodinvoke1"))
		{
			TreeNode n = buildAST(pnv.elementAt(0));
			TreeNode al = buildAST(pnv.elementAt(1));
			MethodInvokeNode method = new MethodInvokeNode(n);
			int size = al.Method.getParameterTable().getDescriptorsSet().size();
			for (int counter = 0; counter < size; counter++)
			{
				method.setArgumentSet(al.Tree);
			}

			return method;
		}
		else if (label.equals("argument_list"))
		{
			TreeNode arguments = new TreeNode();

			for (int i = 0; i < pnv.size(); i++)
			{
				TreeNode m = buildAST(pnv.elementAt(i));
				arguments.Tree.add(m);
			}
			return arguments;
		}
		else if (label.equals("ifstatement"))
		{
			TreeNode condition = buildAST(pnv.elementAt(0));
			TreeNode block = buildAST(pnv.elementAt(1));
			return new IfStatementNode(condition, block);
		}
		else if (label.equals("condition"))
		{
			TreeNode condition = buildAST(pnv.elementAt(0));
			return condition;
		}
		else if (label.equals("statement"))
		{
			TreeNode statement = buildAST(pnv.elementAt(0));
			return statement;
		}
		else if (label.equals("createobject"))
		{

		}
		else if (label.equals("whilestatement"))
		{
			TreeNode condition = buildAST(pnv.elementAt(0));
			TreeNode block = buildAST(pnv.elementAt(1));
			return new WhileStatementNode(condition, block);
		}
		else if (label.equals("class"))
		{
			TreeNode c = buildAST(pnv.elementAt(0));
			return c;
		}
		else if (label.equals("cast2"))
		{

		}
		else
		{
			throw new Error();
		}
		throw new Error();
	}
	*/
}
