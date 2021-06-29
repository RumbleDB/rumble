(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { }, true, true, true, true, { "foo" : 2 }, Success, Success, Success, Success, Success, { "foo" : [ 2 ] }, { "foo" : [ 3, 4 ] }, { }, Success, Success, Success, { "foo" : [ 2 ] }, { "foo" : [ 3, 4 ] }, Success, Success, Success, Success)" :)
declare type local:x as { "foo" : "integer" };
declare type local:y as { "foo" : [ "integer" ] };
declare type local:z as { "!foo" : [ "integer" ] };
validate type local:x {
  { "foo" : 2 }
},
validate type local:x? {
  { }
},
validate type local:x? {
  { "foo" : 2 }
},
try {
  validate type local:z? {
    { "foo" : [ "foo" ] },
    { "foo" : [ "foo" ] }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:z {
    { "foo" : [ "foo" ] },
    { "foo" : [ "foo" ] }
  }
} catch XQDY0027 {
  "Success"
},
validate type local:x {
  { "foo" : 2 }
} instance of local:x,
validate type local:x {
  { "foo" : 2 }
} instance of local:x?,
validate type local:x {
  { "foo" : 2 }
} instance of local:x*,
validate type local:x {
  { "foo" : 2 }
} instance of local:x+,
validate type local:x? {
  { "foo" : 2 }
} instance of local:x,
validate type local:x? {
  { "foo" : 2 }
} instance of local:x?,
validate type local:x? {
  { "foo" : 2 }
} instance of local:x*,
validate type local:x? {
  { "foo" : 2 }
} instance of local:x+,

(validate type local:x {
  { "foo" : 2 }
}).foo instance of integer,

(validate type local:y { parallelize((
  { "foo" : [ 3, 4 ] }
))}).foo[] instance of integer+,

(validate type local:y+ {
  { "foo" : [ 2 ] }
}).foo instance of array*,
try {
  validate type local:x {
    { "foo" : 2, "bar" : 3 }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    { "foo" : "foo" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    { "foo" : [ 1 ] }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    { "foo" : { "bar" : 2 } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    2, 3
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    [ "foo" ]
  }
} catch XQDY0027 {
  "Success"
},
validate type local:y {
  { "foo" : [ 2 ] }
},
validate type local:y {
  { "foo" : [ 3, 4 ] }
},
validate type local:y {
  { }
},
try {
  validate type local:y {
    { "foo" : 2 }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y {
    { "foo" : { "bar" : "foo" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y {
    { "foo" : [ "foo" ] }
  }
} catch XQDY0027 {
  "Success"
},
validate type local:z {
  { "foo" : [ 2 ] }
},
try {
  validate type local:z {
    { "foo" : 2 }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:z {
    { "foo" : { "bar" : "foo" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:z {
    { "foo" : [ "foo" ] }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:z {
    { }
  }
} catch XQDY0027 {
  "Success"
}
