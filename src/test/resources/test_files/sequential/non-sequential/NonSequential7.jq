(:JIQS: ShouldCompile :)
declare %an:nonsequential function foo() { 1 };

 {
     variable $x := 3;
 }
 {
     variable $x := 4;
     for $j in [1 to 3]
     return {
         variable $k := 3;
     }
 }