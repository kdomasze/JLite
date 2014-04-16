package IR.Tree;

import java.util.Map;
import java.util.Set;

public class MethodInvokeNode extends ExpressionNode
{
	private NameNode Name;
	private Map<String, TreeNode> ArgumentMap;
	
	//constructor
	public MethodInvokeNode(NameNode N)
	{
		Name = N;
	}
	
	public void addArgumentMap(String N, FieldAccessNode F)
	{
		ArgumentMap.put(N, F);
	}
	public void addArgumentMap(String N, OpNode O)
	{
		ArgumentMap.put(N, O);
	}
	
	//accessors
	public NameNode getNameNode()
	{
		return Name;
	}
	
	public Set<NameNode> getAllArgumentNames()
	{
		return ArgumentMap.keySet();
	}
	public Object getAgruments(NameNode N)
	{
		return ArgumentMap.get(N);
	}
	
	public String toString()
	{
		return "[MethodInvoke: " + Name + ": " + ArgumentMap + "]";
	}
}