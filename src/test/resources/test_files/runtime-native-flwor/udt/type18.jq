(:JIQS: ShouldRun; Output="Success" :)
declare type local:x as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "string",
  "maxLength" : 2
};
try {
    validate type local:x* {
      [ ],
      [ "foo" ],
      [ "foo", "bar" ],
      [ "foo", "bar", 1, 2, 3 ]
    }
} catch XQDY0027 {
    "Success"
}



