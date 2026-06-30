<?php
define('NOMBRE_FICHERO_LOG', 'registro_borrado.log');

function escribirLog($usuario, $idNota) {
    $registro = "$usuario;$idNota" . PHP_EOL;
    $file = fopen(NOMBRE_FICHERO_LOG, 'a');
    if (flock($file, LOCK_EX)) {
        fwrite($file, $registro);
        flock($file, LOCK_UN);
    }
    fclose($file);
}
?>
