(:JIQS: ShouldNotParse; ErrorCode="XQDY0027"; ErrorMetadata="LINE:1:COLUMN:0:" :)
declare type local:x as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "string",
  "minLength" : 2
};

validate type local:x* {
  [ ],
  [ "foo" ],
  [ "foo", "bar" ]
}

(: test minLength :)