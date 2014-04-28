package IR;

public class Operation {
  public static final int BIT_OR=1;
  public static final int BIT_XOR=2;
  public static final int BIT_AND=3;
  public static final int EQUAL=4;
  public static final int NOTEQUAL=5;
  public static final int LT=6;
  public static final int GT=7;
  public static final int SUB=8;
  public static final int ADD=9;
  public static final int MULT=10;
  public static final int DIV=11;
  public static final int LOGIC_NOT=12;
  /* Flat Operations */
  public static final int ASSIGN=100;

  private int operation;
  public Operation(int op) {
    this.operation=op;
  }

  public Operation(String op) {
    this.operation=parseOp(op);
  }

  public int getOp() {
    return operation;
  }

  public static int parseOp(String st) {
    if (st.equals("bitwise_or"))
      return BIT_OR;
    else if (st.equals("bitwise_xor"))
      return BIT_XOR;
    else if (st.equals("bitwise_and"))
      return BIT_AND;
    else if (st.equals("equal"))
      return EQUAL;
    else if (st.equals("not_equal"))
      return NOTEQUAL;
    else if (st.equals("comp_lt"))
      return LT;
    else if (st.equals("comp_gt"))
      return GT;
    else if (st.equals("sub"))
      return SUB;
    else if (st.equals("add"))
      return ADD;
    else if (st.equals("mult"))
      return MULT;
    else if (st.equals("div"))
      return DIV;
    else if (st.equals("not"))
      return LOGIC_NOT;
    else
      throw new Error(st);
  }

  public String toString() {
    if (operation==LOGIC_NOT)
      return "not";
    else if (operation==BIT_OR)
      return "|";
    else if (operation==BIT_XOR)
      return "^";
    else if (operation==BIT_AND)
      return "&";
    else if (operation==EQUAL)
      return "==";
    else if (operation==NOTEQUAL)
      return "!=";
    else if (operation==LT)
      return "<";
    else if (operation==GT)
      return ">";
    else if (operation==SUB)
      return "-";
    else if (operation==ADD)
      return "+";
    else if (operation==MULT)
      return "*";
    else if (operation==DIV)
      return "/";
    else if (operation==ASSIGN)
      return "assign";
    else throw new Error("op="+operation);
  }


}
