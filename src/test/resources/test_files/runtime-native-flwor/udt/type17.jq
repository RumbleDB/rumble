(:JIQS: ShouldRun; Output="([ 1, 2 ], [ ], [ 1 ], [ 1, 2 ], [ 1.2, 2 ], [ 1.2, 2 ], Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    { "name" : "foo", "type" : "integer" }
  ]
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "baseType" : "local:x",
  "content" : [
    { "name" : "bar", "type" : "date" }
  ]
};

validate type local:y* {
  { "foo" : "2", "bar" : "2021-01-01" },
  { "foo" : 22 },
  { "bar" : "2021-01-01" }
},
validate type local:y {
  { "foo" : "2", "bar" : "2021-01-01" }
},
validate type local:x {
  { "foo" : 22 }
},
validate type local:x* {
  { "foo" : 22 },
  { "foo" : 24 }
},
try {
  validate type local:x {
    { "foo" : "2", "bar" : "2021-01-01" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y* {
    { "foo" : "1.2", "bar" : "2021-01-01" }
  }
} catch XQDY0027 {
  "Success"
}