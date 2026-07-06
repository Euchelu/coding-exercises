"""Lectura perezosa de un archivo de logs: generador + context manager propio."""

import re
from collections.abc import Iterator
from datetime import datetime
from pathlib import Path

from decoradores import contar_llamadas
from modelos import LogEntry, NivelLog

PATRON_LINEA = re.compile(
    r"^(?P<fecha>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}) \[(?P<nivel>\w+)\] (?P<mensaje>.*)$"
)


@contar_llamadas
def parsear_linea(linea: str) -> LogEntry | None:
    """Devuelve None si la linea esta mal formada en vez de lanzar una
    excepcion: un log con lineas corruptas no deberia tumbar el analisis."""
    coincidencia = PATRON_LINEA.match(linea.strip())
    if coincidencia is None:
        return None
    try:
        nivel = NivelLog(coincidencia["nivel"])
    except ValueError:
        return None
    timestamp = datetime.strptime(coincidencia["fecha"], "%Y-%m-%d %H:%M:%S")
    return LogEntry(timestamp=timestamp, nivel=nivel, mensaje=coincidencia["mensaje"])


class AnalizadorLogs:
    """Abre el archivo al entrar al 'with' y lo cierra al salir (equivalente
    al try-with-resources de Java), exponiendo sus entradas ya parseadas
    mediante un generador para no cargar el archivo completo en memoria."""

    def __init__(self, ruta: Path):
        self.ruta = ruta
        self._archivo = None

    def __enter__(self) -> "AnalizadorLogs":
        self._archivo = self.ruta.open(encoding="utf-8")
        return self

    def __exit__(self, exc_type, exc_val, exc_tb) -> None:
        if self._archivo is not None:
            self._archivo.close()

    def entradas(self) -> Iterator[LogEntry]:
        if self._archivo is None:
            raise RuntimeError("usa 'with AnalizadorLogs(ruta) as analizador:' antes de leer entradas()")
        for linea in self._archivo:
            if not linea.strip():
                continue
            entrada = parsear_linea(linea)
            if entrada is not None:
                yield entrada