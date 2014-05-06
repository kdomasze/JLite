package IR.Flat;

import java.util.HashMap;
import java.util.Set;

import IR.*;
import IR.Tree.*;


public class BuildFlat
{
	State state;
	HashMap<Descriptor, FlatNode> TAC = new HashMap();
	int tempDescCount = 0;
	
	public BuildFlat(State s)
	{
		state = s;
	}
	
	public void flatten()
	{
		// grab symbol table of all classes
		SymbolTable classes = state.getClassSymbolTable();
		Set<ClassDescriptor> classSet = classes.getAllValueSet();
		
		// iterate over classes
		for(ClassDescriptor Class : classSet)
		{			
			// grab symbol table for all methods in Class
			SymbolTable methods = Class.getMethodTable();
			Set<MethodDescriptor> methodSet = methods.getAllValueSet();
			int i = 0;
			// iterate over methods
			for(MethodDescriptor Method : methodSet)
			{
				BlockNode bn = state.getMethodBody(Method);
				
				SymbolTable mdst = Method.getParameterTable();
				i++;
				
				NodePair np = flattenBlockNode(bn, mdst);
				
				FlatNode fn = np.getBegin();
				
				FlatMethod fm = new FlatMethod(Method);
				if(!bn.hasNoCode())
				{
					fm.addNext(fn);
				}
				
				fm.addFormalParameter(Method.getParameterTable());
				Store(Method, fm);
			}
		}
	}
	
	public NodePair flattenBlockNode(BlockNode bn, SymbolTable nametable)
	{
		FlatNode begin = null;
		FlatNode end = null;
		
		bn.getVarTable().setParent(nametable);
		
		for(int i = 0; i<bn.size(); i++)
		{
			if(bn.get(i) instanceof DeclarationNode)
			{
				if(((DeclarationNode)(bn.get(i))).getExpression() == null)
				{
					continue;
				}
			}
			NodePair np = flattenBlockStatementNode(bn.get(i), bn.getVarTable());
			FlatNode np_begin = np.getBegin();
			FlatNode np_end = np.getEnd();
			if (begin == null)
			{
				begin = np_begin;
			}
			if(end == null)
			{
				end = np_end;
			}
			else
			{
				end.addNext(np_begin);
				end = np_end;
			} 
		}
		
		if (begin == null) 
		{
			end = begin = new FlatNop();
		}
		return new NodePair(begin,end);
	}
	
	public NodePair flattenBlockStatementNode(TreeNode SubTree, SymbolTable nametable)
	{
		NodePair np = null;
		TempDescriptor out = null;
		
		if(SubTree instanceof ExpressionNode)
		{
			np = FlattenExpression(SubTree, out);
		}
		else if(SubTree instanceof IfStatementNode)
		{
			//np = FlattenIf(SubTree, out);
		}
		else if(SubTree instanceof LoopNode)
		{
			//np = FlattenLoop(SubTree, out);
		}
		else if(SubTree instanceof ReturnNode)
		{
		
		}
		else if(SubTree instanceof DeclarationNode)
		{
			np = FlattenDeclarationNode(SubTree, out);
		}
		else
		{
			throw new Error("You dun goof.");
		}
		
		return np;
	}
	
	public NodePair FlattenExpression(TreeNode SubTree, TempDescriptor out)
	{
		FlatNode flat = null;
		
		if(SubTree instanceof OpNode)
		{
			flat = FlattenOpNode(SubTree, out);
		}
		else if(SubTree instanceof AssignmentNode)
		{
			flat = FlattenAssignmentNode(SubTree, out);
		}
		else if(SubTree instanceof ReturnNode)
		{
			flat = FlattenReturnNode(SubTree);
		}
		else if(SubTree instanceof CreateObjectNode)
		{
			flat = FlattenCreateObjectNode(SubTree);
		}
		else if(SubTree instanceof FieldAccessNode)
		{
			flat = FlattenFieldAccessNode(SubTree);
		}
		else if(SubTree instanceof MethodInvokeNode)
		{
			flat = FlattenMethodInvokeNode(SubTree);
		}
		else
		{
			flat = FlattenNopNode(SubTree);
		}
	
		return new NodePair(null,null);
		
	}

	public NodePair FlattenDeclarationNode(TreeNode SubTree, TempDescriptor out)
	{
		FlatNode n1;
		FlatNode n2 = new FlatNop();
		DeclarationNode dn = (DeclarationNode)SubTree;
		NameNode nn = new NameNode(new NameDescriptor(dn.getVarDescriptor().getName()));
		AssignmentNode as = new AssignmentNode(nn, dn.getExpression());
		n1 = FlattenAssignmentNode((TreeNode)as, out);
				
		return new NodePair(n1, n2);
	}
	
	public void Store(Descriptor desc, FlatNode fn)
	{
		TAC.put(desc, fn);
	}
	
	public FlatNode FlattenOpNode(TreeNode SubTree, TempDescriptor out)
	{
		FlatNode flat = new FlatNode();
		
		
		
		return flat;
		
	}
	
	public FlatNode FlattenAssignmentNode(TreeNode SubTree, TempDescriptor out)
	{
		FlatNode flat = null;
		
		AssignmentNode as = ((AssignmentNode)SubTree);
		
		if(as.getSrc() instanceof NameNode)
		{
			out = new TempDescriptor("t" + tempDescCount, null);
			tempDescCount++;
			NameNode nn = ((NameNode)as.getSrc());
			flat = new FlatOpNode(out, new TempDescriptor(nn.getName().getIdentifier(), new TypeDescriptor(TypeDescriptor.NULL)), null, new Operation(Operation.ASSIGN));
		}
		else if(as.getSrc() instanceof LiteralNode)
		{
			out = new TempDescriptor("t" + tempDescCount, new TypeDescriptor(TypeDescriptor.INT));
			tempDescCount++;
			LiteralNode ln = ((LiteralNode)as.getSrc());
			flat = new FlatOpNode(out, new TempDescriptor("t" + tempDescCount, new TypeDescriptor(TypeDescriptor.INT), ((int)ln.getValue())), null, new Operation(Operation.ASSIGN));
		}
		else if(as.getSrc() instanceof OpNode)
		{
			OpNode on = ((OpNode)as.getSrc());
			flat = FlattenOpNode((TreeNode)on, out);
		}
		else
		{
			throw new Error("I can't let you do that, Dave.");
		}
		
		return flat;
	}
	
	public FlatNode FlattenCreateObjectNode(TreeNode SubTree)
	{
		FlatNode flat = null;
		return flat;
	}
	
	public FlatNode FlattenReturnNode(TreeNode SubTree)
	{
		FlatNode flat = null;
		return flat;
	}
	
	public FlatNode FlattenFieldAccessNode(TreeNode SubTree)
	{
		FlatNode flat = null;
		return flat;
	}
	
	public FlatNode FlattenMethodInvokeNode(TreeNode SubTree)
	{
		FlatNode flat = null;
		return flat;
	}
	
	public FlatNode FlattenNopNode(TreeNode SubTree)
	{
		FlatNode flat = null;
		return flat;
	}
	
	public String toString()
	{
		String returnString = "";
		
		for(FlatNode flat : TAC.values())
		{
			if(flat instanceof FlatMethod)
			{
				returnString += ((FlatMethod)flat).printMethod() + "\n";
			}
		}
		
		return returnString;
	}
}
