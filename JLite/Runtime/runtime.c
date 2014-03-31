#include <stdlib.h>
#include <stdio.h>
#include "runtime.h"
#include "methodheaders.h"

__attribute__((malloc)) void * allocate_new(int type) {
  ObjectPtr v=calloc(1,classsize[type]);
  v->type=type;
  return v;
}

int ___System______input____(struct ___System___ * ___this___) {
  return getchar();
}

void ___System______output____I(struct ___System___ * ___this___, int ___out___) {
  putchar(___out___);
}
