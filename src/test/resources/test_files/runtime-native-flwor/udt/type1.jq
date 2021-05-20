(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { })" :)
declare type local:x as { "foo" : "integer" };
declare type local:y as { "foo" : [ "integer" ] };
declare type local:z as { "!foo" : [ "integer" ] };
validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3, "bar" : 4 },
  { }
}
