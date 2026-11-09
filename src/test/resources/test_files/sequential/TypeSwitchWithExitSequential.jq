(:JIQS: ShouldCompile :)
typeswitch(duration("P4Y5MT5M"))
case string | decimal? | hexBinary* return "not this";
case base64Binary+ return "not even this";
case int return exit returning "none";
default return "correct";