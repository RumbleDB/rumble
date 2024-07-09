(:JIQS: ShouldCompile :)
variable $a as xs:integer := 0;
variable $b as xs:integer := 1;
variable $c as xs:integer := $a + $b;
variable $fibseq as xs:integer* := ($a, $b);

while ($c < 100) {
  $fibseq := ($fibseq, $c);
  $a := $b;
  $b := $c;
  $c := $a + $b;
  while ($c < 100) {
    $fibseq := ($fibseq, $c);
    $a := $b;
    $b := $c;
    $c := $a + $b;
    continue loop;
  }
}