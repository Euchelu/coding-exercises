"""Demo: generador + context manager propio para leer el log de forma
perezosa, decoradores para instrumentar funciones, y comprehensions/Counter
para las estadisticas."""

from pathlib import Path

from analizador import AnalizadorLogs, parsear_linea
from decoradores import medir_tiempo
from estadisticas import contar_por_nivel, filtrar_por_nivel, mensajes_mas_comunes, severidad
from modelos import LogEntry, NivelLog

RUTA_LOG = Path(__file__).parent / "logs_ejemplo.log"


@medir_tiempo
def cargar_entradas(ruta: Path) -> list[LogEntry]:
    with AnalizadorLogs(ruta) as analizador:
        return list(analizador.entradas())


def main() -> None:
    entradas = cargar_entradas(RUTA_LOG)
    omitidas = parsear_linea.llamadas - len(entradas)
    print(f"Lineas procesadas: {parsear_linea.llamadas} | validas: {len(entradas)} | omitidas: {omitidas}")

    print("\n--- Conteo por nivel ---")
    conteo = contar_por_nivel(entradas)
    for nivel in NivelLog:
        print(f"{nivel.value:>8}: {conteo.get(nivel, 0)}")

    graves = filtrar_por_nivel(entradas, NivelLog.ERROR, NivelLog.CRITICAL)
    graves_ordenados = sorted(graves, key=lambda e: severidad(e.nivel), reverse=True)
    print(f"\n--- Errores/Criticos ({len(graves_ordenados)}) ---")
    for entrada in graves_ordenados:
        print(f"  [{entrada.nivel.value}] {entrada.timestamp} - {entrada.mensaje}")

    print("\n--- Mensajes mas frecuentes ---")
    for mensaje, veces in mensajes_mas_comunes(entradas):
        print(f"  x{veces}  {mensaje}")


if __name__ == "__main__":
    main()