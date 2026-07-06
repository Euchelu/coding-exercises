# Snake — Terminal Edition 🐍

El clásico Snake corriendo en la terminal. Solo Python 3.8+, sin dependencias externas.

## Uso

```bash
python snake.py
```

## Controles

| Tecla | Acción |
|---|---|
| `W` / ↑ | Arriba |
| `S` / ↓ | Abajo |
| `A` / ← | Izquierda |
| `D` / → | Derecha |
| `P` | Pausar / reanudar |
| `Q` | Salir al menú |

## Funcionalidades

- Tablero 30×18 renderizado con caracteres Unicode
- +10 puntos por cada fruta comida
- Top 10 de récords guardado en `records.json`
- Pausa con `P`
- Compatible con Windows (msvcrt) y Unix/macOS (termios)
- Sin parpadeo: usa posicionamiento de cursor en vez de clear

## Archivos

- `snake.py` — juego completo en un solo archivo
- `records.json` — tabla de récords (generado automáticamente, en .gitignore)
