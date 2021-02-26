<?php
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON); 
$usuario = strval($input->usuario);
$password = strval($input->password);


$conn = mysqli_connect("localhost", "user", "12345", "proyectoriesgos");

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

$stmt1 = $conn->prepare("INSERT INTO usermanager (user, password) VALUES (?, ?)");
$stmt1->bind_param("ss", $usuario, $password);
$stmt1->execute();
$stmt1->close();

$stmt2 = $conn->prepare("SELECT user FROM usermanager WHERE user=?");
$stmt2->bind_param("s", $usuario);
$stmt2->execute();

$result = $stmt2->get_result();
if($result->num_rows > 0) {
    $marks = array("success"=>"true");
    
}else{
    $marks = array("success"=>"false");
}
echo json_encode($marks);
$stmt2->close();
$conn->close();
?>
