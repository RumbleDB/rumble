(:JIQS: ShouldRun; Output="2" :)
declare namespace test = "http://www.example.com/1";
declare namespace test2 = "http://www.example.com/2";
declare function test:func($x) { $x };
test:func(2)

