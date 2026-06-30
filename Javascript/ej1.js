const DEPTOS = ["Ventas", "Soporte", "Desarrollo"];
const SEDES = ["Madrid", "Barcelona", "Sevilla"];
const NUMUBICACIONES = 5;

function generarUnidades(deptos, sedes, numUbicaciones) {
  const combinaciones = [];
  while (combinaciones.length < numUbicaciones) {
    const depto = deptos[Math.floor(Math.random() * deptos.length)];
    const sede = sedes[Math.floor(Math.random() * sedes.length)];
    const ubicacion = `${depto} - ${sede}`;
    if (!combinaciones.includes(ubicacion)) {
      combinaciones.push(ubicacion);
    }
  }
  return combinaciones.map((ubicacion, index) => `${index + 1}. ${ubicacion}`);
}

function inicializacion() {
  const ubicaciones = generarUnidades(DEPTOS, SEDES, NUMUBICACIONES);
  const listContainer = document.createElement("ul");
  ubicaciones.forEach((ubicacion) => {
    const listItem = document.createElement("li");
    const textNode = document.createTextNode(ubicacion);
    listItem.appendChild(textNode);
    listContainer.appendChild(listItem);
  });
  document.body.appendChild(listContainer);
}

inicializacion();