#ifndef ALLOCATOR_H
#define ALLOCATOR_H

#include <stddef.h>

void *my_malloc(size_t size);
void  my_free(void *ptr);
void *my_calloc(size_t nmemb, size_t size);
void *my_realloc(void *ptr, size_t size);

/* Imprime el estado actual del heap gestionado (direccion, tamaño, libre/ocupado) */
void heap_dump(void);

#endif
