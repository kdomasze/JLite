package IR.Flat;

import IR.MethodDescriptor;
import IR.SymbolTable;

import java.util.*;

public class FlatMethod extends FlatNode
{
	MethodDescriptor method;
	
	SymbolTable formalParameters = new SymbolTable();

	public FlatMethod(MethodDescriptor md)
	{
		method = md;
	}
	
	public void addFormalParameter(SymbolTable param)
	{
		formalParameters = param;
	}

	public MethodDescriptor getMethod()
	{
		return method;
	}

	public void check()
	{
		Set<FlatNode> set = getNodeSet();
		for (Iterator<FlatNode> setit = set.iterator(); setit.hasNext();)
		{
			FlatNode fn = setit.next();
			for (int i = 0; i < fn.numPrev(); i++)
			{
				FlatNode fnprev = fn.getPrev(i);
				if (!set.contains(fnprev))
				{
					System.out.println(fn + " has unreachable parent:" + i
							+ "  " + fnprev);
					System.out.println(printMethod());
					throw new Error();

				}
			}
		}
	}

	/** This method returns a set of the nodes in this flat representation */

	public Set<FlatNode> getNodeSet()
	{
		HashSet<FlatNode> tovisit = new HashSet<FlatNode>();
		HashSet<FlatNode> visited = new HashSet<FlatNode>();
		tovisit.add(this);
		while (!tovisit.isEmpty())
		{
			FlatNode fn = tovisit.iterator().next();
			tovisit.remove(fn);
			visited.add(fn);
			for (int i = 0; i < fn.numNext(); i++)
			{
				FlatNode nn = fn.getNext(i);
				if (nn == null)
					continue;
				if (!visited.contains(nn))
					tovisit.add(nn);
			}
		}
		return visited;
	}

	/**
	 * This method returns a string that is a human readable representation of
	 * this method.
	 */

	public String printMethod()
	{
		String st = method + " {\n";
		HashSet tovisit = new HashSet();
		HashSet visited = new HashSet();
		int labelindex = 0;
		Hashtable nodetolabel = new Hashtable();
		tovisit.add(this);
		FlatNode current_node = null;
		// Assign labels 1st
		// Node needs a label if it is
		while (!tovisit.isEmpty())
		{
			FlatNode fn = (FlatNode) tovisit.iterator().next();
			tovisit.remove(fn);
			visited.add(fn);

			for (int i = 0; i < fn.numNext(); i++)
			{
				FlatNode nn = fn.getNext(i);
				if (i > 0)
				{
					// 1) Edge >1 of node
					nodetolabel.put(nn, new Integer(labelindex++));
				}
				if (!visited.contains(nn) && !tovisit.contains(nn))
				{
					tovisit.add(nn);
				}
				else
				{
					// 2) Join point
					nodetolabel.put(nn, new Integer(labelindex++));
				}
			}
		}

		// Do the actual printing
		tovisit = new HashSet();
		visited = new HashSet();
		tovisit.add(this);
		while (current_node != null || !tovisit.isEmpty())
		{
			if (current_node == null)
			{
				current_node = (FlatNode) tovisit.iterator().next();
				tovisit.remove(current_node);
			}
			else
			{
				if (tovisit.contains(current_node))
					tovisit.remove(current_node);
			}
			visited.add(current_node);
			if (nodetolabel.containsKey(current_node))
			{
				st += "L" + nodetolabel.get(current_node) + ":\n";
				for (int i = 0; i < current_node.numPrev(); i++)
				{
					st += "i=" + i + " " + current_node.getPrev(i);
				}
				st += "\n";
			}
			if (current_node.numNext() == 0)
			{
				st += "   " + current_node.toString() + "\n";
				current_node = null;
			}
			else if (current_node.numNext() == 1)
			{
				st += "   " + current_node.toString() + "\n";
				FlatNode nextnode = current_node.getNext(0);
				if (visited.contains(nextnode))
				{
					st += "goto L" + nodetolabel.get(nextnode) + "\n";
					current_node = null;
				}
				else
					current_node = nextnode;
			}
			else if (current_node.numNext() == 2)
			{
				/* Branch */
				st += "   " + current_node.toString() + "goto L"
						+ nodetolabel.get(current_node.getNext(1)) + "\n";
				if (!visited.contains(current_node.getNext(1)))
					tovisit.add(current_node.getNext(1));
				if (visited.contains(current_node.getNext(0)))
				{
					st += "goto L" + nodetolabel.get(current_node.getNext(0))
							+ "\n";
					current_node = null;
				}
				else
					current_node = current_node.getNext(0);
			}
			else
				throw new Error();
		}
		return st + "}\n";
	}
}
