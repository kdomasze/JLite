package IR;

import Parse.ParseNode;
import Parse.ParseNodeVector;

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
	
	
	//Does not return or use symboltable
	public void ParseClassBody(SymbolTable table, ParseNode node)
	{
		if(node.getLabel()=="class_body_declaration_list")
		{
			ParseNodeVector children = node.getChildren();
			int size = children.size();
			int i = 0;
			ParseNode child;
			while(i < size)
			{
				child = children.elementAt(i);
				if(child.getLabel() == "field")
				{
					ParseField(table,child);
				}
				else if(child.getLabel() == "method")
				{
					ParseMethod(table,child);
				}
			}
		}
	}
	public void ParseField(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseMethod(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseMethodParameters(SymbolTable table, ParseNode node)
	{
		if(node.getLabel() == "parameters")
		{
			ParseNode method = node.getFirstChild();
			if(method.getLabel() == "formal_parameter_list")
			{
				ParseNodeVector children = method.getChildren();
				int size = children.size();
				int i = 0;
				while(i < size)
				{
					ParseNode child = children.elementAt(i);
					ParseField(table,child);
				}
			}
		}
	}
	
	public void ParseBlockBody(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseExpression(SymbolTable table, ParseNode node)
	{
		
	}
	
	public void ParseReturn(SymbolTable table, ParseNode node)
	{
		if(node.getLabel() == "returntype")
		{
			ParseNode child = node.getFirstChild();
			
		}
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