# Coding Exercises

Colección de ejercicios para practicar fundamentos de programación en **C**, **Java**, **Python** y **JavaScript**: gestión de memoria y procesos, programación orientada a objetos, patrones de diseño, concurrencia y redes.

| Lenguaje | Ejercicio | Qué practica |
|---|---|---|
| C | [Mini shell](C/02_mini_shell) | Procesos con `fork`/`exec`, tuberías y redirección de entrada/salida (`<`, `>`, `>>`) |
| C | [Gestor de memoria](C/03_memory_allocator) | Implementación propia de `malloc`, `free`, `calloc` y `realloc` sobre `sbrk()` con estrategia *first-fit* |
| Java | [Biblioteca POO](Java/01_biblioteca_poo) | Herencia, excepciones propias y patrones Factory Method, Strategy y Observer |
| Java | [Chat con sockets](Java/02_chat_sockets) | Servidor TCP multicliente con un hilo por cliente (`ExecutorService`) |
| Python | [Analizador de logs](python/01_analizador_logs) | Generadores, *context managers*, decoradores, *comprehensions* y `Counter` |
| Python | [Pomodoro CLI](python/02_pomodoro_cli) | Temporizador Pomodoro en la terminal, sin dependencias |
| Python | [Snake](python/03_snake) | El clásico Snake en la terminal |
| JavaScript | [Generador de nombres de bandas](javascript/01_band_name_generator) | Manipulación del DOM con HTML, CSS y JavaScript puro |

## Cómo ejecutarlos

```bash
# C (Linux/macOS, requiere gcc y make)
cd C/02_mini_shell && make && ./mini_shell
cd C/03_memory_allocator && make && ./demo

# Java (JDK 17+)
cd Java/01_biblioteca_poo/src && javac *.java && java Main
cd Java/02_chat_sockets/src && javac *.java && java ChatServer   # y en otra terminal: java ChatClient

# Python (3.8+)
python python/01_analizador_logs/main.py
python python/02_pomodoro_cli/pomodoro.py
python python/03_snake/snake.py

# JavaScript: abre javascript/01_band_name_generator/index.html en el navegador
```

## Autor

**José Ángel Ceballos Rodríguez** · [Portfolio](https://euchelu.github.io/portfolio/) · [GitHub](https://github.com/Euchelu)
