(:JIQS: ShouldRun; Output="({ "foo" : 42 }, { "foo" : null }, { "foo" : null }, { "foo" : 42 }, { }, Success, { "foo" : 42 }, Success, Success, { "foo" : 42 }, { "foo" : null }, { "foo" : null }, { "foo" : [ { "bar" : "foo" } ] }, { "foo" : null }, { "foo" : null }, { "foo" : { "bar" : "foo" } }, { "foo" : null }, { "foo" : null })" :)
declare type local:canbenull as { "foo?" : "integer" };
declare type local:optional as { "foo" : "integer" };
declare type local:required as { "!foo" : "integer" };
declare type local:requiredcanbenull as { "!foo?" : "integer" };
declare type local:z as { "foo?" : [ { "bar" : "string" } ] };
declare type local:w as { "foo?" : { "bar" : "string" } };
validate type local:canbenull* {
    { "foo" : 42 }
},
validate type local:canbenull* {
    { "foo" : "null" }
},
validate type local:canbenull* {
    { "foo" : null }
},
validate type local:optional* {
    { "foo" : 42 }
},
validate type local:optional* {
    { "foo" : null }
},
try {
  validate type local:optional* {
    { "foo" : "null" }
  }
} catch XQDY0027 {
  "Success"
},
validate type local:required* {
    { "foo" : 42 }
},
try {
  validate type local:required* {
    { "foo" : null }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:required* {
    { "foo" : "null" }
  }
} catch XQDY0027 {
  "Success"
},
validate type local:requiredcanbenull* {
    { "foo" : 42 }
},
validate type local:requiredcanbenull* {
    { "foo" : null }
},
validate type local:requiredcanbenull* {
    { "foo" : "null" }
},
validate type local:z* {
    { "foo" : [ { "bar" : "foo" } ] }
},
validate type local:z* {
    { "foo" : null }
},
validate type local:z* {
    { "foo" : "null" }
},
validate type local:w* {
    { "foo" : { "bar" : "foo" } }
},
validate type local:w* {
    { "foo" : null }
},
validate type local:w* {
    { "foo" : "null" }
}