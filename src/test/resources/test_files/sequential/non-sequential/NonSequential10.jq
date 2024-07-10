(:JIQS: ShouldCompile :)
try {
    variable $x := 3;
} catch * {
    variable $x := 2;
}