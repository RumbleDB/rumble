(:JIQS: ShouldNotParse; ErrorCode="XQST0012"; ErrorMetadata="LINE:1:COLUMN:0:" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    { "name" : "foo", "type" : "integer" }
  ],
  "closed" : false
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "baseType" : "local:x",
  "content" : [
  ]
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "baseType" : "local:x",
  "content" : [
    { "name" : "foo", "type" : "decimal" }
  ]
};



()
