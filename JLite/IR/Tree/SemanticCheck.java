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
		
		Set<Descriptor> classes = program.program.getClasses().getDescriptorsSet();
		Set<String> classNames = program.program.getClasses().getNamesSet(); // class names for checking for duplicate names
		
		// loop over all classes in the program
		for(Descriptor classInProgam : classes)
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
		 * Semantic Check 1: Methods
		 */
		for(String methodName : methodNames)
		{
			if(methods.getDescriptorsSet(methodName).size() > 1)
			{
				throw new Error("[ERROR_01] '" + methodName + "' identifier has duplicate @'" + classDesc.getSymbol() + "'.");
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
			 * Semantic Check 1: Method Params
			 */
			if((nametable.get(name) != null && !(nametable.get(name) instanceof FieldDescriptor)))
			{
				throw new Error("[ERROR_01] '" + name + "' identifier has duplicate @'" + method.getSymbol() + "'.");
			}
			VarDescriptor tempVar = new VarDescriptor(name, new TypeDescriptor(null, type), expression);
			nametable.add(tempVar);
			
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
				String name = ((NameNode)((OpNode)expression).getOperand1()).getName();
				if(nametable.get(name) == null)
				{
					throw new Error("[ERROR_02] '" + name + "' identifier not declared.");
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
				checkExpression(expressionSend, nametable, method);
			}
			
			// check operand 2
			if(((OpNode)expression).getOperand2() instanceof NameNode)
			{
				String name = ((NameNode)((OpNode)expression).getOperand2()).getName();
				if(nametable.get(name) == null)
				{
					throw new Error("[ERROR_02] '" + name + "' identifier not declared.");
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
				checkExpression(expressionSend, nametable, method);
			}
		}
		
		// check methodInvokeNode
		else if(expression instanceof MethodInvokeNode)
		{
			if((nametable.get(((NameNode)((MethodInvokeNode)expression).getNameNode()).getName())) == null)
			{
				throw new Error("[ERROR_02] '" + ((NameNode)((MethodInvokeNode)expression).getNameNode()).getName() + "' identifier not declared.");
			}
			else if((nametable.get(((NameNode)((MethodInvokeNode)expression).getNameNode()).getName())) != null && !((nametable.get(((NameNode)((MethodInvokeNode)expression).getNameNode()).getName())) instanceof MethodDescriptor))
			{
				throw new Error("[ERROR_02] '" + ((NameNode)((MethodInvokeNode)expression).getNameNode()).getName() + "' identifier not declared.");
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
			if(nametable.get(((NameNode)expression).getName()) == null)
			{
				throw new Error("[ERROR_02] '" + ((NameNode)expression).getName() + "' identifier not declared.");
			}
		}
	}
	
	// checks conditions
	private void checkConditions(ExpressionNode expression, SymbolTable nametable, MethodDescriptor method)
	{
		checkExpression(expression, nametable, method);
	}
}
