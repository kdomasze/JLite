package IR;

public class SymbolTableDescriptor extends Descriptor
{
	private SymbolTable symbolTable;
	
	public SymbolTableDescriptor(String name, SymbolTable p)
	{
		super(name);
		symbolTable = new SymbolTable(p);
	}
	
	//get
	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}
	
	//put
	public void addToSymbolTable(Descriptor d)
	{
		symbolTable.add(d);
	}
	
	public String toString()
	{
		return symbolTable.toString();
	}
}
