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
	
	public void ParseClassBody(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseField(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseMethod(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseMethodParameters(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseBlockBody(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseExpression(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseReturn(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseVarDeclaration(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseIfStatement(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseWhilteStatement(SymbolTable table, ParseNode node)
	{
		
	}
}