package IR;

import IR.Tree.TreeNode;

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
	
	// set methods
	public void setType(TypeDescriptor type)
	{
		ReturnType = type;
	}
	
	public void setASTTree(TreeNode node)
	{
		ASTTree = node;
	}
	
	// put methods	
	public void createASTTree(TreeNode AST)
	{
		ASTTree = AST;
	}
	
	public void addParameter(FieldDescriptor field)
	{
		ParameterSymbolTable.add(field);
	}
	
	public void addVarDescriptor(VarDescriptor var)
	{
		VarDescriptorSymbolTable.add(var);
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
	
	public SymbolTable getVarDescriptorTable()
	{
		return VarDescriptorSymbolTable;
	}
	
	public String toString()
	{
		return ReturnType.toString() + "]" + "\n\t\tParameters: " + ParameterSymbolTable.toString() + "\n\t\tVarDescriptor: " + VarDescriptorSymbolTable.toString() + "\n\t" + ASTTree.toString();
	}
}
