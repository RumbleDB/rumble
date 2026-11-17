(:JIQS: ShouldParse :)
while ($c < 100) {
  $fibseq := ($fibseq, $c);
  $a := $b;
  $b := $c;
  $c := $a + $b;
}