package IR;

import Tree.*;

public class MethodDescriptor extends Descriptor
{
	private TypeDescriptor ReturnType; // holds the return type of the Method
	private SymbolTable ParameterSymbolTable; // Symbol Table for the method parameters
	private TreeNode ASTTree; // holds the AST Tree for the method
	private SymbolTable VarDescriptorSymbolTable; // Symbol Table for variable descriptors
	
	// constructor
	public MethodDescriptor(String name, TypeDescriptor rt, SymbolTable parent)
	{
		super(name);
		ParameterSymbolTable = new SymbolTable(parent);
		ReturnType = rt;
		VarDescriptorSymbolTable = new SymbolTable(parent);
	}
	
	// put methods	
	public void createASTTree(TreeNode AST)
	{
		ASTTree = AST;
	}
	
	// get methods
	public TypeDescriptor getReturnType()
	{
		return ReturnType;
	}
	
	public SymbolTable getParameterTable()
	{
		return ParameterSymbolTable;
	}
	
	public TreeNode getASTTree()
	{
		return ASTTree;
	}
	
	public VarDescriptor getVarDescriptorTable()
	{
		return VarDescriptorSymbolTable;
	}
}
