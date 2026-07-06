"""Modelos de datos para el analizador de logs."""

from dataclasses import dataclass
from datetime import datetime
from enum import Enum


class NivelLog(Enum):
    DEBUG = "DEBUG"
    INFO = "INFO"
    WARNING = "WARNING"
    ERROR = "ERROR"
    CRITICAL = "CRITICAL"


@dataclass(frozen=True, slots=True)
class LogEntry:
    timestamp: datetime
    nivel: NivelLog
    mensaje: str