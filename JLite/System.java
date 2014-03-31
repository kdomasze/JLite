class System {
  int input();
  void output(int out);
  void printInt(int x) {
    if (x==0)
      output('0');
    else {
      if (x<0) {
        output('-');
        x=0-x;
      }
      printRecurse(x);
    }
  }
  void printRecurse(int x) {
    if (x==0)
      return;
    int shifted=x/10;
    printRecurse(shifted);
    int digit='0'+(x-(shifted*10));
    output(digit);
  }
}
