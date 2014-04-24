package IR.Tree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import IR.*;

/***
 * Implement the check function for each type of AST Node
 * 
 */
public class SemanticCheck
{
	private BuildAST program;
	
	public SemanticCheck(BuildAST p)
	{
		program = p;
	}
	
	public void run()
	{
		/*
		 *  main loop over the AST Tree
		 */
		
		SymbolTable classes = program.program.getClasses();
		Set<Descriptor> classSet = classes.getDescriptorsSet();
		Set<String> classNames = classes.getNamesSet(); // class names for checking for duplicate names
		
		/*
		 * Semantic Check 1: Classes
		 */
		for(String className : classNames)
		{
			if(classes.getDescriptorsSet(className).size() > 1)
			{
				throw new Error("[ERROR_01] '" + className + "' identifier has duplicate.");
			}
		}
		
		// loop over all classes in the program
		for(Descriptor classInProgam : classSet)
		{
			// confirms if they are classes
			if(classInProgam instanceof ClassDescriptor)
			{
				SymbolTable nametable = new SymbolTable(null);
				
				// if they are, we run a check on the things inside of the class
				checkClassDescriptor((ClassDescriptor)classInProgam, nametable);
			}
		}
	}
	
	// checks all of the elements inside of the class
	private void checkClassDescriptor(ClassDescriptor classDesc, SymbolTable nametable)
	{

		SymbolTable methods = classDesc.getMethodDescriptorTable();
		Set<Descriptor> methodSet = methods.getDescriptorsSet();
		Set<String> methodNames = classDesc.getMethodDescriptorTable().getNamesSet(); // method names for checking for duplicate names
		
		SymbolTable fields = classDesc.getFieldDescriptorTable();
		Set<Descriptor> fieldSet = fields.getDescriptorsSet();
		Set<String> fieldNames = classDesc.getFieldDescriptorTable().getNamesSet(); // method names for checking for duplicate names
		
		/*
		 * Semantic Check 1: Fields
		 */
		for(String fieldName : fieldNames)
		{
			if(fields.getDescriptorsSet(fieldName).size() > 1)
			{
				throw new Error("[ERROR_01] '" + fieldName + "' identifier has duplicate @'" + classDesc.getSymbol() + "'.");
			}
			nametable.add(fields.get(fieldName));
		}
		
		/*
		 * Semantic Check 14: Methods
		 */
		for(String methodName : methodNames)
		{
			if(methods.getDescriptorsSet(methodName).size() > 1)
			{
				throw new Error("[ERROR_14] '" + methodName + "' is being overloaded @'" + classDesc.getSymbol() + "'.");
			}
			nametable.add(methods.get(methodName));
		}
		
		// loop over all methods in the class
		for(Descriptor methodInClass : methodSet)
		{
			// confirms if they are methods
			if(methodInClass instanceof MethodDescriptor)
			{
				SymbolTable newnametable = new SymbolTable(nametable);
				// check the header of the method
				checkMethodHeader((MethodDescriptor)methodInClass, newnametable);
				
				// get the tree elements of the method body
				TreeNode methodBody = ((MethodDescriptor)methodInClass).getASTTree();
				
				// "convert" the methodBody tree into a vector
				Vector<BlockStatementNode> methodBodyVector = ((BlockNode)methodBody).getblockStatementVector();
				
				// check the method body
				checkBlockStatementVector(methodBodyVector, newnametable, (MethodDescriptor)methodInClass);
			}
		}
	}
	
	// checks the elements in the method header
	private void checkMethodHeader(MethodDescriptor methodDesc, SymbolTable nametable)
	{
		
		SymbolTable params = methodDesc.getParameterTable();
		Set<Descriptor> paramSet = params.getDescriptorsSet();
		Set<String> paramNames = params.getNamesSet(); // names for checking for duplicate names
		
		/*
		 * Semantic Check 1: Method Params
		 */
		for(String fieldName : paramNames)
		{
			if(params.getDescriptorsSet(fieldName).size() > 1 || (nametable.get(fieldName) != null && !(nametable.get(fieldName) instanceof FieldDescriptor)))
			{
				throw new Error("[ERROR_01] '" + fieldName + "' identifier has duplicate @'" + methodDesc.getSymbol() + "'.");
			}
			FieldDescriptor tempVar = new FieldDescriptor(params.get(fieldName).getSymbol(), ((FieldDescriptor)params.get(fieldName)).getFieldType());
			nametable.add(tempVar);
		}
		
		/*
		 * Semantic Check 6 & 7
		 */
		String methodName = methodDesc.getSymbol();
		String type = methodDesc.getReturnType().getType();
		String returnStatementType = "__";
		Vector<BlockStatementNode> methodBlockVector = ((BlockNode)((MethodDescriptor)nametable.get(methodName)).getASTTree()).getblockStatementVector();
		
		boolean noReturn = true;
		
		for(BlockStatementNode statement : methodBlockVector)
		{
			if(statement instanceof ReturnNode)
			{
				if(type.equals("void"))
				{
					throw new Error("[ERROR_06] '" + methodName + "' has return with void type.");
				}
				else
				{
					noReturn = false;
				}
				returnStatementType = getArgumentType(((ReturnNode)statement).getReturnStatement(), nametable).getType();
			}
		}
		
		if(noReturn && !type.equals("void"))
		{
			throw new Error("[ERROR_06] '" + methodName + "' has no return.");
		}
		
		if(!returnStatementType.equals(type) && !type.equals("void"))
		{
			throw new Error("[ERROR_07] '" + methodName + "' return type does not match returned value.");
		}
	}
	
	// iterates over the block vector to check each block statement
	private void checkBlockStatementVector(Vector<BlockStatementNode> bodyVector, SymbolTable nametable, MethodDescriptor method)
	{
		SymbolTable newnametable = new SymbolTable(nametable);
		// loop over all block statements in the block vector
		for(BlockStatementNode blockStatement : bodyVector)
		{
			// check the block statement
			checkBlockStatement(blockStatement, newnametable, method);
		}
	}
	
	// checks all the different possible block statements in the block bodies
	private void checkBlockStatement(BlockStatementNode blockStatement, SymbolTable nametable, MethodDescriptor method)
	{
		// if the statement is a while statement, we need to check the condition and body
		if(blockStatement instanceof WhileStatementNode)
		{
			checkConditions(((WhileStatementNode)blockStatement).getOperation(), nametable, method);
			Vector<BlockStatementNode> whileBodyVector = ((WhileStatementNode)blockStatement).getBlock().getblockStatementVector();
			checkBlockStatementVector(whileBodyVector, nametable, method);
		}
		// if the statement is an if statement, we need to check the condition, body, and else body.
		else if(blockStatement instanceof IfStatementNode)
		{
			checkConditions(((IfStatementNode)blockStatement).getOperation(), nametable, method);
			Vector<BlockStatementNode> ifBodyVector = ((IfStatementNode)blockStatement).getBlock().getblockStatementVector();

			checkBlockStatementVector(ifBodyVector, nametable, method);
			
			if(((IfStatementNode)blockStatement).getBlocke() != null)
			{
				Vector<BlockStatementNode> elseBodyVector = ((IfStatementNode)blockStatement).getBlocke().getblockStatementVector();
				checkBlockStatementVector(elseBodyVector, nametable, method);
			}
		}
		else if(blockStatement instanceof DeclarationNode)
		{
			String name = ((DeclarationNode)blockStatement).getName();
			TypeNode type = ((DeclarationNode)blockStatement).getType();
			ExpressionNode expression = ((DeclarationNode)blockStatement).getInitializer();
			
			/*
			 * Semantic Check 1: var declaration
			 */
			if((nametable.get(name) != null && !(nametable.get(name) instanceof FieldDescriptor)))
			{
				throw new Error("[ERROR_01] '" + name + "' identifier has duplicate @'" + method.getSymbol() + "'.");
			}
			VarDescriptor tempVar = new VarDescriptor(name, new TypeDescriptor(null, type), expression);
			nametable.add(tempVar);
			
			if(expression instanceof MethodInvokeNode)
			{
				/*
				 * Semantic Check 5
				 */
				String methodName = ((NameNode)((MethodInvokeNode)expression).getNameNode()).getName();
				
				Vector<BlockStatementNode> methodBlockVector = ((BlockNode)((MethodDescriptor)nametable.get(methodName)).getASTTree()).getblockStatementVector();
				
				boolean noReturn = true;
				
				for(BlockStatementNode statement : methodBlockVector)
				{
					if(statement instanceof ReturnNode)
					{
						noReturn = false;
					}
				}
				
				if(noReturn)
				{
					throw new Error("[ERROR_05] '" + methodName + "' has no return.");
				}
			}
			
			checkExpression(expression, nametable, method);
		}
		else if(blockStatement instanceof AssignmentNode)
		{
			ExpressionNode lhs = ((AssignmentNode)blockStatement).getLeftHandSide();
			ExpressionNode rhs = ((AssignmentNode)blockStatement).getRightHandSide();
			
			checkExpression(lhs, nametable, method);
			checkExpression(rhs, nametable, method);
		}
	}
	
	private void checkExpression(ExpressionNode expression, SymbolTable nametable, MethodDescriptor method)
	{
		
		if(expression instanceof OpNode)
		{
			// check operand 1
			if(((OpNode)expression).getOperand1() instanceof NameNode)
			{
				/*
				 * Semantic Check 2: expression
				 */
				String name = ((NameNode)((OpNode)expression).getOperand1()).getName();
				if(nametable.get(name) == null)
				{
					throw new Error("[ERROR_02] '" + name + "' identifier not declared.");
				}
				if(nametable.get(name) instanceof FieldDescriptor)
				{
					if(!((FieldDescriptor)nametable.get(name)).getFieldType().getType().equals("int"))
					{
						throw new Error("[ERROR_10] '" + name + "' not of type int.");
					}
				}
				if(nametable.get(name) instanceof VarDescriptor)
				{
					if(!((VarDescriptor)nametable.get(name)).getType().getType().equals("int"))
					{
						throw new Error("[ERROR_10] '" + name + "' not of type int.");
					}
				}
			}
			else if(((OpNode)expression).getOperand1() instanceof OpNode)
			{
				ExpressionNode expressionSend = ((OpNode)expression).getOperand1();
				checkExpression(expressionSend, nametable, method);
			}
			else if(((OpNode)expression).getOperand1() instanceof MethodInvokeNode)
			{
				ExpressionNode expressionSend = ((OpNode)expression).getOperand1();
				
				/*
				 * Semantic Check 5
				 */
				String methodName = ((NameNode)((MethodInvokeNode)expressionSend).getNameNode()).getName();
				
				Vector<BlockStatementNode> methodBlockVector = ((BlockNode)((MethodDescriptor)nametable.get(methodName)).getASTTree()).getblockStatementVector();
				
				boolean noReturn = true;
				
				for(BlockStatementNode statement : methodBlockVector)
				{
					if(statement instanceof ReturnNode)
					{
						noReturn = false;
					}
				}
				
				if(noReturn)
				{
					throw new Error("[ERROR_05] '" + methodName + "' has no return.");
				}
				
				checkExpression(expressionSend, nametable, method);
			}
			
			// check operand 2
			if(((OpNode)expression).getOperand2() instanceof NameNode)
			{
				/*
				 * Semantic Check 2: expression
				 */
				String name = ((NameNode)((OpNode)expression).getOperand2()).getName();
				if(nametable.get(name) == null)
				{
					throw new Error("[ERROR_02] '" + name + "' identifier not declared.");
				}
				if(nametable.get(name) instanceof FieldDescriptor)
				{
					if(!((FieldDescriptor)nametable.get(name)).getFieldType().getType().equals("int"))
					{
						throw new Error("[ERROR_10] '" + name + "' not of type int.");
					}
				}
				if(nametable.get(name) instanceof VarDescriptor)
				{
					if(!((VarDescriptor)nametable.get(name)).getType().getType().equals("int"))
					{
						throw new Error("[ERROR_10] '" + name + "' not of type int.");
					}
				}
			}
			else if(((OpNode)expression).getOperand2() instanceof OpNode)
			{
				ExpressionNode expressionSend = ((OpNode)expression).getOperand2();
				checkExpression(expressionSend, nametable, method);
			}
			else if(((OpNode)expression).getOperand2() instanceof MethodInvokeNode)
			{
				ExpressionNode expressionSend = ((OpNode)expression).getOperand2();
				
				/*
				 * Semantic Check 5
				 */
				String methodName = ((NameNode)((MethodInvokeNode)expressionSend).getNameNode()).getName();
				
				Vector<BlockStatementNode> methodBlockVector = ((BlockNode)((MethodDescriptor)nametable.get(methodName)).getASTTree()).getblockStatementVector();
				
				boolean noReturn = true;
				
				for(BlockStatementNode statement : methodBlockVector)
				{
					if(statement instanceof ReturnNode)
					{
						noReturn = false;
					}
				}
				
				if(noReturn)
				{
					throw new Error("[ERROR_05] '" + methodName + "' has no return.");
				}
				
				checkExpression(expressionSend, nametable, method);
			}
		}
		
		// check methodInvokeNode
		else if(expression instanceof MethodInvokeNode)
		{
			/*
			 * Semantic Check 2: method invoke
			 */
			if((nametable.get(((NameNode)((MethodInvokeNode)expression).getNameNode()).getName())) == null)
			{
				throw new Error("[ERROR_02] '" + ((NameNode)((MethodInvokeNode)expression).getNameNode()).getName() + "' identifier not declared.");
			}
			else if((nametable.get(((NameNode)((MethodInvokeNode)expression).getNameNode()).getName())) != null && !((nametable.get(((NameNode)((MethodInvokeNode)expression).getNameNode()).getName())) instanceof MethodDescriptor))
			{
				throw new Error("[ERROR_02] '" + ((NameNode)((MethodInvokeNode)expression).getNameNode()).getName() + "' identifier not declared.");
			}
			
			/*
			 * Semantic Check 4: method invoke params
			 */
			Vector<ExpressionNode> methodParamVector = ((MethodInvokeNode)expression).getArgumentVector();
			String methodInvokeName = ((NameNode)((MethodInvokeNode)expression).getNameNode()).getName();
			Set<Descriptor> paramSet = ((MethodDescriptor)nametable.get(methodInvokeName)).getParameterTable().getDescriptorsSet();
			Object[] paramObjectArray = paramSet.toArray();
			
			FieldDescriptor[] paramArray = Arrays.copyOf(paramObjectArray, paramObjectArray.length, FieldDescriptor[].class);
			
			if(paramSet.size() != methodParamVector.size())
			{
				throw new Error("[ERROR_04] '" + methodInvokeName + "' invoke arguments do not match method declaration.");
			}
			else
			{
				int i = 0;
				for(ExpressionNode arg : methodParamVector)
				{
					TypeNode type = getArgumentType(arg, nametable);
					if(!paramArray[i].getFieldType().getType().equals(type.getType()))
					{
						throw new Error("[ERROR_04] '" + methodInvokeName + "' invoke arguments do not match method declaration.");
					}
					
					i++;
				}
			}
				
			Vector<ExpressionNode> argumentVector = ((MethodInvokeNode)expression).getArgumentVector();
			for(ExpressionNode expr : argumentVector)
			{
				if(expr instanceof OpNode)
				{
					checkExpression((OpNode)expr, nametable, method);
				}
				else if(expr instanceof MethodInvokeNode)
				{
					checkExpression((MethodInvokeNode)expr, nametable, method);
				}
				else if(expr instanceof NameNode)
				{
					/*
					 * Semantic Check 2: method invoke param
					 */
					String name = ((NameNode)expr).getName();
					if(nametable.get(name) == null)
					{
						throw new Error("[ERROR_02] '" + name + "' identifier not declared.");
					}
				}
			}
		}
		
		// check namenodes
		else if(expression instanceof NameNode)
		{
			/*
			 * Semantic Check 2: single names
			 */
			if(nametable.get(((NameNode)expression).getName()) == null)
			{
				throw new Error("[ERROR_02] '" + ((NameNode)expression).getName() + "' identifier not declared.");
			}
			if(nametable.get(((NameNode)expression).getName()) instanceof FieldDescriptor)
			{	if(((FieldDescriptor)nametable.get(((NameNode)expression).getName())).getFieldType().equals("int"))
				{	
					throw new Error("[ERROR_10] '" + nametable.get(((NameNode)expression).getName()) + "' not of type int.");
				}
			}
			if(nametable.get(((NameNode)expression).getName()) instanceof VarDescriptor)
			{	if(((VarDescriptor)nametable.get(((NameNode)expression).getName())).getType().equals("int"))
				{	
					throw new Error("[ERROR_10] '" + nametable.get(((NameNode)expression).getName()) + "' not of type int.");
				}
			}
		}
	}
	
	private TypeNode getArgumentType(ExpressionNode arg, SymbolTable nametable)
	{
		if(arg instanceof OpNode)
		{
			// check operand 1
			if(((OpNode)arg).getOperand1() instanceof NameNode)
			{
				String name = ((NameNode)((OpNode)arg).getOperand1()).getName();
				
				if(nametable.get(name) instanceof FieldDescriptor)
				{
					return new TypeNode(((FieldDescriptor)nametable.get(name)).getFieldType().getType());
				}
				else if(nametable.get(name) instanceof VarDescriptor)
				{
					return new TypeNode(((VarDescriptor)nametable.get(name)).getType().getType());
				}
			}
			else if(((OpNode)arg).getOperand1() instanceof OpNode)
			{
				ExpressionNode expressionSend = ((OpNode)arg).getOperand1();
				return getArgumentType(expressionSend, nametable);
			}
			else if(((OpNode)arg).getOperand1() instanceof MethodInvokeNode)
			{
				ExpressionNode expressionSend = ((OpNode)arg).getOperand1();
				MethodDescriptor methodDesc = ((MethodDescriptor)nametable.get(((NameNode)((MethodInvokeNode)expressionSend).getNameNode()).getName()));
				
				return new TypeNode(methodDesc.getReturnType().getType());
			}
			
			// check operand 2
			if(((OpNode)arg).getOperand2() instanceof NameNode)
			{
				String name = ((NameNode)((OpNode)arg).getOperand2()).getName();
				
				if(nametable.get(name) instanceof FieldDescriptor)
				{
					return new TypeNode(((FieldDescriptor)nametable.get(name)).getFieldType().getType());
				}
				else if(nametable.get(name) instanceof VarDescriptor)
				{
					return new TypeNode(((VarDescriptor)nametable.get(name)).getType().getType());
				}
			}
			else if(((OpNode)arg).getOperand2() instanceof OpNode)
			{
				ExpressionNode expressionSend = ((OpNode)arg).getOperand2();
				return getArgumentType(expressionSend, nametable);
			}
			else if(((OpNode)arg).getOperand2() instanceof MethodInvokeNode)
			{
				MethodInvokeNode expressionSend = ((MethodInvokeNode)((OpNode)arg).getOperand2());
				MethodDescriptor methodDesc = ((MethodDescriptor)nametable.get(((NameNode)((MethodInvokeNode)expressionSend).getNameNode()).getName()));
				
				return new TypeNode(methodDesc.getReturnType().getType());
			}
		}
		
		// check methodInvokeNode
		else if(arg instanceof MethodInvokeNode)
		{			
			MethodInvokeNode expressionSend = (MethodInvokeNode)arg;
			MethodDescriptor methodDesc = ((MethodDescriptor)nametable.get(((NameNode)((MethodInvokeNode)expressionSend).getNameNode()).getName()));
			
			return new TypeNode(methodDesc.getReturnType().getType());
		}
		
		else if(arg instanceof NameNode)
		{
			String name = ((NameNode)arg).getName();
			
			if(nametable.get(name) instanceof FieldDescriptor)
			{
				return new TypeNode(((FieldDescriptor)nametable.get(name)).getFieldType().getType());
			}
			else if(nametable.get(name) instanceof VarDescriptor)
			{
				return new TypeNode(((VarDescriptor)nametable.get(name)).getType().getType());
			}
		}
		return new TypeNode("NULL");
		
		
	}
	
	// checks conditions
	private void checkConditions(ExpressionNode expression, SymbolTable nametable, MethodDescriptor method)
	{
		checkExpression(expression, nametable, method);
	}
}
