package IR.Tree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import IR.ClassDescriptor;
import IR.Descriptor;
import IR.MethodDescriptor;
import IR.NameDescriptor;
import IR.Program;
import IR.SymbolTable;
import IR.SymbolTableDescriptor;
import IR.TypeDescriptor;
import IR.VarDescriptor;

/***
 * Implement the check function for each type of AST Node
 * 
 */
public class SemanticCheck
{
	private SymbolTable nameTable = new SymbolTable();
	private BuildAST program;
	
	public SemanticCheck(BuildAST program2)
	{
		program = program2;
	}
	
	public void run()
	{
		semanticCheck1();
	}
	
	public void semanticCheck1()
	{
		// get an array of the class names
		Object[] tempClassNames = program.program.getClasses().getNamesSet().toArray();
		String[] classNames = Arrays.copyOf(tempClassNames, tempClassNames.length, String[].class);
		SymbolTable classes = program.program.getClasses();
		
		// check for duplicate class names
		checkClassNames(classNames, classes);
		
		// step into each class to check fields and methods
		for(int i = 0; i < classNames.length; i++)
		{
			// get one of the class descriptors
			ClassDescriptor classDesc = (ClassDescriptor) classes.get(classNames[i]);
			// check fields for duplicates
			checkFieldNames(classDesc);
			
			// get all the names for the method descriptors
			SymbolTable methods = classDesc.getMethodDescriptorTable();
			Object[] tempMethodsNames = methods.getNamesSet().toArray();
			String[] methodNames = Arrays.copyOf(tempMethodsNames, tempMethodsNames.length, String[].class);
			
			// check method descriptor for duplicate names
			checkMethodNames(methods, methodNames, classDesc);
			
			// step into the body of the methods
			for(int j = 0; j < methodNames.length; j++)
			{
				// check the method body for duplicate vars
				checkVariableNames((MethodDescriptor)methods.get(methodNames[j]), classDesc);
				checkIfWhileBlocks((MethodDescriptor)methods.get(methodNames[j]), null, classDesc, null, null);
			}
		}
	}
	
	public void checkClassNames(String[] classNames, SymbolTable classes)
	{
		// check class names for duplicates
		for(int i = 0; i < classNames.length; i++)
		{
			if(classes.getDescriptorsSet(classNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + classNames[i] + "' identifier has duplicate.");
			}
			
			// create new symbol table inside of name table for classes
			nameTable.add(new SymbolTableDescriptor(classNames[i], nameTable));
		}
	}
	
	public void checkFieldNames(ClassDescriptor classDesc)
	{
		// get names of all fields
		SymbolTable fields = classDesc.getFieldDescriptorTable();
		Object[] tempFieldNames = fields.getNamesSet().toArray();
		String[] fieldNames = Arrays.copyOf(tempFieldNames, tempFieldNames.length, String[].class);
		
		// create a new symbol table for all fields
		SymbolTableDescriptor classSymbolTable = ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol()));
		classSymbolTable.addToSymbolTable(new SymbolTableDescriptor("Field", ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol())).getSymbolTable()));
		
		SymbolTable FieldSymbolTable = ((SymbolTableDescriptor)classSymbolTable.getSymbolTable().get("Field")).getSymbolTable();
		
		// check fields for duplicates
		for(int i = 0; i < fieldNames.length; i++)
		{
			if(fields.getDescriptorsSet(fieldNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + fieldNames[i] + "' identifier has duplicate @'" + classDesc.getSymbol() + "'.");
			}
			
			// add field names to symbol table
			FieldSymbolTable.add(new NameDescriptor(fields.get(fieldNames[i]).getSymbol()));
		}
	}
	
	public void checkMethodNames(SymbolTable methods, String[] methodNames, ClassDescriptor classDesc)
	{
		// create a new symbol table for all methods
		SymbolTableDescriptor classSymbolTable = ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol()));
		
		// check methods for duplicates
		for(int i = 0; i < methodNames.length; i++)
		{
			if(methods.getDescriptorsSet(methodNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + methodNames[i] + "' identifier has duplicate @'" + classDesc.getSymbol() + "'.");
			}
			
			// add method names to symbol table
			classSymbolTable.addToSymbolTable(new SymbolTableDescriptor(methodNames[i], classSymbolTable.getSymbolTable()));
		}
	}
	
	public void checkVariableNames(MethodDescriptor method, ClassDescriptor classDesc)
	{
		
		// create a new symbol table for all vars
		SymbolTableDescriptor classSymbolTable = ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol()));
		SymbolTableDescriptor methodSymbolTable = ((SymbolTableDescriptor)classSymbolTable.getSymbolTable().get(method.getSymbol()));
		
		methodSymbolTable.addToSymbolTable(new SymbolTableDescriptor("Vars", methodSymbolTable.getSymbolTable()));
		
		SymbolTable VarSymbolTable = ((SymbolTableDescriptor)methodSymbolTable.getSymbolTable().get("Vars")).getSymbolTable();
		
		// fill vector with block statement nodes
		Vector<BlockStatementNode> blockStatementVector = ((BlockNode)method.getASTTree()).getblockStatementVector();
		
		SymbolTable tempVarSymbolTable = new SymbolTable();
		
		for(int i = 0; i < blockStatementVector.size(); i++)
		{
			if(blockStatementVector.elementAt(i) instanceof DeclarationNode)
			{
				String name = ((DeclarationNode)blockStatementVector.elementAt(i)).getName();
				TypeDescriptor type = new TypeDescriptor(null, ((DeclarationNode)blockStatementVector.elementAt(i)).getType());
				ExpressionNode expression = ((DeclarationNode)blockStatementVector.elementAt(i)).getInitializer();
				
				// add vars to temp symbol table
				tempVarSymbolTable.add(new VarDescriptor(name, type, expression));
			}
		}
		
		// check vars for duplicates
		Object[] tempVarNames = tempVarSymbolTable.getNamesSet().toArray();
		String[] varNames = Arrays.copyOf(tempVarNames, tempVarNames.length, String[].class);
		
		for(int i = 0; i < varNames.length; i++)
		{
			if(tempVarSymbolTable.getDescriptorsSet(varNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + varNames[i] + "' identifier has duplicate @'" + method.getSymbol() + "'.");
			}
			
			// add var names to symbol table
			VarSymbolTable.add(new NameDescriptor(varNames[i]));
		}
	}
	
	public void checkIfWhileBlocks(MethodDescriptor method, BlockStatementNode node, ClassDescriptor classDesc, String symbolTableName, SymbolTableDescriptor symbolTable)
	{
		// create a new symbol table for all vars
		SymbolTableDescriptor classSymbolTable;
		SymbolTableDescriptor methodSymbolTable;
		Vector<BlockStatementNode> blockStatementVector = null;
		// fill vector with block statement nodes
		if(method != null)
		{
			classSymbolTable = ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol()));
			methodSymbolTable = ((SymbolTableDescriptor)classSymbolTable.getSymbolTable().get(method.getSymbol()));
			blockStatementVector = ((BlockNode)((MethodDescriptor)method).getASTTree()).getblockStatementVector();
		}
		else if(symbolTableName.startsWith("I"))
		{
			methodSymbolTable = symbolTable;
			blockStatementVector = ((IfStatementNode)node).getBlock().getblockStatementVector();
		}
		else if(symbolTableName.startsWith("W"))
		{
			methodSymbolTable = symbolTable;
			blockStatementVector = ((WhileStatementNode)node).getBlock().getblockStatementVector();
		}
		else 
		{
			throw new Error("You messed up");
		}
		
		SymbolTable tempVarSymbolTable = new SymbolTable();
		int ifCount = 0;
		int whileCount = 0;
		for(int i = 0; i < blockStatementVector.size(); i++)
		{
			if(blockStatementVector.elementAt(i) instanceof IfStatementNode)
			{
				methodSymbolTable.addToSymbolTable(new SymbolTableDescriptor("If" + ifCount, methodSymbolTable.getSymbolTable()));
				checkBlockNode(blockStatementVector.elementAt(i), methodSymbolTable, "If" + ifCount);
				checkIfWhileBlocks(null, blockStatementVector.elementAt(i), classDesc, "If" + ifCount, methodSymbolTable);
				ifCount++;
			}
			else if(blockStatementVector.elementAt(i) instanceof WhileStatementNode)
			{
				methodSymbolTable.addToSymbolTable(new SymbolTableDescriptor("While" + whileCount, methodSymbolTable.getSymbolTable()));
				checkBlockNode(blockStatementVector.elementAt(i), methodSymbolTable, "While" + whileCount);
				checkIfWhileBlocks(null, blockStatementVector.elementAt(i), classDesc, "While" + whileCount, methodSymbolTable);
				whileCount++;
			}
		}
	}
	
	public void checkBlockNode(BlockStatementNode block, SymbolTableDescriptor table, String symbolTableName)
	{
		Vector<BlockStatementNode> blockStatementVector = null;
		if(symbolTableName.startsWith("I"))
		{
			blockStatementVector = ((IfStatementNode)block).getBlock().getblockStatementVector();
		}
		else if(symbolTableName.startsWith("W"))
		{
			blockStatementVector = ((WhileStatementNode)block).getBlock().getblockStatementVector();
		}
		else 
		{
			throw new Error("You messed up");
		}
		SymbolTable tempVarSymbolTable = new SymbolTable();
		SymbolTable VarSymbolTable = ((SymbolTableDescriptor)table.getSymbolTable().get(symbolTableName)).getSymbolTable();
		for(int i = 0; i < blockStatementVector.size(); i++)
		{
			if(blockStatementVector.elementAt(i) instanceof DeclarationNode)
			{
				String name = ((DeclarationNode)blockStatementVector.elementAt(i)).getName();
				TypeDescriptor type = new TypeDescriptor(null, ((DeclarationNode)blockStatementVector.elementAt(i)).getType());
				ExpressionNode expression = ((DeclarationNode)blockStatementVector.elementAt(i)).getInitializer();
				
				// add vars to temp symbol table
				tempVarSymbolTable.add(new VarDescriptor(name, type, expression));
			}
		}
		
		// check vars for duplicates
		Object[] tempVarNames = tempVarSymbolTable.getNamesSet().toArray();
		String[] varNames = Arrays.copyOf(tempVarNames, tempVarNames.length, String[].class);
		
		for(int i = 0; i < varNames.length; i++)
		{
			if(tempVarSymbolTable.getDescriptorsSet(varNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + varNames[i] + "' identifier has duplicate @'" + table.getSymbol() + "'.");
			}
			checkBlockParents(VarSymbolTable, varNames[i], table);
		}
		System.out.println(nameTable);		
	}
	
	public void checkBlockParents(SymbolTable varSymbolTable, String varName, SymbolTableDescriptor table)
	{
		SymbolTable temp = varSymbolTable.getParent();
		Set<Descriptor> tempSymbolTableDescriptor = temp.getDescriptorsSet();

		Object[] tempArray = tempSymbolTableDescriptor.toArray();
		Descriptor[] tempArrayDescriptor = Arrays.copyOf(tempArray, tempArray.length, Descriptor[].class);
		for(int j = 0; j < tempSymbolTableDescriptor.size() ; j++)
		{
			SymbolTable parentSymbolTable = ((SymbolTableDescriptor)tempArrayDescriptor[j]).getSymbolTable();
			Set<Descriptor> parentSet = parentSymbolTable.getDescriptorsSet();
			Object[] tempParentArray = parentSet.toArray();
			Descriptor[] parentArrayDescriptor = Arrays.copyOf(tempParentArray, tempParentArray.length, Descriptor[].class);

			for(int k = 0; k < parentArrayDescriptor.length; k++)
			{					
				if(varName.equals(parentArrayDescriptor[k].getSymbol()))
				{
					throw new Error("[ERROR_01] '" + varName + "' identifier has duplicate @'" + table.getSymbol() + "'.");
				}
			}
		}
		// add var names to symbol table
		varSymbolTable.add(new NameDescriptor(varName));
		
		if(temp.getParent().contains("Vars"))
		{
			checkBlockParents(varSymbolTable.getParent(), varName, table);
		}
	}
}
