(:JIQS: ShouldRun; Output=" ({ "foo" : 2 }, { "foo" : 3 }, { }, Success) Actual result: ({ "bar" : [ { "foo" : 1 }, { "foo" : 2 }, { "foo" : 3 } ], "foobar" : 1 }, { "bar" : [ { "foo" : 1 }, { "foo" : 2 }, { "foo" : 3 } ], "foobar" : 2 }, { "bar" : [ { "foo" : 1 }, { "foo" : 2 }, { "foo" : 3 } ], "foobar" : 3 })" :)
declare type local:at as { "foo" : "integer" };
declare type local:bt as { "foobar" : "integer", "bar" : [ "local:at" ] };
    
let $y := validate type local:bt* { (1 to 3)!{"foobar" : $$, "bar":[ (1 to 3)! {"foo":$$}] } }

return $y