(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { })" :)
declare type local:x as { "foo" : "integer" };
validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3 },
  { }
} treat as local:x+

