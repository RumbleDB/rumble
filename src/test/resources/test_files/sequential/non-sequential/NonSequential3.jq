(:JIQS: ShouldCompile :)
typeswitch(duration("P4Y5MT5M"))
case string | decimal? | hexBinary* return variable $x := "this";
case base64Binary+ return variable $y := "not even this";
case int return variable $y := "none";
default return variable $x := "correct";