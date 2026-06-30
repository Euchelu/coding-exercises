$.fn.contadorCaracteres = function(limite, idContador) {
  this.on("input", function() {
    const restantes = limite - $(this).val().length;
    $(`#${idContador}`).text(restantes);
  });
  return this;
};

$("#descripcion").contadorCaracteres(200, "contador");