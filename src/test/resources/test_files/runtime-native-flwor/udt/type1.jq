(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { }, Success, Success, Success, Success, Success, { "foo" : [ 2 ] }, { "foo" : [ 3, 4 ], "bar" : 4 }, { })" :)
declare type local:x as { "foo" : "integer" };
declare type local:y as { "foo" : [ "integer" ] };
declare type local:z as { "!foo" : [ "integer" ] };
validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3, "bar" : 4 },
  { }
},
try {
  validate type local:x* {
    { "foo" : "foo" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : [ 1 ] }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : { "bar" : 2 } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    2, 3
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    [ "foo" ]
  }
} catch XQDY0027 {
  "Success"
},
validate type local:y* {
  { "foo" : [ 2 ] },
  { "foo" : [ 3, 4 ], "bar" : 4 },
  { }
}
