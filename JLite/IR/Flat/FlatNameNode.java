package IR.Flat;

import IR.TypeDescriptor;

public class FlatNameNode extends FlatNode
{
	TypeDescriptor type;
	TempDescriptor dst;

	public FlatNameNode(TypeDescriptor type, TempDescriptor dst)
	{
		this.type = type;
		this.dst = dst;
	}

	public String toString()
	{
		String str = "FlatNameNode_" + dst;
		return str;
	}
}
