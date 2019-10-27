(:JIQS: ShouldRun; Output="(100, 1, 2, 3, 15)" :)
declare function price () as integer+ { 100 };
declare function price ($z as integer) as integer+ { $z };
declare function price ($x as integer, $y as integer) as integer+ { $x, $y };
declare function price ($x as integer, $y as integer, $z as integer) as integer+ { $x+$y+$z };
price(), price(1), price(2,3), price(4,5,6)

(: Function overloading  :)
