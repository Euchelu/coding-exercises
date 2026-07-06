"""
Pomodoro CLI — temporizador con historial de sesiones.
Uso: python pomodoro.py
"""
import time
import json
import sys
import os
import threading
from datetime import datetime, date
from pathlib import Path

# ── Configuración ────────────────────────────────────────────────────────────
WORK_MIN  = 25
SHORT_MIN = 5
LONG_MIN  = 15
SESSIONS_BEFORE_LONG = 4

HISTORY_FILE = Path(__file__).parent / "sessions.json"

# ── Colores ANSI ─────────────────────────────────────────────────────────────
RED    = "\033[91m"
GREEN  = "\033[92m"
YELLOW = "\033[93m"
CYAN   = "\033[96m"
BOLD   = "\033[1m"
DIM    = "\033[2m"
RESET  = "\033[0m"

# ── Sonido (beep multiplataforma) ─────────────────────────────────────────────
def beep(times: int = 3) -> None:
    for _ in range(times):
        try:
            if sys.platform == "win32":
                import winsound
                winsound.Beep(880, 300)
            else:
                sys.stdout.write("\a")
                sys.stdout.flush()
        except Exception:
            sys.stdout.write("\a")
            sys.stdout.flush()
        time.sleep(0.15)

# ── Historial ─────────────────────────────────────────────────────────────────
def load_history() -> list:
    if HISTORY_FILE.exists():
        try:
            return json.loads(HISTORY_FILE.read_text(encoding="utf-8"))
        except (json.JSONDecodeError, OSError):
            return []
    return []

def save_session(task: str, duration_min: int, kind: str) -> None:
    history = load_history()
    history.append({
        "date": str(date.today()),
        "time": datetime.now().strftime("%H:%M"),
        "task": task,
        "duration_min": duration_min,
        "kind": kind,
    })
    HISTORY_FILE.write_text(json.dumps(history, ensure_ascii=False, indent=2), encoding="utf-8")

def show_history() -> None:
    history = load_history()
    if not history:
        print(f"\n{DIM}No hay sesiones registradas aún.{RESET}")
        return

    today = str(date.today())
    today_sessions = [s for s in history if s["date"] == today]
    work_today = sum(s["duration_min"] for s in today_sessions if s["kind"] == "work")

    print(f"\n{BOLD}{CYAN}── Historial de hoy ({today}) ──────────────────────{RESET}")
    if not today_sessions:
        print(f"  {DIM}Sin sesiones hoy.{RESET}")
    else:
        for s in today_sessions:
            icon = "🍅" if s["kind"] == "work" else "☕"
            print(f"  {icon} {s['time']}  {s['duration_min']:>2}min  {DIM}{s['task']}{RESET}")
        print(f"\n  {BOLD}Total trabajo hoy: {work_today} min ({work_today // 60}h {work_today % 60}min){RESET}")

    print(f"\n{BOLD}{CYAN}── Todas las sesiones ({len(history)} total) ─────────{RESET}")
    all_work = sum(s["duration_min"] for s in history if s["kind"] == "work")
    print(f"  Tiempo de trabajo acumulado: {BOLD}{all_work} min{RESET} ({all_work // 60}h {all_work % 60}min)\n")

# ── Temporizador ──────────────────────────────────────────────────────────────
def countdown(total_seconds: int, label: str, color: str) -> bool:
    """Muestra la barra de progreso. Devuelve True si completó, False si se interrumpió."""
    start = time.time()
    try:
        while True:
            elapsed = time.time() - start
            remaining = total_seconds - elapsed
            if remaining <= 0:
                break
            mins, secs = divmod(int(remaining), 60)
            filled = int(20 * elapsed / total_seconds)
            bar = "█" * filled + "░" * (20 - filled)
            pct = int(100 * elapsed / total_seconds)
            print(
                f"\r  {color}{BOLD}{label}{RESET}  "
                f"{color}[{bar}]{RESET} {pct:>3}%  "
                f"{BOLD}{mins:02d}:{secs:02d}{RESET}   ",
                end="", flush=True
            )
            time.sleep(0.5)
        print()
        return True
    except KeyboardInterrupt:
        print(f"\n\n  {YELLOW}⏸  Sesión interrumpida.{RESET}")
        return False

# ── UI ────────────────────────────────────────────────────────────────────────
def clear() -> None:
    os.system("cls" if sys.platform == "win32" else "clear")

def header() -> None:
    print(f"\n{BOLD}{RED}  🍅 POMODORO CLI{RESET}")
    print(f"  {DIM}trabajo · foco · productividad{RESET}\n")

def ask_task() -> str:
    print(f"  {CYAN}¿En qué vas a trabajar?{RESET}")
    task = input(f"  {DIM}> {RESET}").strip()
    return task or "Tarea sin nombre"

def menu() -> str:
    print(f"\n  {BOLD}¿Qué quieres hacer?{RESET}")
    print(f"  {GREEN}[1]{RESET} Iniciar Pomodoro ({WORK_MIN} min)")
    print(f"  {CYAN}[2]{RESET} Ver historial")
    print(f"  {DIM}[3]{RESET} Salir\n")
    return input(f"  {DIM}> {RESET}").strip()

# ── Bucle principal ───────────────────────────────────────────────────────────
def run() -> None:
    session_count = 0

    while True:
        clear()
        header()
        choice = menu()

        if choice == "3":
            print(f"\n  {DIM}¡Hasta pronto! 👋{RESET}\n")
            break

        elif choice == "2":
            show_history()
            input(f"\n  {DIM}Pulsa Enter para volver...{RESET}")

        elif choice == "1":
            task = ask_task()
            clear()
            header()
            print(f"  {DIM}Tarea: {BOLD}{task}{RESET}\n")

            completed = countdown(WORK_MIN * 60, "Trabajo", RED)

            if completed:
                session_count += 1
                save_session(task, WORK_MIN, "work")
                beep(3)

                if session_count % SESSIONS_BEFORE_LONG == 0:
                    kind = "long_break"
                    mins = LONG_MIN
                    label = "Descanso largo"
                    color = GREEN
                else:
                    kind = "short_break"
                    mins = SHORT_MIN
                    label = "Descanso corto"
                    color = CYAN

                print(f"\n  {BOLD}✅ ¡Pomodoro #{session_count} completado!{RESET}")
                print(f"  {color}☕ {label}: {mins} minutos{RESET}\n")
                input(f"  {DIM}Pulsa Enter para iniciar el descanso...{RESET}")

                clear()
                header()
                completed_break = countdown(mins * 60, label, color)
                if completed_break:
                    save_session(label, mins, kind)
                    beep(2)
                    print(f"\n  {BOLD}{GREEN}✓ Descanso terminado. ¡A por el siguiente!{RESET}")
                    time.sleep(2)
        else:
            pass  # opción no válida, vuelve al menú


if __name__ == "__main__":
    try:
        run()
    except KeyboardInterrupt:
        print(f"\n\n  {DIM}Saliendo...{RESET}\n")
