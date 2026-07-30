(:JIQS: ShouldRun; Output="([ "foo" ], [ "foo", "bar" ], [ "foo", "bar", "foobar" ], Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "string",
  "minLength" : 1,
  "maxLength" : 3
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
      "type" : "string"
    }
  ]
};

validate type local:x* {
  [ "foo" ],
  [ "foo", "bar" ],
  [ "foo", "bar", "foobar" ]
},
try {
  validate type local:x {
    []
  }
} catch XQDY0027 {
  "Success"
},
try {
    validate type local:y {
      { "foo" : ["foo", "bar", "foobar", "foobarfoo"], "bar" : "bar" }
    }
} catch XQDY0027 {
   "Success"
}

(: test minLength and maxLength :)