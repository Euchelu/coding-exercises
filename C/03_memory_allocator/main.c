/*
 * Demo del allocator propio (my_malloc/my_free/my_calloc/my_realloc).
 * Cada seccion imprime el estado del heap para que se vea a simple
 * vista el splitting y el coalescing en accion.
 */

#include <stdio.h>
#include "allocator.h"

static void demo_split_and_coalesce(void) {
    printf("\n== split y coalescing ==\n");

    void *a = my_malloc(64);
    void *b = my_malloc(64);
    void *c = my_malloc(64);
    printf("Reservados a, b, c (64 bytes cada uno):\n");
    heap_dump();

    my_free(b);
    printf("\nLiberado b -> queda un hueco libre entre a y c:\n");
    heap_dump();

    void *d = my_malloc(16);
    printf("\nReservado d (16 bytes) -> reutiliza el hueco de b, se parte en dos:\n");
    heap_dump();

    my_free(d);
    my_free(c);
    printf("\nLiberados d y c (contiguos) -> deberian fusionarse en un solo bloque libre:\n");
    heap_dump();

    my_free(a);
}

static void demo_calloc(void) {
    printf("\n== calloc (memoria puesta a cero) ==\n");
    int *arr = (int *)my_calloc(5, sizeof(int));
    printf("valores iniciales: ");
    for (int i = 0; i < 5; i++) printf("%d ", arr[i]);
    printf("\n");
    my_free(arr);
}

static void demo_realloc(void) {
    printf("\n== realloc (arreglo dinamico que crece) ==\n");
    int cap = 4;
    int *arr = (int *)my_malloc((size_t)cap * sizeof(int));

    for (int i = 0; i < 20; i++) {
        if (i == cap) {
            cap *= 2;
            arr = (int *)my_realloc(arr, (size_t)cap * sizeof(int));
            printf("  -> capacidad ampliada a %d\n", cap);
        }
        arr[i] = i * i;
    }

    printf("contenido final: ");
    for (int i = 0; i < 20; i++) printf("%d ", arr[i]);
    printf("\n");
    my_free(arr);
}

static void demo_stress(void) {
    printf("\n== stress test (alloc/free en bucle) ==\n");
    void *ptrs[100] = {0};
    for (int round = 0; round < 500; round++) {
        int slot = round % 100;
        if (round >= 100) my_free(ptrs[slot]);
        size_t size = (size_t)((round * 37) % 200) + 1;
        ptrs[slot] = my_malloc(size);
    }
    for (int i = 0; i < 100; i++) my_free(ptrs[i]);
    printf("500 ciclos de alloc/free completados sin fallos.\n");
    heap_dump();
}

int main(void) {
    demo_split_and_coalesce();
    demo_calloc();
    demo_realloc();
    demo_stress();
    return 0;
}
