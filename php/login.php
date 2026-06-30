<?php
session_start();
require 'utilidad.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = trim($_POST['email']);
    $password = trim($_POST['password']);

    // Validación del email
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        echo "Error: El email no tiene un formato válido.";
    } else {
        $usuario = obtenerUsuarioPorEmail($email);
        if ($usuario && password_verify($password, $usuario['contrasenia_hash'])) {
            // Credenciales correctas
            $_SESSION['email'] = $email;
            header('Location: index.php');
            exit();
        } else {
            echo "Error: Email o contraseña incorrectos.";
        }
    }
}
?>
<form method="POST" action="">
    <label>Email:</label>
    <input type="email" name="email" required>
    <label>Contraseña:</label>
    <input type="password" name="password" required>
    <button type="submit">Acceder</button>
</form>
