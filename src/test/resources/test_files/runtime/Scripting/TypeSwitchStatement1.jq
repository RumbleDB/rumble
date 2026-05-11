(:JIQS: ShouldRun; Output="string" :)
variable $res;
typeswitch("Hello world!")
case hexBinary? return $res := hexBinary("0123");
case boolean+ return $res := true;
case decimal return $res := 1;
case string* return $res := "string";
default return $res := "default";
$res
