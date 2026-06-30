session_start();
$conn = new mysqli('localhost', 'root', '', 'mi_base');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $texto = htmlspecialchars($_POST['texto']);
    $fecha = $_POST['fecha'];
    $email = $_SESSION['email'];

    $stmt = $conn->prepare("INSERT INTO notas (texto, fecha, email) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $texto, $fecha, $email);
    $stmt->execute();
    header('Location: index.php');
    exit();
}

$stmt = $conn->prepare("SELECT * FROM notas WHERE email = ? AND estado = 0");
$stmt->bind_param("s", $_SESSION['email']);
$stmt->execute();
$notas = $stmt->get_result();
?>
<table>
    <?php foreach ($notas as $nota): ?>
        <tr>
            <td><?= htmlspecialchars($nota['texto']) ?></td>
            <td><?= $nota['fecha'] ?></td>
        </tr>
    <?php endforeach; ?>
</table>
