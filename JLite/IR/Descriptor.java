package IR;

/**
 * Descriptor
 *
 * represents a symbol in the language (var name, function name, etc).
 */

public abstract class Descriptor {
  protected String name;

  public Descriptor(String name) {
    this.name = name;
  }

  public void setName(String n)
  {
	  name = n;
  }
  
  public String toString() {
    return getSymbol();
  }

  public String getSymbol() {
    return name;
  }
  
}
