<?php
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON); 
$usuario = strval($input->usuario);
$password = strval($input->password);

$conn = mysqli_connect("localhost", "user", "12345", "proyectoriesgos");
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

$stmt = $conn->prepare("SELECT user, password FROM usermanager WHERE user=? AND password=?");
$stmt->bind_param("ss", $usuario, $password);
$stmt->execute();

$result = $stmt->get_result();
if($result->num_rows > 0) {
    $marks = array("success"=>"true");
    
}else{
    $marks = array("success"=>"false");
    
}
echo json_encode($marks);
$stmt->close();
$conn->close();
?>