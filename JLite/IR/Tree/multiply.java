package IR.Tree;

import java.util.Vector;

public class multiply extends expression
{
	Vector<String>Identifier = new Vector<String>(2,1);
	
	public multiply(String i)
	{
		Identifier.add(i);
	}
	
	public void addIdentifier(String i)
	{
		Identifier.add(i);
	}
}