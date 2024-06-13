(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_utils = "jsoniq_utils.jq";

declare function jsoniq_utils:cast-as($value, $type as string) {
    switch ($type)
        case "string" return $value cast as string
        case "integer" return $value cast as integer
        case "numeric" return $value cast as numeric
        case "float" return $value cast as float
        case "decimal" return $value cast as decimal
        case "double" return $value cast as double
        case "boolean" return $value cast as boolean
        case "null" return $value cast as null
        default return $value cast as integer
};

(: Function returns the shape of the array as a list where each index represents the number of elements in that particular dimension.
Required params are:
- arr (array): the array to compute the shape for :)
declare function jsoniq_utils:shape($arr as array) {
    variable $shape := [];
    variable $pos := 1;
    variable $it := $arr;
    try {
        while (size($it) gt 0) {
            insert json size($it) into $shape at position $pos;
            $pos := $pos + 1;
            $it := $it[[1]];
        }
    } catch XPTY0004 {
        (: While loop stops when $it becomes a single value :)
        ();
    }
    $shape
};