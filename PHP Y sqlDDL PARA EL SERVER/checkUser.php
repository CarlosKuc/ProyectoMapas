<?php
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON); 
$usuario = strval($input->usuario);
$conn = mysqli_connect("localhost", "user", "12345", "proyectoriesgos");
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
$stmt = $conn->prepare("SELECT user FROM usermanager WHERE user=?");
$stmt->bind_param("s", $usuario);
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