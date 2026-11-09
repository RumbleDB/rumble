(:JIQS: ShouldRun; Output="empty" :)
variable $res;
typeswitch(())
case string return $res := "string";
case () return $res := "empty";
default return $res := "other";
$res