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
	private SymbolTable nameTable = new SymbolTable();
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
				// if they are, we run a check on the things inside of the class
				checkClassDescriptor((ClassDescriptor)classInProgam);
			}
		}
		
	}
	
	// checks all of the elements inside of the class
	private void checkClassDescriptor(ClassDescriptor classDesc)
	{

		Set<Descriptor> methods = classDesc.getMethodDescriptorTable().getDescriptorsSet();
		Set<String> methodNames = classDesc.getFieldDescriptorTable().getNamesSet(); // method names for checking for duplicate names
		
		// loop over all methods in the class
		for(Descriptor methodInClass : methods)
		{
			// confirms if they are methods
			if(methodInClass instanceof MethodDescriptor)
			{
				// check the header of the method
				checkMethodHeader((MethodDescriptor)methodInClass);
				
				// get the tree elements of the method body
				TreeNode methodBody = ((MethodDescriptor)methodInClass).getASTTree();
				
				// "convert" the methodBody tree into a vector
				Vector<BlockStatementNode> methodBodyVector = ((BlockNode)methodBody).getblockStatementVector();
				
				// check the method body
				checkBlockStatementVector(methodBodyVector);
			}
		}
	}
	
	// checks the elements in the method header
	private void checkMethodHeader(MethodDescriptor methodDesc)
	{
		
	}
	
	// iterates over the block vector to check each block statement
	private void checkBlockStatementVector(Vector<BlockStatementNode> bodyVector)
	{
		// loop over all block statements in the block vector
		for(BlockStatementNode blockStatement : bodyVector)
		{
			// check the block statement
			checkBlockStatement(blockStatement);
		}
	}
	
	// checks all the different possible block statements in the block bodies
	private void checkBlockStatement(BlockStatementNode blockStatement)
	{
		// if the statement is a while statement, we need to check the condition and body
		if(blockStatement instanceof WhileStatementNode)
		{
			checkConditions(((WhileStatementNode)blockStatement).getOperation());
			Vector<BlockStatementNode> whileBodyVector = ((WhileStatementNode)blockStatement).getBlock().getblockStatementVector();
			checkBlockStatementVector(whileBodyVector);
		}
		// if the statement is an if statement, we need to check the condition, body, and else body.
		else if(blockStatement instanceof IfStatementNode)
		{
			checkConditions(((IfStatementNode)blockStatement).getOperation());
			Vector<BlockStatementNode> ifBodyVector = ((IfStatementNode)blockStatement).getBlock().getblockStatementVector();
			Vector<BlockStatementNode> elseBodyVector = ((IfStatementNode)blockStatement).getBlocke().getblockStatementVector();
			checkBlockStatementVector(ifBodyVector);
			
			if(((IfStatementNode)blockStatement).getBlocke() != null)
			{
				checkBlockStatementVector(elseBodyVector);
			}
		}
		else
		{
			
		}
	}
	
	// checks conditions
	private void checkConditions(ExpressionNode expression)
	{
		
	}
}
