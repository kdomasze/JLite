#ifndef RUNTIME_H
#define RUNTIME_H

struct BaseObject {
  int type;
};

typedef struct BaseObject * ObjectPtr;
__attribute__((malloc)) void * allocate_new(int type);

#endif
