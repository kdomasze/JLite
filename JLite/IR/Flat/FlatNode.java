package IR.Flat;

import java.util.Vector;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class FlatNode
{
	protected Vector<FlatNode> next;
	protected Vector<FlatNode> prev;

	public FlatNode()
	{
		next = new Vector<FlatNode>();
		prev = new Vector<FlatNode>();
	}

	public void addNext(FlatNode fn)
	{
		next.add(fn);
	}

	public void addPrev(FlatNode fn)
	{
		prev.add(fn);
	}

	public int numNext()
	{
		return next.size();
	}

	public FlatNode getNext(int i)
	{
		return next.get(i);
	}

	public int numPrev()
	{
		return prev.size();
	}

	public FlatNode getPrev(int i)
	{
		return prev.get(i);
	}
}
