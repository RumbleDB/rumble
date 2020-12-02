(:JIQS: ShouldRun; Output="(1, 1, 1, 1, 1, 1, 1, 1, good, good, good, 1, 2, 1, 2, good, good)" :)
declare function local:one-double($x as double) as double
{
  $x
};
declare function local:zero-or-one-double($x as double?) as double?
{
  $x
};
declare function local:doubles($x as double*) as double*
{
  $x
};
declare function local:at-least-one-double($x as double+) as double+
{
  $x
};
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := local:one-double($a.foo)
return $b,
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := local:zero-or-one-double($a.foo)
return $b,
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := local:doubles($a.foo)
return $b,
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := local:at-least-one-double($a.foo)
return $b,

let $a := annotate({"foo" : 1},{"foo":"integer"})
let $b := local:one-double($a.foo)
return $b,
let $a := annotate({"foo" : 1},{"foo":"integer"})
let $b := local:zero-or-one-double($a.foo)
return $b,
let $a := annotate({"foo" : 1},{"foo":"integer"})
let $b := local:doubles($a.foo)
return $b,
let $a := annotate({"foo" : 1},{"foo":"integer"})
let $b := local:at-least-one-double($a.foo)
return $b,

try {
  let $a := annotate({"foo" : "bar"},{"foo":"string"})
  let $b := local:at-least-one-double($a.foo)
  return $b
} catch XPTY0004
{
  "good"
},

try {
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := local:one-double($a.foo)
return $b
} catch XPTY0004
{
  "good"
},
try {
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := local:zero-or-one-double($a.foo)
return $b
} catch XPTY0004
{
  "good"
},
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := local:doubles($a.foo)
return $b,
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := local:at-least-one-double($a.foo)
return $b,



try {
let $a := annotate((),{"foo":"double"})
let $b := local:one-double($a.foo)
return $b
} catch XPTY0004
{
  "good"
},
let $a := annotate((),{"foo":"double"})
let $b := local:zero-or-one-double($a.foo)
return $b,
let $a := annotate((),{"foo":"double"})
let $b := local:doubles($a.foo)
return $b,
try {
let $a := annotate((),{"foo":"double"})
let $b := local:at-least-one-double($a.foo)
return $b
} catch XPTY0004
{
  "good"
}
