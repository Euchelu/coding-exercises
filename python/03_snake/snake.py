"""
Snake en la terminal — Python 3.8+
Controles: WASD o flechas  |  Q para salir  |  P para pausar
"""
import sys
import os
import time
import json
import random
from pathlib import Path
from collections import deque

# ── Dependencia de teclado ────────────────────────────────────────────────────
try:
    if sys.platform == "win32":
        import msvcrt
        def get_key() -> str | None:
            if msvcrt.kbhit():
                ch = msvcrt.getwch()
                if ch in ('\x00', '\xe0'):   # tecla especial (flecha)
                    ch2 = msvcrt.getwch()
                    return {'H': 'UP', 'P': 'DOWN', 'K': 'LEFT', 'M': 'RIGHT'}.get(ch2)
                return ch.upper()
            return None
    else:
        import tty
        import termios
        import select
        def get_key() -> str | None:
            fd = sys.stdin.fileno()
            old = termios.tcgetattr(fd)
            try:
                tty.setraw(fd)
                r, _, _ = select.select([sys.stdin], [], [], 0)
                if r:
                    ch = sys.stdin.read(1)
                    if ch == '\x1b':
                        ch2 = sys.stdin.read(2)
                        return {'[A': 'UP', '[B': 'DOWN', '[D': 'LEFT', '[C': 'RIGHT'}.get(ch2)
                    return ch.upper()
            finally:
                termios.tcsetattr(fd, termios.TCSADRAIN, old)
            return None
except ImportError:
    def get_key() -> str | None:
        return None

# ── Configuración ─────────────────────────────────────────────────────────────
WIDTH  = 30
HEIGHT = 18
TICK   = 0.12   # segundos por frame

RECORDS_FILE = Path(__file__).parent / "records.json"

# ── Colores ANSI ──────────────────────────────────────────────────────────────
GREEN  = "\033[92m"
RED    = "\033[91m"
YELLOW = "\033[93m"
CYAN   = "\033[96m"
WHITE  = "\033[97m"
BOLD   = "\033[1m"
DIM    = "\033[2m"
RESET  = "\033[0m"

# ── Records ───────────────────────────────────────────────────────────────────
def load_records() -> list:
    if RECORDS_FILE.exists():
        try:
            return json.loads(RECORDS_FILE.read_text(encoding="utf-8"))
        except Exception:
            return []
    return []

def save_record(score: int) -> int:
    records = load_records()
    records.append(score)
    records.sort(reverse=True)
    records = records[:10]
    RECORDS_FILE.write_text(json.dumps(records), encoding="utf-8")
    return records.index(score) + 1 if score in records else len(records)

def best_score() -> int:
    r = load_records()
    return r[0] if r else 0

# ── Renderizado ───────────────────────────────────────────────────────────────
def clear() -> None:
    os.system("cls" if sys.platform == "win32" else "clear")

def hide_cursor() -> None:
    print("\033[?25l", end="", flush=True)

def show_cursor() -> None:
    print("\033[?25h", end="", flush=True)

def render(snake: deque, food: tuple, score: int, paused: bool) -> None:
    head = snake[0]
    body = set(list(snake)[1:])

    lines = []
    border = f"{DIM}+" + "─" * WIDTH + f"+{RESET}"
    lines.append(border)

    for y in range(HEIGHT):
        row = f"{DIM}│{RESET}"
        for x in range(WIDTH):
            cell = (x, y)
            if cell == head:
                row += f"{BOLD}{GREEN}@{RESET}"
            elif cell in body:
                row += f"{GREEN}█{RESET}"
            elif cell == food:
                row += f"{RED}●{RESET}"
            else:
                row += " "
        row += f"{DIM}│{RESET}"
        lines.append(row)

    lines.append(border)
    lines.append(
        f"  {BOLD}Puntuación: {YELLOW}{score}{RESET}   "
        f"{DIM}Récord: {best_score()}{RESET}"
        + (f"   {CYAN}{BOLD}[ PAUSA ]{RESET}" if paused else "")
    )
    lines.append(f"  {DIM}WASD/Flechas · P pausar · Q salir{RESET}")

    # Mover cursor al inicio en vez de limpiar (menos parpadeo)
    print("\033[H" + "\n".join(lines), flush=True)

# ── Lógica ────────────────────────────────────────────────────────────────────
DIRS = {
    'W': (0, -1), 'UP':    (0, -1),
    'S': (0,  1), 'DOWN':  (0,  1),
    'A': (-1, 0), 'LEFT':  (-1, 0),
    'D': (1,  0), 'RIGHT': (1,  0),
}
OPPOSITES = {(0,-1):(0,1),(0,1):(0,-1),(-1,0):(1,0),(1,0):(-1,0)}

def random_food(snake: deque) -> tuple:
    occupied = set(snake)
    while True:
        pos = (random.randint(0, WIDTH - 1), random.randint(0, HEIGHT - 1))
        if pos not in occupied:
            return pos

def game_loop() -> int:
    snake: deque = deque([(WIDTH // 2, HEIGHT // 2)])
    direction = (1, 0)
    food = random_food(snake)
    score = 0
    paused = False

    clear()
    hide_cursor()

    last_tick = time.time()

    try:
        while True:
            key = get_key()

            if key == 'Q':
                break
            if key == 'P':
                paused = not paused
            if key and key in DIRS:
                new_dir = DIRS[key]
                if new_dir != OPPOSITES.get(direction):
                    direction = new_dir

            now = time.time()
            if not paused and (now - last_tick) >= TICK:
                last_tick = now
                hx, hy = snake[0]
                dx, dy = direction
                new_head = (hx + dx, hy + dy)

                # Colisión con pared
                if not (0 <= new_head[0] < WIDTH and 0 <= new_head[1] < HEIGHT):
                    break
                # Colisión consigo mismo
                if new_head in snake:
                    break

                snake.appendleft(new_head)
                if new_head == food:
                    score += 10
                    food = random_food(snake)
                else:
                    snake.pop()

            render(snake, food, score, paused)
            time.sleep(0.02)

    finally:
        show_cursor()

    return score

# ── Menú ──────────────────────────────────────────────────────────────────────
def show_records() -> None:
    records = load_records()
    print(f"\n{BOLD}{CYAN}  ── Top 10 ──────────────────{RESET}")
    if not records:
        print(f"  {DIM}Sin récords aún.{RESET}")
    else:
        medals = ["🥇","🥈","🥉"] + ["  "] * 7
        for i, r in enumerate(records):
            print(f"  {medals[i]} #{i+1:>2}  {BOLD}{YELLOW}{r:>5} pts{RESET}")
    print()

def main() -> None:
    while True:
        clear()
        print(f"\n{BOLD}{GREEN}  🐍 SNAKE — Terminal Edition{RESET}")
        print(f"  {DIM}Tablero {WIDTH}×{HEIGHT} · +10 pts por fruta{RESET}\n")
        print(f"  {BOLD}[1]{RESET} Jugar")
        print(f"  {BOLD}[2]{RESET} Ver récords")
        print(f"  {DIM}[3]{RESET} Salir\n")
        choice = input(f"  {DIM}> {RESET}").strip()

        if choice == "3":
            print(f"\n  {DIM}¡Hasta pronto! 🐍{RESET}\n")
            break
        elif choice == "2":
            show_records()
            input(f"  {DIM}Pulsa Enter...{RESET}")
        elif choice == "1":
            score = game_loop()
            rank = save_record(score)
            clear()
            print(f"\n{BOLD}{RED}  ── GAME OVER ──────────────────{RESET}")
            print(f"  Puntuación: {BOLD}{YELLOW}{score} pts{RESET}")
            if rank <= 3:
                print(f"  {BOLD}{CYAN}🏆 ¡Top {rank} en el ranking!{RESET}")
            print(f"  Récord: {best_score()} pts\n")
            input(f"  {DIM}Pulsa Enter para volver al menú...{RESET}")


if __name__ == "__main__":
    main()
