(:JIQS: ShouldRun; Output="1" :)
declare namespace test = "http://www.example.com/1";
declare namespace test2 = "http://www.example.com/1";
let $test:var := 1
return $test2:var
