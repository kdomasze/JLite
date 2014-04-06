package IR.Tree;

import java.util.Vector;

public class add extends expression
{
	Vector<String>Identifier = new Vector<String>(2,1);
	
	public add(String i)
	{
		Identifier.add(i);
	}
	
	public void addIdentifier(String i)
	{
		Identifier.add(i);
	}
}