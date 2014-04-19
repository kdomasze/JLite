package IR.Tree;

import IR.*;
import Parse.ParseNode;
import Parse.ParseNodeVector;

public class BuildAST
{
	Program program;

	public BuildAST()
	{
		program = new Program();

	}

	public Program parse(ParseNode pn)
	{
		ParseNodeVector pnv = pn.getFirstChild().getChildren();
		for(int i = 0; i < pnv.size(); i++)
		{
			parseClassDeclaration(pnv.elementAt(i));
		}

		return program;
	}

	public void parseClassDeclaration(ParseNode node)
	{
		NameNode name = parseNameNode(node.getChild("name"));
		NameNode superClass = parseSuperClass(node.getChild("super"));
		
		ClassDescriptor Class = new ClassDescriptor(name.getName(), null, superClass.getName());
 
		parseClassBody(Class, node.getChild("classbody"));
		
		program.addClass(Class);
	}
	
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
	
	public NameNode parseSuperClass(ParseNode node)
	{
		return new NameNode(node.getFirstChild().getLabel());
	}
	
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
	
	public FieldDescriptor ParseField(ParseNode node)
	{
		ParseNode pn = node.getFirstChild();
		TypeNode typeNode = parseTypeNode(pn.getChild("type"));
		VarNode varNode = parseVarNode(pn.getChild("variables"));
		
		TypeDescriptor type = new TypeDescriptor(null, typeNode);
		
		return new FieldDescriptor(varNode.getName().getName(), type);
	}
	
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
	
	public VarNode parseVarNode(ParseNode node)
	{
		if(node.getLabel().equals("variables"))
		{
			ParseNode pn = node.getFirstChild().getFirstChild();
			NameNode name = parseNameNode(pn);
			
			return new VarNode(name, null);
		}
		
		return new VarNode(null, null);
	}
	
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
	
	public FieldDescriptor parseParameters(ParseNode node)
	{
		ParseNode pn = node.getChild("type");
		TypeNode typeNode =  parseTypeNode(node);
		
		TypeDescriptor type = new TypeDescriptor(null, typeNode);
		
		return new FieldDescriptor(node.getChild("single").getFirstChild().getLabel(), type);
	}
	
	public BlockNode parseBlockNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getFirstChild().getChildren();
		BlockStatementNode s = new 	BlockStatementNode();
		for(int i = 0; i < pnv.size(); i++)
		{
			s = parseBlockStatementNode(pnv.elementAt(i));
		}
		
		BlockNode blockNode = new BlockNode(s);

		return blockNode;
	}
	
	public 	BlockStatementNode parseBlockStatementNode(ParseNode node)
	{
		if(node.getLabel().equals("ifstatement"))
		{
			IfStatementNode ifstatementNode = parseIfStatementNode(node);
			return ifstatementNode;
		}
		else if(node.getLabel().equals("whilestatement"))
		{
			WhileStatementNode whilestatementNode = parseWhileStatementNode(node);
			return whilestatementNode;
		}
		else if(node.getLabel().equals("expression"))
		{
			ExpressionNode expressionNode = parseExpressionNode(node);
			return expressionNode;
		}

		throw new Error();

	}
	
	public IfStatementNode parseIfStatementNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getChildren();
		OpNode opNode = parseOpNode(pnv.elementAt(0).getFirstChild());
		BlockNode blockNode = parseBlockNode(pnv.elementAt(1).getChild("block_statement_list"));

		return new IfStatementNode(opNode, blockNode);
	}

	public WhileStatementNode parseWhileStatementNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getChildren();

		OpNode opNode = parseOpNode(pnv.elementAt(0).getFirstChild());
		BlockNode blockNode = parseBlockNode(pnv.elementAt(1).getChild("block_statement_list"));

		return new WhileStatementNode(opNode, blockNode);
	}
	
	public AssignmentNode parseAssignmentNode(ParseNode node)
	{
		ParseNodeVector pnv = node.getChildren();
		ExpressionNode lhs = parseNameNode(pnv.elementAt(0));
		ExpressionNode rhs = parseNameNode(pnv.elementAt(1));
		
		return new AssignmentNode(lhs, rhs);		
	}
	
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
	
	public ExpressionNode parseExpressionNode(ParseNode node)
	{
		if(node.getLabel().equals("local_variable_declaration"))
		{
			TypeNode typeNode = parseTypeNode(node.getChild("type"));
			return typeNode;
		}
		else if(node.getFirstChild().getLabel().equals("assignment"))
		{
			AssignmentNode assignmentNode = parseAssignmentNode(node.getFirstChild().getChild("args"));
			return assignmentNode;
		}

			return new TypeNode("hello");
		//throw new Error();
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
