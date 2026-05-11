(:JIQS: ShouldRun; Output="({ }, { "foo" : { } }, { "bar" : { } }, { "foo" : { }, "bar" : { } }, { "foo" : { } }, { "bar" : { } }, { "foo" : { }, "bar" : { } }, { }, { }, Success, Success, Success, Success, Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "content" : [ ]
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "content" : [
    {
      "name" : "foo",
      "type" : "local:x"
    },
    {
      "name" : "bar",
      "type" : {
        "kind" : "object",
        "content" : [ ]
      }
    }
  ]
};

validate type local:y* {
  { },
  { "foo" : { } },
  { "bar" : { } },
  { "foo" : { }, "bar" : { } }
},
validate type local:y {
  { "foo" : { } }
},
validate type local:y {
  { "bar" : { } }
},
validate type local:y {
  { "foo" : { }, "bar" : { } }
},
validate type local:x {
  { }
},
validate type local:x* {
  { }
},
try {
  validate type local:x {
    { "foo" : 2, "nested" : { "foobar" : "AABBCC" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : 2, "nested" : { "foobar" : "AABBCC" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y {
    { "foo" : { "bar" : 2 } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y {
    { "bar" : { "bar" : 2 } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y* {
    { "foo" : { "bar" : 2 } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y* {
    { "bar" : { "bar" : 2 } }
  }
} catch XQDY0027 {
  "Success"
}