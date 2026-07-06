# Pomodoro CLI 🍅

Temporizador Pomodoro en la terminal. Sin dependencias externas — solo Python 3.8+.

## Uso

```bash
python pomodoro.py
```

## Funcionalidades

- Temporizador de 25 min con barra de progreso animada
- Descansos cortos (5 min) y largos (15 min cada 4 pomodoros)
- Notificación de sonido al terminar cada sesión (`winsound` en Windows, beep ANSI en Unix)
- Historial persistente en `sessions.json` con fecha, hora y tarea
- Vista de resumen: sesiones de hoy + tiempo total acumulado

## Estructura de `sessions.json`

```json
[
  {
    "date": "2025-01-15",
    "time": "10:30",
    "task": "Implementar autenticación JWT",
    "duration_min": 25,
    "kind": "work"
  }
]
```
