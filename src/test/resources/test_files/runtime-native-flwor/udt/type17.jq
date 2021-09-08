(:JIQS: ShouldRun; Output="({ "foo" : 2, "bar" : "2021-01-01" }, { "foo" : 22 }, { "bar" : "2021-01-01" }, { "foo" : 2, "bar" : "2021-01-01" }, { "foo" : 22 }, { "foo" : 22 }, { "foo" : 24 }, Success, Success, true, true, true, true)" :)
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
},
validate type local:y {
  { "foo" : "2", "bar" : "2021-01-01" }
} instance of local:y,
validate type local:y {
  { "foo" : "2", "bar" : "2021-01-01" }
} instance of local:x,
validate type local:y* {
  { "foo" : "2", "bar" : "2021-01-01" }
} instance of local:y+,
validate type local:y+ {
  { "foo" : "2", "bar" : "2021-01-01" }
} instance of local:x*