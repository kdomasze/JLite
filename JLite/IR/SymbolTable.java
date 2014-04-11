package IR;

import java.util.*;

public class SymbolTable {
  private HashMap<String, HashSet<Descriptor>> table;
  private SymbolTable parent;
  private HashSet<Descriptor> valueset;

  public SymbolTable() {
    table = new HashMap();
    valueset = new HashSet<Descriptor>();
  }

  public SymbolTable(SymbolTable parent) {
    this();
    this.parent = parent;
  }

  public void add(Descriptor d) {
    add(d.getSymbol(), d);
  }

  private void add(String name, Descriptor d) {
    if (!table.containsKey(name))
      table.put(name, new HashSet<Descriptor>());
    HashSet<Descriptor> hs=table.get(name);
    hs.add(d);
    valueset.add(d);
  }

  /** getAllDescriptorSet returns all descriptors that match name
   * including from parent scopes.*/

  public Set<Descriptor> getDescriptorsSet(String name) {
    Set<Descriptor> hs=(parent!=null)?parent.getDescriptorsSet(name):new HashSet<Descriptor>();

    if (table.containsKey(name)) {
      hs.addAll(table.get(name));
    }
    return hs;
  }

  /** getSetFromSameScope returns only descriptors that match name
   * from the same scope.*/

  public Set getSetFromSameScope(String name) {
    if (table.containsKey(name)) {
      HashSet<Descriptor> hs=table.get(name);
      return hs;
    } else
      return new HashSet<Descriptor>();
  }


  /** get looks up a name, and if we can't find it, we try the parent
   * scope. */

  public Descriptor get(String name) {
    Descriptor d = getFromSameScope(name);
    if (d != null)
      return d;

    if(parent != null) {
      d = parent.get(name);
      if (d!=null)
        return d;
    }
    
    return null;
  }

  /** getFromSameScopeee looks up a name in only the current scope. */

  public Descriptor getFromSameScope(String name) {
    if (table.containsKey(name)) {
      HashSet<Descriptor> hs= table.get(name);
      return hs.iterator().next();
    } else
      return null;

  }

  /** Returns the set of names. */

  public Set<String> getNamesSet() {
    return table.keySet();
  }

  /**Returns the set of descriptors. */
  public Set<Descriptor> getDescriptorsSet() {
    return valueset;
  }

  /** Returns all descriptors from all scopes. */
  public Set<Descriptor> getAllDescriptorsSet() {
    Set<Descriptor> hs=null;
    if (parent!=null)
      hs=parent.getAllDescriptorsSet();
    else
      hs=new HashSet<Descriptor>();

    hs.addAll(valueset);
    return hs;
  }

  /** Returns whether the current scope contains a descriptor with the
   * given name. */
  public boolean contains(String name) {
    return (get(name) != null);
  }


  /** Returns the parent SymbolTable. */
  public SymbolTable getParent() {
    return parent;
  }

  /** Sets the parent SymbolTable. */
  public void setParent(SymbolTable parent) {
    this.parent = parent;
  }

  public String toString() {
    return table.toString();
  }
}
