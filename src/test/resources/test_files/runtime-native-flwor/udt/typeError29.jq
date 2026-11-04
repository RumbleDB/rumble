(:JIQS: ShouldNotParse; ErrorCode="XQST0012"; ErrorMetadata="LINE:1:COLUMN:0:" :)
declare type local:a as jsound verbose { "kind": "atomic", "baseType": "local:b", "maxInclusive": 100 };
declare type local:b as jsound verbose { "kind": "atomic", "baseType": "local:a", "maxInclusive": 100 };
1
