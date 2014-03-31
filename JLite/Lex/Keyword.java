package Lex;

import java.util.Hashtable;
import java_cup.runtime.Symbol;
import Parse.Sym;

class Keyword extends Token {
  String keyword;
  Keyword(String s) {
    keyword = s;
  }

  Symbol token() {
    Integer i = (Integer) key_table.get(keyword);
    return new Symbol(i.intValue());
  }
  public String toString() {
    return "Keyword <"+keyword+">";
  }

  static private final Hashtable key_table = new Hashtable();
  static {
    key_table.put("class", new Integer(Sym.CLASS));
    key_table.put("else", new Integer(Sym.ELSE));
    key_table.put("extends", new Integer(Sym.EXTENDS));
    key_table.put("if", new Integer(Sym.IF));
    key_table.put("int", new Integer(Sym.INT));
    key_table.put("new", new Integer(Sym.NEW));
    key_table.put("return", new Integer(Sym.RETURN));
    key_table.put("this", new Integer(Sym.THIS));
    key_table.put("void", new Integer(Sym.VOID));
    key_table.put("while", new Integer(Sym.WHILE));
  }
}
