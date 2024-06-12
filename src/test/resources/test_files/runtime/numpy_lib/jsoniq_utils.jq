(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_utils = "jsoniq_utils.jq";

declare function jsoniq_utils:cast-as($value, $type as string) {
    switch ($type)
        case "string" return $value cast as string;
        case "integer" return $value cast as integer;
        case "numeric" return $value cast as numeric;
        case "float" return $value cast as float;
        case "decimal" return $value cast as decimal;
        case "double" return $value cast as double;
        case "boolean" return $value cast as boolean;
        case "null" return $value cast as null;
        default return $value cast as integer;
};