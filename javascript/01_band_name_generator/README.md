# Generador de Nombres de Bandas 🎸

Página web interactiva que genera nombres absurdos de bandas combinando adjetivos, sustantivos y géneros musicales. Sin dependencias — HTML + CSS + JS puro.

## Uso

Abre `index.html` en cualquier navegador. No necesita servidor.

## Funcionalidades

- Genera nombres con 6 patrones distintos ("Los X Ys", "El X Y Experience"...)
- 20 adjetivos × 20 sustantivos × 19 géneros × 6 patrones = miles de combinaciones
- Guarda favoritos en `localStorage` (persisten al cerrar el navegador)
- Elimina favoritos individualmente
- Copia toda la lista de favoritos al portapapeles
- Animaciones CSS: pop al generar, slide-in al guardar
- Diseño responsive, misma paleta visual que el portfolio

## Estructura

```
index.html   — estructura
style.css    — estilos (dark theme, glassmorphism, animaciones)
app.js       — lógica de generación y gestión de favoritos
```
