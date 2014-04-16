package IR;

public class Program
{
	private SymbolTable Classes = new SymbolTable();
	
	public void addClass(ClassDescriptor cD)
	{
		Classes.add(cD);
	}
	
	public String toString()
	{
		return Classes.toString();
	}
}
