(:JIQS: ShouldNotParse; ErrorCode="XQST0012"; ErrorMetadata="LINE:1:COLUMN:0:" :)
declare type local:a as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "integer",
  "maxLength" : 6
};
declare type local:b as jsound verbose {
  "kind" : "array",
  "baseType" : "local:a",
  "content" : "integer",
  "maxLength" : 7
};

()