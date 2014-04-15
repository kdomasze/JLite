package IR;

import com.sun.org.apache.xpath.internal.ExpressionNode;

import IR.Tree.LiteralNode;
import IR.Tree.NameNode;
import IR.Tree.TypeNode;
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
		NameNode className = new NameNode(node.getChild("name").getFirstChild().getLabel());
		NameNode superClassName = new NameNode(node.getChild("super").getFirstChild().getLabel());
		// Call ParseClassBody once we know what we are getting back
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
		TypeNode varType = new TypeNode(node.getChild("type").getFirstChild().getLabel());
		NameNode varName = new NameNode(node.getChild("variable_declarator").getChild("single").getFirstChild().getLabel());
		LiteralNode varValue = new LiteralNode((int)node.getChild("variable_declarator").getChild("initializer").getChild("literal").getFirstChild().getLiteral());
		
		TypeDescriptor varTypeDesc = new TypeDescriptor("type", varType);
		
		VarDescriptor variable = new VarDescriptor(varName.getName(), varTypeDesc, varValue);
	}
	public void ParseIfStatement(SymbolTable table, ParseNode node)
	{
		
	}
	public void ParseWhilteStatement(SymbolTable table, ParseNode node)
	{
		
	}
}