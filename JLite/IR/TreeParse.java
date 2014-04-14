package IR;

import Parse.ParseNode;

public class TreeParse
{
	private SymbolTable table;
	private ParseNode node;
	
	//Constructor
	public TreeParse(SymbolTable t, ParseNode n)
	{
		node = n;
		table = t;
	}
	
	//getters
	public ParseNode getParseNode()
	{
		return node;
	}
	
	public SymbolTable getSymbolTable()
	{
		return table;
	}
	
	//methods
	public void ParseClassDeclarer(SymbolTable table, ParseNode node)
	{
		
	}
	
	public void ClassBody(SymbolTable table, ParseNode node)
	{
		
	}
}