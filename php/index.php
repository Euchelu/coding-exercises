<?php
require 'seguridad.php';
require 'utilidad.php';

$emailUsuario = $_SESSION['email'];
$notas = obtenerNotasPorUsuario($emailUsuario, 0); // Estado 0: notas activas
?>
<table>
    <tr>
        <th>ID</th>
        <th>Texto</th>
        <th>Fecha</th>
        <th>Acción</th>
    </tr>
    <?php foreach ($notas as $nota): ?>
        <tr>
            <td><?= $nota['id'] ?></td>
            <td><?= htmlspecialchars($nota['texto']) ?></td>
            <td><?= $nota['fecha'] ?></td>
            <td><a href="borrar.php?id=<?= $nota['id'] ?>">Borrar</a></td>
        </tr>
    <?php endforeach; ?>
</table>
