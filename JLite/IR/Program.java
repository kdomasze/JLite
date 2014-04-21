package IR;

public class Program
{
	private SymbolTable Classes = new SymbolTable();
	
	public void addClass(ClassDescriptor cD)
	{
		Classes.add(cD);
	}
	
	public SymbolTable getClasses()
	{
		return Classes;
	}
	
	public String toString()
	{
		return Classes.toString();
	}
}
