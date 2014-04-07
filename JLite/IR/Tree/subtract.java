package IR.Tree;

import java.util.Vector;

public class subtract extends expression
{
	Vector<String>Identifier = new Vector<String>(2,1);

	public subtract(String i)
	{
		Identifier.add(i);
	}

	public void subtractIdentifier(String i)
	{
		Identifier.add(i);
	}
}