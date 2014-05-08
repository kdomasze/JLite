package IR.Flat;

public class NodePair
{
	FlatNode begin;
	FlatNode end;
	
	TempDescriptor tmp = null;
	
	public NodePair(FlatNode begin, FlatNode end)
	{
		this.begin = begin;
		this.end = end;
	}
	
	public NodePair(FlatNode begin, FlatNode end, TempDescriptor t)
	{
		this.begin = begin;
		this.end = end;
		this.tmp = t;
	}
	
	public FlatNode getBegin()
	{
		return begin;
	}
	
	public FlatNode getEnd()
	{
		return end;
	}
	
	public String toString()
	{
		return begin + "\n" + end;
	}
}
