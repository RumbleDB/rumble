(:JIQS: ShouldCompile:)
declare function foo() {
          copy $je := [1 to 4]
          modify delete json $je[[2]]
          return $je};
declare variable $var := foo();
