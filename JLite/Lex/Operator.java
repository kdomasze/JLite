package Lex;

import java.util.Hashtable;
import java_cup.runtime.Symbol;
import Parse.Sym;

class Operator extends Token {
  String which;
  Operator(String which) {
    this.which = which;
  }

  public String toString() {
    return "Operator <"+which+">";
  }

  Symbol token() {
    Integer i = (Integer) op_table.get(which);
    return new Symbol(i.intValue());
  }

  static private final Hashtable op_table = new Hashtable();
  static {
    op_table.put("=", new Integer(Sym.EQ));
    op_table.put(">", new Integer(Sym.GT));
    op_table.put("<", new Integer(Sym.LT));
    op_table.put("!", new Integer(Sym.NOT));
    op_table.put("==", new Integer(Sym.EQEQ));
    op_table.put("!=", new Integer(Sym.NOTEQ));
    op_table.put("+", new Integer(Sym.PLUS));
    op_table.put("-", new Integer(Sym.MINUS));
    op_table.put("*", new Integer(Sym.MULT));
    op_table.put("/", new Integer(Sym.DIV));
    op_table.put("&", new Integer(Sym.AND));
    op_table.put("|", new Integer(Sym.OR));
    op_table.put("^", new Integer(Sym.XOR));
  }
}
