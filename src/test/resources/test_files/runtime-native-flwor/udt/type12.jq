(:JIQS: ShouldRun; Output="([ ], [ "foo" ], [ "foo", "bar" ], [ "foo", "bar", "1", "2", "3" ], [ ], [ "foo" ], [ "foo", "bar" ], [ "foo", "bar", "1", "2", "3" ], Success, Success, Success, [ ], [ "2021-01-01" ], [ "2021-01-01", "2021-07-21" ], [ "2021-01-01", "2021-07-21" ], Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "string"
};

declare type local:y as jsound verbose {
  "kind" : "array",
  "content" : "date"
};

validate type local:x* {
  [ ],
  [ "foo" ],
  [ "foo", "bar" ],
  [ "foo", "bar", 1, 2, 3 ]
},
validate type local:x {
  [ ]
},
validate type local:x {
  [ "foo" ]
},
validate type local:x {
  [ "foo", "bar" ]
},
validate type local:x {
  [ "foo", "bar", 1, 2, 3 ]
},
try {
  validate type local:x {
    { "foo" : 2, "nested" : { "foobar" : "AABBCC" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    "foo"
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    [ { "foo" : 1 } ]
  }
} catch XQDY0027 {
  "Success"
},

validate type local:y* {
  [ ],
  [ "2021-01-01" ],
  [ "2021-01-01", "2021-07-21" ]
},
validate type local:y {
  [ "2021-01-01", "2021-07-21" ]
},
try {
  validate type local:y {
    [ "foo" ]
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y* {
    [ "foo" ]
  }
} catch XQDY0027 {
  "Success"
}
