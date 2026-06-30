function validarLista(campo) {
  const valor = document.getElementById(campo).value;
  if (valor === "") {
    mostrarError(campo, "Debe seleccionar una opción válida.");
    return false;
  }
  eliminarError(campo);
  return true;
}

function validarTipoDeIncidencia() {
  return validarLista("tipoDeIncidencia");
}

function limitarCaracteres(campo, limiteCaracteres) {
  const input = document.getElementById(campo);
  const texto = input.value;
  if (texto.length > limiteCaracteres) {
    mostrarError(campo, `El texto excede el límite de ${limiteCaracteres} caracteres. Has escrito ${texto.length}.`);
    return false;
  }
  eliminarError(campo);
  return true;
}

function comprobarFormatoFecha(fecha) {
  const regex = /^\d{2}\/\d{2}\/\d{4}$/;
  if (!regex.test(fecha)) {
    mostrarError("fecha", "El formato debe ser dd/mm/yyyy.");
    return false;
  }
  eliminarError("fecha");
  return true;
}