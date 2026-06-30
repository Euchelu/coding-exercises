<?php
require 'seguridad.php';
require 'utilidad.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $texto = trim($_POST['texto']);
    $fecha = trim($_POST['fecha']);

    if (empty($texto) || empty($fecha)) {
        echo "Error: Todos los campos son obligatorios.";
    } elseif (!checkdate(substr($fecha, 5, 2), substr($fecha, 8, 2), substr($fecha, 0, 4))) {
        echo "Error: Fecha no válida.";
    } else {
        $idNota = uniqid();
        $emailUsuario = $_SESSION['email'];
        darAltaNota($idNota, $texto, $fecha, $emailUsuario);
        header('Location: index.php');
        exit();
    }
}
?>
<form method="POST" action="">
    <label>Texto:</label>
    <input type="text" name="texto" value="<?= $texto ?? '' ?>" required>
    <label>Fecha:</label>
    <input type="date" name="fecha" value="<?= $fecha ?? '' ?>" required>
    <button type="submit">Crear Nota</button>
</form>
