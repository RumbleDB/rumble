(:JIQS: ShouldRun; Output="(Success, { "foo" : [ { "bar" : 2, "foobar" : "foo" }, { "bar" : 1, "foobar" : "bar" } ] }, { "foo" : [ { "bar" : 2, "foobar" : "foo" } ] }, { "foo" : [ ] }, Success, Success, { "foo" : [ { "foo" : "2025-10-01", "bar" : 1, "foobar" : "foo" }, { "foo" : "2025-10-01", "bar" : 2, "foobar" : "bar" } ] }, { "foo" : [ { "foo" : "2025-10-01", "bar" : 2, "foobar" : "foo" }, { "foo" : "2025-10-02", "bar" : 2, "foobar" : "bar" } ] })" :)
declare type local:x as { "foo" : [ { "@bar" : "integer" } ] };
declare type local:y as { "foo" : [ { "@bar" : "integer", "foobar" : "string" } ] };
declare type local:z as { "foo" : [ { "@foo" : "date", "@bar" : "integer", "foobar" : "string" } ] };
try {
  validate type local:x* {
    { "foo" : [ { "bar" : 1 }, { "bar" : 2 }, { "bar" : 3 }, { "bar" : 1 }, { "bar" : 2 } ] }
  }
} catch XQDY0027 {
  "Success"
},
validate type local:y* {
  { "foo" : [ { "bar" : 2, "foobar" : "foo" }, { "bar" : 1, "foobar" : "bar" } ] }
},
validate type local:y* {
  { "foo" : [ { "bar" : 2, "foobar" : "foo" } ] }
},
validate type local:y* {
  { "foo" : [ ] }
},
try {
  validate type local:y* {
    { "foo" : [ { "bar" : 24, "foobar" : "foo" }, { "bar" : 24, "foobar" : "bar" } ] }
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:z* {
    { "foo" : [ { "date" : "2025-10-01", "bar" : "2", "foobar" : "foo" }, { "date" : "2025-10-01", "bar" : "2", "foobar" : "bar" } ] }
  }
} catch XQDY0027 {
  "Success"
},
validate type local:z* {
  { "foo" : [ { "foo" : "2025-10-01", "bar" : "1", "foobar" : "foo" }, { "foo" : "2025-10-01", "bar" : "2", "foobar" : "bar" } ] }
},
validate type local:z* {
  { "foo" : [ { "foo" : "2025-10-01", "bar" : "2", "foobar" : "foo" }, { "foo" : "2025-10-02", "bar" : "2", "foobar" : "bar" } ] }
}
