package IR.Tree;

import java.util.Vector;

public class Add extends expression
{
	Vector<String>Identifier = new Vector<String>(2,1);
	
	public Add(String i)
	{
		Identifier.add(i);
	}
	
	public void addIdentifier(String i)
	{
		Identifier.add(i);
	}
}