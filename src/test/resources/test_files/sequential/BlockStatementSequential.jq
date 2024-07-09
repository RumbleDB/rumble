(:JIQS: ShouldCompile :)
declare %an:sequential function foo() { 1 };

 {
     variable $x := 3;
     foo();
 }
 {
     variable $x := 4;
     for $j in [1 to 3]
     return {
         break loop;
         variable $res;
     }
 }