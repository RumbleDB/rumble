(:JIQS: ShouldNotParse; ErrorCode="XQST0012"; ErrorMetadata="LINE:1:COLUMN:0:" :)
declare type local:smallerByte as jsound verbose {
    "kind": "atomic",
    "baseType": "xs:byte",
    "maxInclusive": 120
};
declare type local:middleByte as jsound verbose {
    "kind": "atomic",
    "baseType": "local:smallerByte",
    "maxInclusive": 125
};
()