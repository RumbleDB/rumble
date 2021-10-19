(:JIQS: ShouldRun; Output="Success" :)
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
            "type" : "positiveInteger",
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
try {
  validate type local:x* {
    { "foo" : 2, "nested" : { "bar" : -20, "foobar" : "AABBCC" } }
  }
} catch XQDY0027 {
  "Success"
}

