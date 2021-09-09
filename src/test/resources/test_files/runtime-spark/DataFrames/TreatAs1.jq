(:JIQS: ShouldRun; Output="(1, 1, 1, 1, good, good, good, good, good, good, good, 1, 2, 1, 2, good, good)" :)
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := $a.foo treat as double
return $b,
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := $a.foo treat as double?
return $b,
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := $a.foo treat as double*
return $b,
let $a := annotate({"foo" : 1e0},{"foo":"double"})
let $b := $a.foo treat as double+
return $b,

try {
let $a := annotate({"foo" : 1e0},{"foo":"integer"})
let $b := $a.foo treat as double
return $b
} catch XPDY0050
{
  "good"
},
try {
let $a := annotate({"foo" : 1e0},{"foo":"integer"})
let $b := $a.foo treat as double?
return $b
} catch XPDY0050
{
  "good"
},
try {
let $a := annotate({"foo" : 1e0},{"foo":"integer"})
let $b := $a.foo treat as double*
  return $b
} catch XPDY0050
{
  "good"
},
try {
let $a := annotate({"foo" : 1e0},{"foo":"integer"})
let $b := $a.foo treat as double+
  return $b
} catch XPDY0050
{
  "good"
},

try {
  let $a := annotate({"foo" : "bar"},{"foo":"string"})
  let $b := $a.foo treat as double+
  return $b
} catch XPDY0050
{
  "good"
},

try {
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := $a.foo treat as double
return $b
} catch XPDY0050
{
  "good"
},
try {
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := $a.foo treat as double?
return $b
} catch XPDY0050
{
  "good"
},
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := $a.foo treat as double*
return $b,
let $a := annotate(({"foo" : 1e0}, {"foo" : 2e0}),{"foo":"double"})
let $b := $a.foo treat as double+
return $b,



try {
let $a := annotate((),{"foo":"double"})
let $b := $a.foo treat as double
return $b
} catch XPDY0050
{
  "good"
},
let $a := annotate((),{"foo":"double"})
let $b := $a.foo treat as double?
return $b,
let $a := annotate((),{"foo":"double"})
let $b := $a.foo treat as double*
return $b,
try {
let $a := annotate((),{"foo":"double"})
let $b := $a.foo treat as double+
return $b
} catch XPDY0050
{
  "good"
}
