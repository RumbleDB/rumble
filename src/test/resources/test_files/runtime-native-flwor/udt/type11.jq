(:JIQS: ShouldRun; Output="({ "foo" : 2, "nested" : { "bar" : 2021-01-01, "foobar" : AABBCC } }, { "nested" : { "bar" : 2021-02-02, "foobarbar" : [ ] } }, { "foo" : 2, "nested" : { "bar" : 2021-01-01, "foobar" : AABBCC } }, { "nested" : { "bar" : 2021-02-02, "foobarbar" : [ ] } }, Success, Success, Success, Success, Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    {
      "name" : "foo",
      "type" : "integer"
    },
    {
      "name" : "nested",
      "type" : {
        "kind" : "object",
        "baseType" : "object",
        "content" : [
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
        ],
        "closed" : false
      },
      "required" : false
    }
  ]
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
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
  ],
  "closed" : false
};

validate type local:x* {
  { "foo" : 2, "nested" : { "bar" : "2021-01-01", "foobar" : "AABBCC" } },
  { "nested" : { "bar" : "2021-02-02", "foobarbar": [] } }
},
validate type local:x {
  { "foo" : 2, "nested" : { "bar" : "2021-01-01", "foobar" : "AABBCC" } }
},
validate type local:x {
  { "nested" : { "bar" : "2021-02-02", "foobarbar": [] } }
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
    { "foo" : 2, "nested" : { "bar" : "2021-01-01", "foobar" : "AABBCCJK" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x {
    { "foo" : 2, "nested" : { "bar" : "123", "foobar" : "AABBCC" } }
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
  validate type local:x* {
    { "foo" : 2, "nested" : { "bar" : "2021-01-01", "foobar" : "AABBCCJK" } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : 2, "nested" : { "bar" : "123", "foobar" : "AABBCC" } }
  }
} catch XQDY0027 {
  "Success"
}