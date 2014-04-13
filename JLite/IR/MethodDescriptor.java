package IR;

import java.util.HashMap;
import java.util.Map;

public class MethodDescriptor extends Descriptor
{
	private TypeDescriptor ReturnType; // holds the return type of the Method
	private Map<NameDescriptor, FieldDescriptor> ParameterMap = new HashMap<NameDescriptor, FieldDescriptor>(); // hashmap for the method parameters
	private TreeNode ASTTree; // holds the AST Tree for the method
	private Map<NameDescriptor, VarDescriptor> VarDescriptorMap = new HashMap<NameDescriptor, VarDescriptor>(); // hashMap for variable descriptors
	
	// constructor
	public MethodDescriptor(TypeDescriptor rt)
	{
		ReturnType = rt;
	}
	
	// put methods	
	public void addParameter(NameDescriptor Identifier, FieldDescriptor Parameter)
	{
		ParameterMap.put(Identifier, Parameter);
	}
	
	public void createASTTree(TreeNode AST)
	{
		ASTTree = AST;
	}
	
	public void addVarDescriptor(NameDescriptor Identifier, VarDescriptor VD)
	{
		VarDescriptorMap.put(Identifier, VD);
	}
	
	// get methods
	public TypeDescriptor getReturnType()
	{
		return ReturnType;
	}
	
	public FieldDescriptor getParameter(NameDescriptor Identifier)
	{
		return ParameterMap.get(Identifier);
	}
	
	public TreeNode getASTTree()
	{
		return ASTTree;
	}
	
	public VarDescriptor getVarDescriptor(NameDescriptor Identifier)
	{
		return VarDescriptorMap.get(Identifier);
	}
}
