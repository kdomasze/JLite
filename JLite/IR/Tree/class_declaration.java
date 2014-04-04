package IR.Tree;

import java.util.Vector;

public class class_declaration
{
	String name;
	Vector<field> Field = new Vector<field>(1,1);
	Vector<method> Method = new Vector<method>(1,1);

	public class_declaration(String n)
	{
		name = n;
	}
	
	public void addField(type Type, String name)
	{
		field tempField = new field(Type, name);
		Field.add(tempField);
	}
}