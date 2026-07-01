CC = gcc
CFLAGS = -Wall -Wextra -std=c11 -g

demo: main.c allocator.c allocator.h
	$(CC) $(CFLAGS) -o demo main.c allocator.c

clean:
	rm -f demo
