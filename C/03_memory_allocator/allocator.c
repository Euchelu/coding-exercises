/*
 * Allocator de memoria propio: implementa malloc/free/calloc/realloc
 * desde cero usando sbrk(), sin apoyarse en la libc para la gestion real.
 *
 * Estrategia: lista doblemente enlazada de bloques (libres y ocupados),
 * en orden de direccion de memoria. Bloques contiguos y libres se
 * fusionan (coalescing) al liberar, y un bloque libre suficientemente
 * grande se puede partir (splitting) al reservar.
 */

#define _DEFAULT_SOURCE /* necesario para que glibc declare sbrk() con -std=c11 */

#include "allocator.h"
#include <unistd.h>
#include <stdio.h>

#define ALIGNMENT 8
#define ALIGN(size) (((size) + (ALIGNMENT - 1)) & ~(ALIGNMENT - 1))
#define MIN_PAYLOAD 8 /* por debajo de esto no compensa partir un bloque */

typedef struct block_header {
    size_t size;               /* tamaño util (payload), ya alineado */
    int free;
    struct block_header *prev;
    struct block_header *next;
} block_header;

#define HEADER_SIZE (sizeof(block_header))
#define BLOCK_PTR(payload) ((block_header *)((char *)(payload) - HEADER_SIZE))
#define PAYLOAD_PTR(block) ((void *)((char *)(block) + HEADER_SIZE))

static block_header *heap_head = NULL;
static block_header *heap_tail = NULL;

/* Busca en la lista un bloque libre con espacio suficiente (first-fit) */
static block_header *find_free_block(size_t size) {
    block_header *cur = heap_head;
    while (cur) {
        if (cur->free && cur->size >= size) return cur;
        cur = cur->next;
    }
    return NULL;
}

/* Pide memoria nueva al sistema operativo y la engancha al final de la lista */
static block_header *request_block(size_t size) {
    block_header *block = (block_header *)sbrk((intptr_t)(HEADER_SIZE + size));
    if (block == (void *)-1) return NULL;

    block->size = size;
    block->free = 0;
    block->next = NULL;
    block->prev = heap_tail;

    if (heap_tail) heap_tail->next = block;
    else heap_head = block;
    heap_tail = block;

    return block;
}

/* Si el bloque tiene mucho mas espacio del necesario, lo divide en dos */
static void split_block(block_header *block, size_t size) {
    size_t remaining = block->size - size;
    if (remaining < HEADER_SIZE + MIN_PAYLOAD) return; /* no compensa partir */

    block_header *new_block = (block_header *)((char *)PAYLOAD_PTR(block) + size);
    new_block->size = remaining - HEADER_SIZE;
    new_block->free = 1;
    new_block->prev = block;
    new_block->next = block->next;

    if (block->next) block->next->prev = new_block;
    else heap_tail = new_block;

    block->next = new_block;
    block->size = size;
}

/* Fusiona 'block' con su vecino siguiente si ambos estan libres y son contiguos */
static void coalesce(block_header *block) {
    block_header *next = block->next;
    if (next && next->free) {
        block->size += HEADER_SIZE + next->size;
        block->next = next->next;
        if (next->next) next->next->prev = block;
        else heap_tail = block;
    }
}

void *my_malloc(size_t size) {
    if (size == 0) return NULL;
    size = ALIGN(size);

    block_header *block = find_free_block(size);
    if (block) {
        split_block(block, size);
        block->free = 0;
    } else {
        block = request_block(size);
        if (!block) return NULL;
    }
    return PAYLOAD_PTR(block);
}

void my_free(void *ptr) {
    if (!ptr) return;
    block_header *block = BLOCK_PTR(ptr);
    block->free = 1;

    /* fusiona hacia adelante y, si el anterior tambien esta libre, hacia atras */
    coalesce(block);
    if (block->prev && block->prev->free) coalesce(block->prev);
}

void *my_calloc(size_t nmemb, size_t size) {
    size_t total = nmemb * size;
    if (nmemb != 0 && total / nmemb != size) return NULL; /* overflow */

    void *ptr = my_malloc(total);
    if (ptr) {
        char *bytes = (char *)ptr;
        for (size_t i = 0; i < total; i++) bytes[i] = 0;
    }
    return ptr;
}

void *my_realloc(void *ptr, size_t size) {
    if (!ptr) return my_malloc(size);
    if (size == 0) { my_free(ptr); return NULL; }

    block_header *block = BLOCK_PTR(ptr);
    size_t aligned = ALIGN(size);
    if (block->size >= aligned) {
        split_block(block, aligned);
        return ptr;
    }

    void *new_ptr = my_malloc(size);
    if (!new_ptr) return NULL;

    size_t to_copy = block->size < aligned ? block->size : aligned;
    char *src = (char *)ptr;
    char *dst = (char *)new_ptr;
    for (size_t i = 0; i < to_copy; i++) dst[i] = src[i];

    my_free(ptr);
    return new_ptr;
}

void heap_dump(void) {
    block_header *cur = heap_head;
    int i = 0;
    printf("--- heap (%zu bytes de overhead por bloque) ---\n", HEADER_SIZE);
    while (cur) {
        printf("  [%d] %p size=%-6zu %s\n", i, (void *)cur, cur->size,
               cur->free ? "libre" : "ocupado");
        cur = cur->next;
        i++;
    }
    printf("-----------------------------------------------\n");
}
