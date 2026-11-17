(:JIQS: ShouldCrash; ErrorCode="XQDY0027"; ErrorMetadata="LINE:1:COLUMN:0:" :)
declare type local:x as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "string",
  "maxLength" : 4
};

validate type local:x* {
  [ ],
  [ "foo" ],
  [ "foo", "bar" ],
  [ "foo", "bar", "foofoo", "barbar", "foobar" ]
}

(: test maxLength :)