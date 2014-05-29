#include <stdlib.h>
#include <stdio.h>
#include "runtime.h"
#include "methodheaders.h"

__attribute__((malloc)) void * allocate_new(int type) 
{
	ObjectPtr v = calloc(1, classsize[type]);
	v -> type = type;
	return v;
}

int System_input(struct System * this)
{
	int input;
	scanf("%d", &input);
	return input;
}

void System_output(struct System * this, int out)
{
	printf("%d\n", out);
}
