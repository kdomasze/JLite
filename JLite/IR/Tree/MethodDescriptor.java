package IR.Tree;

import java.util.HashMap;
import java.util.Map;

public class MethodDescriptor extends Descriptor
{
	private TreeNode ASTTree; // holds the AST Tree for the method
	private Map<String, VarDescriptor> VarDescriptorMap = new HashMap<String, VarDescriptor>(); // hashMap for variable descriptors
	
	// put methods
	public void createASTTree(TreeNode AST)
	{
		ASTTree = AST;
	}
	
	public void addVarDescriptor(String Var, VarDescriptor VD)
	{
		VarDescriptorMap.put(Var, VD);
	}
	
	// get methods
	public TreeNode getASTTree()
	{
		return ASTTree;
	}
	
	public VarDescriptor getVarDescriptor(String Var)
	{
		return VarDescriptorMap.get(Var);
	}
}
