(:JIQS: ShouldRun; Output="(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89)" :)
variable $a as xs:integer := 0;
variable $b as xs:integer := 1;
variable $c as xs:integer := $a + $b;
variable $fibseq as xs:integer* := ($a, $b);

while ($c < 100) {
  $fibseq := ($fibseq, $c);
  $a := $b;
  $b := $c;
  $c := $a + $b;
}
$fibseq