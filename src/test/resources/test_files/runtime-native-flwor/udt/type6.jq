(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { }, true, true, Success, Success, Success, Success, Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    {
      "name" : "foo",
      "type" : "integer"
    }
  ]
};

validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3 },
  { }
},
validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3 },
  { }
} instance of local:x+,
(validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3 },
  { }
}).foo instance of integer+,
try {
  validate type local:x* {
    { "foo" : 2, "bar" : 3 }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : "foo" }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : [ 1 ] }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    { "foo" : { "bar" : 2 } }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    2, 3
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:x* {
    [ "foo" ]
  }
} catch XQDY0027 {
  "Success"
}
