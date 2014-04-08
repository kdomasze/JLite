package IR.Tree;

import java.util.HashMap;
import java.util.Map;

public class MethodDescriptor extends Descriptor
{
	private TypeDescriptor ReturnType; // holds the return type of the Method
	private Map<String, FieldDescriptor> ParameterMap = new HashMap<String, FieldDescriptor>(); // hashmap for the method parameters
	private TreeNode ASTTree; // holds the AST Tree for the method
	private Map<String, VarDescriptor> VarDescriptorMap = new HashMap<String, VarDescriptor>(); // hashMap for variable descriptors
	
	// constructor
	public MethodDescriptor(TypeDescriptor rt)
	{
		ReturnType = rt;
	}
	
	// put methods	
	public void addParameter(String Identifier, FieldDescriptor Parameter)
	{
		ParameterMap.put(Identifier, Parameter);
	}
	
	public void createASTTree(TreeNode AST)
	{
		ASTTree = AST;
	}
	
	public void addVarDescriptor(String Identifier, VarDescriptor VD)
	{
		VarDescriptorMap.put(Identifier, VD);
	}
	
	// get methods
	public TypeDescriptor getReturnType()
	{
		return ReturnType;
	}
	
	public FieldDescriptor getParameter(String Identifier)
	{
		return ParameterMap.get(Identifier);
	}
	
	public TreeNode getASTTree()
	{
		return ASTTree;
	}
	
	public VarDescriptor getVarDescriptor(String Identifier)
	{
		return VarDescriptorMap.get(Identifier);
	}
}
