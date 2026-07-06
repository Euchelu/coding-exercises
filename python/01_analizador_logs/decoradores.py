"""Decoradores de proposito general para instrumentar funciones."""

import functools
import time
from collections.abc import Callable
from typing import TypeVar

T = TypeVar("T")


def medir_tiempo(func: Callable[..., T]) -> Callable[..., T]:
    """Imprime cuanto tarda la funcion decorada en ejecutarse."""

    @functools.wraps(func)
    def envoltorio(*args, **kwargs) -> T:
        inicio = time.perf_counter()
        resultado = func(*args, **kwargs)
        duracion_ms = (time.perf_counter() - inicio) * 1000
        print(f"[{func.__name__}] tardo {duracion_ms:.2f} ms")
        return resultado

    return envoltorio


def contar_llamadas(func: Callable[..., T]) -> Callable[..., T]:
    """Le agrega a la funcion un atributo '.llamadas' con el total de
    invocaciones, aprovechando que en Python las funciones son objetos."""

    @functools.wraps(func)
    def envoltorio(*args, **kwargs) -> T:
        envoltorio.llamadas += 1
        return func(*args, **kwargs)

    envoltorio.llamadas = 0
    return envoltorio