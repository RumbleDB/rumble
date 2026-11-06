(:JIQS: ShouldRun; Output="({ }, { "foo" : { } }, { "bar" : { } }, { "foo" : { }, "bar" : { } }, { "foo" : { } }, { "bar" : { } }, { "foo" : { }, "bar" : { } }, { }, { }, { "foo" : 2, "nested" : { "foobar" : "AABBCC" } }, { "foo" : 2, "nested" : { "foobar" : "AABBCC" } }, { "foo" : { "bar" : 2 } }, { "bar" : { "bar" : 2 } }, { "foo" : { "bar" : 2 } }, { "bar" : { "bar" : 2 } })" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "content" : [ ],
  "closed" : false
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
        "content" : [ ],
        "closed" : false
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
validate type local:x {
  { "foo" : 2, "nested" : { "foobar" : "AABBCC" } }
},
validate type local:x* {
  { "foo" : 2, "nested" : { "foobar" : "AABBCC" } }
},
validate type local:y {
  { "foo" : { "bar" : 2 } }
},
validate type local:y {
  { "bar" : { "bar" : 2 } }
},
validate type local:y* {
  { "foo" : { "bar" : 2 } }
},
validate type local:y* {
  { "bar" : { "bar" : 2 } }
}
