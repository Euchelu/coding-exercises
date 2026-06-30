function eliminarError(nombreDelCampo) {
  const errorSpan = document.getElementById(`${nombreDelCampo}Error`);
  if (errorSpan) {
    while (errorSpan.firstChild) {
      errorSpan.removeChild(errorSpan.firstChild);
    }
  }
}

function mostrarError(nombreDelCampo, mensajeDeError) {
  eliminarError(nombreDelCampo);
  const errorSpan = document.getElementById(`${nombreDelCampo}Error`);
  if (errorSpan) {
    const textNode = document.createTextNode(mensajeDeError);
    errorSpan.appendChild(textNode);
  }
}