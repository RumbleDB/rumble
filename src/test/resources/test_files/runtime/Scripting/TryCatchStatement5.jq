(:JIQS: ShouldRun; Output="2" :)
variable $x := 5;
while($x > 3) {
    if (true) then {();} else {();}
    while (false) {}
    try {
        $x := 2;
        continue loop;
        $x := 4;
    } catch * {}
}
$x