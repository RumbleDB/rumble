(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:8:COLUMN:0:" :)
variable $a as xs:integer := 0;
variable $b as xs:integer := 1;
variable $c as xs:integer := $a + $b;
variable $fibseq as xs:integer* := ($a, $b);
variable $je := {"a" : 1, "b" : 2};

while (insert json "c" : 3 into $je) {
  $fibseq := ($fibseq, $c);
  $a := $b;
  $b := $c;
  $c := $a + $b;
}

(: while statement cannot have updating test condition :)