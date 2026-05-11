(:JIQS: ShouldRun; Output="empty" :)
variable $res := "";
while ($res eq "") {
    while (true) {
        typeswitch(())
            case string return $res := "string";
            case () return {$res := "empty"; break loop;}
            default return $res := "other";
        $res := "non-empty";
    }
}
$res