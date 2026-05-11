(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { }, Success)" :)
declare type local:x as { "foo" : "integer" };
declare function local:f($x as local:x*) as local:x+
{
  $x
};
let $x as local:x* :=
  validate type local:x* {
    { "foo" : 2 },
    { "foo" : 3 },
    { }
  }
return local:f($x),
try {
  let $x as local:x* :=
    (
      { "foo" : 2 },
      { "foo" : 3 },
      { }
    )
  return $x
} catch XPTY0004 {
  "Success"
}
