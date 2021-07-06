(:JIQS: ShouldRun; Output="(Success, Success)" :)
declare type local:a as { "foo" : "integer" };
declare type local:b as { "foo" : "integer", "bar" : "string" };

try {
validate type local:a* { validate type local:b* { { "foo" : 1, "bar" : 2 } } }
} catch XQDY0027 {
  "Success"
},

try {
validate type local:a* { { "foo" : 1, "bar" : 2 } }
} catch XQDY0027 {
  "Success"
}