<?php
require 'seguridad.php';
require 'utilidad.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $idNota = $_POST['id'];
    borradoLogico($idNota);
    header('Location: index.php');
    exit();
} elseif (isset($_GET['id'])) {
    $idNota = $_GET['id'];
}
?>
<form method="POST" action="">
    <input type="hidden" name="id" value="<?= htmlspecialchars($idNota) ?>">
    <p>¿Estás seguro de que deseas borrar esta nota?</p>
    <button type="submit">Confirmar borrado</button>
</form>
