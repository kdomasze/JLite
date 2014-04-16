package IR.Tree;

public class Operation extends TreeNode
{
	public static final int ADD = 0;
	public static final int SUB = 1;
	public static final int MULT = 2;
	public static final int DIV = 3;
	public static final int LT = 4;
	public static final int GT = 5;
	public static final int EQ = 6;
	public static final int NEQ = 7;
	public static final int BIT_OR = 8;
	public static final int BIT_AND = 9;
	public static final int BIT_XOR = 10;
	public static final int LOG_OR = 11;
	public static final int LOG_AND = 12;
	public static final int NOT = 13;

	private int operation;

	public Operation(int o)
	{
		operation = o;
	}

	public int getOp()
	{
		return operation;
	}

	public String toString()
	{
		switch (operation)
		{
		case ADD:
			return "+";
		case SUB:
			return "-";
		case MULT:
			return "*";
		case DIV:
			return "/";
		case LT:
			return "<";
		case GT:
			return ">";
		case EQ:
			return "==";
		case NEQ:
			return "!=";
		case BIT_OR:
			return "|";
		case BIT_AND:
			return "&";
		case BIT_XOR:
			return "^";
		case LOG_OR:
			return "||";
		case LOG_AND:
			return "&&";
		case NOT:
			return "!";
		default:
			throw new Error();
		}
	}
}
