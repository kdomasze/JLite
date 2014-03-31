package Lex;

import java.io.IOException;
import java.io.Reader;
import java.io.LineNumberReader;
import Parse.Sym;

/* Java lexer.
 * Copyright (C) 2002 C. Scott Ananian <cananian@alumni.princeton.edu>
 * This program is released under the terms of the GPL; see the file
 * COPYING for more details.  There is NO WARRANTY on this code.
 */

public class Lexer {
  LineNumberReader reader;
  String line = null;
  int line_pos = 1;
  public int line_num = 0;
  LineList lineL = new LineList(-line_pos, null); // sentinel for line #0

  public Lexer(Reader reader) {
    this.reader = new LineNumberReader(new EscapedUnicodeReader(reader));
  }

  public java_cup.runtime.Symbol nextToken() throws java.io.IOException {
    java_cup.runtime.Symbol sym =
      lookahead==null?_nextToken():lookahead.get();
    last = sym;
    return sym;
  }
  private boolean shouldBePLT() throws java.io.IOException {
    // look ahead to see if this LT should be changed to a PLT
    if (last==null || last.sym!=Sym.IDENTIFIER)
      return false;
    if (lookahead==null) lookahead = new FIFO(new FIFO.Getter() {
                                                java_cup.runtime.Symbol next() throws java.io.IOException
                                                { return _nextToken(); }
                                              });
    int i=0;
    // skip past IDENTIFIER (DOT IDENTIFIER)*
    if (lookahead.peek(i++).sym != Sym.IDENTIFIER)
      return false;
    while (lookahead.peek(i).sym == Sym.DOT) {
      i++;
      if (lookahead.peek(i++).sym != Sym.IDENTIFIER)
        return false;
    }
    // now the next sym has to be one of LT GT COMMA EXTENDS IMPLEMENTS
    switch(lookahead.peek(i).sym) {
    default:
      return false;

    case Sym.LT:
    case Sym.GT:
    case Sym.COMMA:
    case Sym.EXTENDS:
      return true;
    }
  }
  private java_cup.runtime.Symbol last = null;
  private FIFO lookahead = null;
  public java_cup.runtime.Symbol _nextToken() throws java.io.IOException {
    /* tokens are:
     *  Identifiers/Keywords/true/false/null (start with java letter)
     *  numeric literal (start with number)
     *  character literal (start with single quote)
     *  string (start with double quote)
     *  separator (parens, braces, brackets, semicolon, comma, period)
     *  operator (equals, plus, minus, etc)
     *  whitespace
     *  comment (start with slash)
     */
    InputElement ie;
    int startpos, endpos;
    do {
      startpos = lineL.head + line_pos;
      ie = getInputElement();
      if (ie instanceof DocumentationComment)
        comment = ((Comment)ie).getComment();
    } while (!(ie instanceof Token));
    endpos = lineL.head + line_pos - 1;

    // System.out.println(ie.toString()); // uncomment to debug lexer.
    java_cup.runtime.Symbol sym = ((Token)ie).token();
    // fix up left/right positions.
    sym.left = startpos; sym.right = endpos;
    // return token.
    return sym;
  }
  public boolean debug_lex() throws java.io.IOException {
    InputElement ie = getInputElement();
    System.out.println(ie);
    return !(ie instanceof EOF);
  }

  String comment;
  public String lastComment() {
    return comment;
  }
  public void clearComment() {
    comment="";
  }

  InputElement getInputElement() throws java.io.IOException {
    if (line_num == 0)
      nextLine();
    if (line==null)
      return new EOF();
    if (line.length()<=line_pos) {      // end of line.
      nextLine();
      if (line==null)
        return new EOF();
    }

    switch (line.charAt(line_pos)) {

    // White space:
    case ' ':    // ASCII SP
    case '\t':    // ASCII HT
    case '\f':    // ASCII FF
    case '\n':    // LineTerminator
      return new WhiteSpace(consume());

    // EOF character:
    case '\020': // ASCII SUB
      consume();
      return new EOF();

    // Comment prefix:
    case '/':
      return getComment();

    // else, a Token
    default:
      return getToken();
    }
  }
  // May get Token instead of Comment.
  InputElement getComment() throws java.io.IOException {
    String comment;
    // line.charAt(line_pos+0) is '/'
    switch (line.charAt(line_pos+1)) {
    case '/': // EndOfLineComment
      comment = line.substring(line_pos+2);
      line_pos = line.length();
      return new EndOfLineComment(comment);

    case '*': // TraditionalComment or DocumentationComment
      line_pos += 2;
      if (line.charAt(line_pos)=='*') { // DocumentationComment
        return snarfComment(new DocumentationComment());
      } else { // TraditionalComment
        return snarfComment(new TraditionalComment());
      }

    default: // it's a token, not a comment.
      return getToken();
    }
  }

  Comment snarfComment(Comment c) throws java.io.IOException {
    StringBuffer text=new StringBuffer();
    while(true) { // Grab CommentTail
      while (line.charAt(line_pos)!='*') { // Add NotStar to comment.
        int star_pos = line.indexOf('*', line_pos);
        if (star_pos<0) {
          text.append(line.substring(line_pos));
          c.appendLine(text.toString()); text.setLength(0);
          line_pos = line.length();
          nextLine();
          if (line==null)
            throw new IOException("Unterminated comment at end of file.");
        } else {
          text.append(line.substring(line_pos, star_pos));
          line_pos=star_pos;
        }
      }
      // At this point, line.charAt(line_pos)=='*'
      // Grab CommentTailStar starting at line_pos+1.
      if (line.charAt(line_pos+1)=='/') { // safe because line ends with '\n'
        c.appendLine(text.toString()); line_pos+=2; return c;
      }
      text.append(line.charAt(line_pos++)); // add the '*'
    }
  }

  Token getToken() throws java.io.IOException {
    // Tokens are: Identifiers, Keywords, Literals, Separators, Operators.
    switch (line.charAt(line_pos)) {
    // Separators: (period is a special case)
    case '(':
    case ')':
    case '{':
    case '}':
    case '[':
    case ']':
    case ';':
    case ',':
      return new Separator(consume());

    // Operators:
    case '=':
    case '>':
    case '<':
    case '!':
    case '~':
    case '?':
    case ':':
    case '&':
    case '|':
    case '+':
    case '-':
    case '*':
    case '/':
    case '^':
    case '%':
      return getOperator();

    case '\'':
      return getCharLiteral();

    // a period is a special case:
    case '.':
      if (Character.digit(line.charAt(line_pos+1),10)!=-1)
        return getNumericLiteral();
      else return new Separator(consume());

    default:
      break;
    }
    if (Character.isJavaIdentifierStart(line.charAt(line_pos)))
      return getIdentifier();
    if (Character.isDigit(line.charAt(line_pos)))
      return getNumericLiteral();
    throw new IOException("Illegal character on line "+line_num);
  }

  static final String[] keywords = new String[] {
    "class",
    "else",
    "extends",
    "if",
    "int",
    "new", 
    "return",
    "this", "void",
    "while"
  };
  Token getIdentifier() throws java.io.IOException {
    // Get id string.
    StringBuffer sb = new StringBuffer().append(consume());

    if (!Character.isJavaIdentifierStart(sb.charAt(0)))
      throw new IOException("Invalid Java Identifier on line "+line_num);
    while (Character.isJavaIdentifierPart(line.charAt(line_pos)))
      sb.append(consume());
    String s = sb.toString();
    // Now check against boolean literals and null literal.
    if (s.equals("null")) return new NullLiteral();
    if (s.equals("true")) return new IntegerLiteral(1);
    if (s.equals("false")) return new IntegerLiteral(0);
    // use binary search.
    for (int l=0, r=keywords.length; r > l; ) {
      int x = (l+r)/2, cmp = s.compareTo(keywords[x]);
      if (cmp < 0) r=x; else l=x+1;
      if (cmp== 0) return new Keyword(s);
    }
    // not a keyword.
    return new Identifier(s);
  }
  NumericLiteral getNumericLiteral() throws java.io.IOException {
    int i;
    // 0x indicates Hex.
    if (line.charAt(line_pos)=='0' &&
        (line.charAt(line_pos+1)=='x' ||
         line.charAt(line_pos+1)=='X')) {
      line_pos+=2; return getIntegerLiteral(/*base*/ 16);
    }
    // otherwise scan to first non-numeric
    for (i=line_pos; Character.digit(line.charAt(i),10)!=-1; )
      i++;
    switch(line.charAt(i)) { // discriminate based on first non-numeric
    case 'L':
    case 'l':
    default:
      if (line.charAt(line_pos)=='0')
        return getIntegerLiteral(/*base*/ 8);
      return getIntegerLiteral(/*base*/ 10);
    }
  }
  NumericLiteral getIntegerLiteral(int radix) throws java.io.IOException {
    long val=0;
    while (Character.digit(line.charAt(line_pos),radix)!=-1)
      val = (val*radix) + Character.digit(consume(),radix);
    // we compare MAX_VALUE against val/2 to allow constants like
    // 0xFFFF0000 to get past the test. (unsigned long->signed int)
    if ((val/2) > Integer.MAX_VALUE ||
        val    < Integer.MIN_VALUE)
      throw new IOException("Constant does not fit in integer on line "+line_num);
    return new IntegerLiteral((int)val);
  }
  String getDigits() {
    StringBuffer sb = new StringBuffer();
    while (Character.digit(line.charAt(line_pos),10)!=-1)
      sb.append(consume());
    return sb.toString();
  }

  Operator getOperator() {
    char first = consume();
    char second= line.charAt(line_pos);

    switch(first) {
    // single-character operators.
    case '~':
    case '?':
    case ':':
      return new Operator(new String(new char[] {first}));

    // doubled operators
    case '+':
    case '-':
    case '&':
    case '|':
      if (first==second)
        return new Operator(new String(new char[] {first, consume()}));

    default:
      break;
    }
    // Check for trailing '='
    if (second=='=')
      return new Operator(new String(new char[] {first, consume()}));

    // Special-case '<<', '>>' and '>>>'
    if ((first=='<' && second=='<') || // <<
        (first=='>' && second=='>')) {  // >>
      String op = new String(new char[] {first, consume()});
      if (first=='>' && line.charAt(line_pos)=='>') // >>>
        op += consume();
      if (line.charAt(line_pos)=='=') // <<=, >>=, >>>=
        op += consume();
      return new Operator(op);
    }

    // Otherwise return single operator.
    return new Operator(new String(new char[] {first}));
  }

  IntegerLiteral getCharLiteral() throws java.io.IOException {
    char firstquote = consume();
    char val;
    switch (line.charAt(line_pos)) {
    case '\\':
      val = getEscapeSequence();
      break;

    case '\'':
      throw new IOException("Invalid character literal on line "+line_num);

    case '\n':
      throw new IOException("Invalid character literal on line "+line_num);

    default:
      val = consume();
      break;
    }
    char secondquote = consume();
    if (firstquote != '\'' || secondquote != '\'')
      throw new IOException("Invalid character literal on line "+line_num);
    return new IntegerLiteral((int)val);
  }

  char getEscapeSequence() throws java.io.IOException {
    if (consume() != '\\')
      throw new IOException("Invalid escape sequence on line " + line_num);
    switch(line.charAt(line_pos)) {
    case 'b':
      consume(); return '\b';

    case 't':
      consume(); return '\t';

    case 'n':
      consume(); return '\n';

    case 'f':
      consume(); return '\f';

    case 'r':
      consume(); return '\r';

    case '\"':
      consume(); return '\"';

    case '\'':
      consume(); return '\'';

    case '\\':
      consume(); return '\\';

    case '0':
    case '1':
    case '2':
    case '3':
      return (char) getOctal(3);

    case '4':
    case '5':
    case '6':
    case '7':
      return (char) getOctal(2);

    default:
      throw new IOException("Invalid escape sequence on line " + line_num);
    }
  }
  int getOctal(int maxlength) throws java.io.IOException {
    int i, val=0;
    for (i=0; i<maxlength; i++)
      if (Character.digit(line.charAt(line_pos), 8)!=-1) {
        val = (8*val) + Character.digit(consume(), 8);
      } else break;
    if ((i==0) || (val>0xFF)) // impossible.
      throw new IOException("Invalid octal escape sequence in line " + line_num);
    return val;
  }

  char consume() {
    return line.charAt(line_pos++);
  }
  void nextLine() throws java.io.IOException {
    line=reader.readLine();
    if (line!=null) line=line+'\n';
    lineL = new LineList(lineL.head+line_pos, lineL); // for error reporting
    line_pos=0;
    line_num++;
  }

  // Deal with error messages.
  public void errorMsg(String msg, java_cup.runtime.Symbol info) {
    int n=line_num, c=info.left-lineL.head;
    for (LineList p = lineL; p!=null; p=p.tail, n--)
      if (p.head<=info.left) {
        c=info.left-p.head; break;
      }
    System.err.println(msg+" at line "+n);
    num_errors++;
  }
  private int num_errors = 0;
  public int numErrors() {
    return num_errors;
  }

  class LineList {
    int head;
    LineList tail;
    LineList(int head, LineList tail) {
      this.head = head; this.tail = tail;
    }
  }
}
