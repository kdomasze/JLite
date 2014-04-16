package IR.Tree;

public class DeclarationNode extends BlockStatementNode
{
	private String name;
	private TypeNode type;
	private TreeNode initializer;
	
	// constructor
	public DeclarationNode(String name2, TypeNode t, TreeNode i)
	{
		name = name2;
		type = t;
		initializer = i;
	}
	
	// get methods
	public String getName()
	{
		return name;
	}
	
	public TypeNode getType()
	{
		return type;
	}
	
	public TreeNode getInitializer()
	{
		return initializer;
	}
	
	public String toString()
	{
		return "[Declaration: " + name + ": " + type + " -> " + initializer + "]";
	}
}