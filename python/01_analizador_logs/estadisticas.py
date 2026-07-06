"""Analisis de entradas de log: comprehensions, generadores y Counter."""

from collections import Counter
from collections.abc import Iterable
from functools import lru_cache

from modelos import LogEntry, NivelLog


def contar_por_nivel(entradas: Iterable[LogEntry]) -> Counter:
    return Counter(entrada.nivel for entrada in entradas)


def filtrar_por_nivel(entradas: Iterable[LogEntry], *niveles: NivelLog) -> list[LogEntry]:
    return [entrada for entrada in entradas if entrada.nivel in niveles]


def mensajes_mas_comunes(entradas: Iterable[LogEntry], top: int = 3) -> list[tuple[str, int]]:
    contador = Counter(entrada.mensaje for entrada in entradas)
    return contador.most_common(top)


@lru_cache(maxsize=None)
def severidad(nivel: NivelLog) -> int:
    match nivel:
        case NivelLog.DEBUG:
            return 0
        case NivelLog.INFO:
            return 1
        case NivelLog.WARNING:
            return 2
        case NivelLog.ERROR:
            return 3
        case NivelLog.CRITICAL:
            return 4