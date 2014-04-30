package IR.Flat;

import java.util.HashMap;
import java.util.Set;

import IR.*;
import IR.Tree.*;

public class BuildFlat
{
	State state;
	HashMap<Descriptor, FlatNode> TAC = new HashMap();
	
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
			
			// iterate over methods
			for(MethodDescriptor Method : methodSet)
			{
				BlockNode bn = state.getMethodBody(Method);
				NodePair np = flattenBlockNode(bn);
				
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
	
	public NodePair flattenBlockNode(BlockNode bn)
	{
		FlatNode begin = null;
		FlatNode end = null;
		
		for(int i = 0; i<bn.size(); i++)
		{
			NodePair np = flattenBlockStatementNode(bn.get(i));
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
	
	public NodePair flattenBlockStatementNode(TreeNode SubTree)
	{
		NodePair np = null;
		TempDescriptor out = null;
		
		if(SubTree instanceof ExpressionNode)
		{
			np = FlattenExpression(SubTree, out);
		}
		else if(SubTree instanceof IfStatementNode)
		{
			np = FlattenIf(SubTree, out);
		}
		else if(SubTree instanceof LoopNode)
		{
			np = FlattenLoop(SubTree, out);
		}
		else
		{
			throw new Error("You dun goof.");
		}
		
		return np;
	}
	
	public void Store(Descriptor desc, FlatNode fn)
	{
		TAC.put(desc, fn);
	}
}
