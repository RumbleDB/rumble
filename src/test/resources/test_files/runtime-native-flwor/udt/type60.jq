(:JIQS: ShouldRun; Output="({ "foo" : 1 }, { "foo" : 300 }, { }, true, true, true, true, true, false, Success, Success, Success, { "foo" : [ 2 ] }, { "foo" : [ 3, 4 ] }, { }, Success, Success, 1000000, { "foo" : [ -2 ] }, { "foo" : [ -3, -4 ] }, Success, Success)" :)
declare type local:x as { "foo" : "positiveInteger" };
declare type local:y as { "foo" : [ "byte" ] };
declare type local:z as { "foo" : [ "nonPositiveInteger" ] };
declare type local:w as { "foo" : "unsignedByte", "bar" : "long" };
validate type local:x* {
  { "foo" : 1 },
  { "foo" : 300 },
  { }
},
validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3 },
  { }
} instance of local:x+,
(validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3 },
  { }
}).foo instance of positiveInteger+,
(validate type local:y+ { parallelize((
  { "foo" : [ -128 ] },
  { "foo" : [ 11, 127 ] },
  { "foo" : [ ] },
  { }
))}).foo[] instance of byte+,
(validate type local:y+ {
  { "foo" : [ -128 ] },
  { "foo" : [ 3, 127 ] },
  { "foo" : [ ] },
  { }
}).foo instance of array*,
(validate type local:z+ {
  { "foo" : [ -1000008 ] },
  { "foo" : [ -10, -1 ] },
  { "foo" : [ ] },
  { }
}).foo[] instance of nonPositiveInteger+,
(validate type local:w* {
    { "foo" : 2, "bar" : 3 }
  }
).bar instance of unsignedLong+,
try {
  validate type local:x* {
    { "foo" : "string" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:z* {
    { "foo" : 1  }
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
  { "foo" : [ 3, 4 ] },
  { }
},
try {
  validate type local:y* {
    { "foo" : 2 }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y* {
    { "foo" : { "bar" : "foo" } }
  }
} catch XQDY0027 {
  "Success"
},
(validate type local:w* {
  { "foo" : 2, "bar": -123456789 },
  { "foo" : 129, "bar": 1000000 }
}).bar[2],
validate type local:z* {
  { "foo" : [ -2 ] },
  { "foo" : [ -3, -4 ] }
},
try {
  validate type local:z* {
    { "foo" : { "bar" : "foo" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:w* {
    { "foo" : [ "foo" ], "bar": 12345 }
  }
} catch XQDY0027 {
  "Success"
}
