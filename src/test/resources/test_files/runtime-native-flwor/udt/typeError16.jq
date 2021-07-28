(:JIQS: ShouldNotParse; ErrorCode="XQST0012"; ErrorMetadata="LINE:1:COLUMN:0:" :)
declare type local:x as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "decimal"
};
declare type local:y as jsound verbose {
  "kind" : "array",
  "baseType" : "local:x",
  "content" : "string"
};

()