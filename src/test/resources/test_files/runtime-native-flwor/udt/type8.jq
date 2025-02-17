(:JIQS: ShouldRun; Output="({ "foo" : 2, "bar" : "2021-01-01", "foobar" : "AABBCC" }, { "bar" : "2021-02-02" }, { "foo" : 2, "bar" : "2021-01-01", "foobar" : "AABBCC" }, { "bar" : "2021-02-02" }, Success, Success, Success, Success, Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    {
      "name" : "foo",
      "type" : "integer"
    },
    {
      "name" : "bar",
      "type" : "date",
      "required" : true
    },
    {
      "name" : "foobar",
      "type" : "hexBinary",
      "required" : false
    }
  ]
};

validate type local:x* {
  { "foo" : 2, "bar" : "2021-01-01", "foobar" : "AABBCC" },
  { "bar" : "2021-02-02" }
},
validate type local:x {
  { "foo" : 2, "bar" : "2021-01-01", "foobar" : "AABBCC" }
},
validate type local:x? {
  { "bar" : "2021-02-02" }
},
try {
  validate type local:x {
    { "foo" : 2, "foobar" : "AABBCC" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    { "foo" : 2, "bar" : "2021-01-01", "foobar" : "AABBCCJK" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    { "foo" : 2, "bar" : "123", "foobar" : "AABBCC" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : 2, "foobar" : "AABBCC" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : 2, "bar" : "2021-01-01", "foobar" : "AABBCCJK" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : 2, "bar" : "123", "foobar" : "AABBCC" }
  }
} catch XQDY0027 {
  "Success"
}