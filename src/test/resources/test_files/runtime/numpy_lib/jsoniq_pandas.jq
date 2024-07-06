(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_pandas = "jsoniq_pandas.jq";
import module namespace utils = "jsoniq_utils.jq";


declare type jsoniq_pandas:describe_params as {
    "include": "array",
    "exclude": "array"
};
(: describe generates descriptive statistics about a dataset. Statistics summarize the central tendency, dispersion and shape of a dataset, excluding null values. Provides a string/dataframe as result.
TODO: Supported percentiles are only [.25, .5, .75].
Required params are:
- dataframe (DataFrame): The dataframe to look into
Params is an object for optional arguments. These arguments are:
- include (array) - list of types of data to include in the description. To return only numeric summaries for numeric types, submit 'number'. To return only object summaries for object types, submit 'object'. TODO: categorical data
- exclude (array) - list of types of data to exclude in the description. To exclude numeric summaries for numeric types, submit 'number'. To exclude object summaries for object types, submit 'object'. TODO: categorical data :)
declare function jsoniq_pandas:describe($dataframe as object*, $params as object) {};


declare type jsoniq_pandas:info_params as {
    "max_cols": "integer=10",
    "memory_usage": "boolean=false",
    "show_counts": "boolean=false"    
};
(: info prints a summary of a DataFrame to the screen.
Required params are:
- dataframe (DataFrame): The dataframe to look into
Params is an object for optional arguments. These arguments are:
- max_cols (integer): If the DataFrame has more columns, a truncated output is provided up to the given maximum column number.
- memory_usage (boolean): Provides memory size of the DataFrame (TODO)
- show_counts (boolean): Option to provide count of null elements in a column.:)
declare function jsoniq_pandas:info($dataframe as object*, $params as object){};

(: assign inserts new columns into a dataframe. Returns a new DataFrame with all of the original dataframe's columns in addition to new ones. Existing columns that are re-assigned are overwritten.
Required params are:
- dataframe (DataFrame): The dataframe to copy the columns from.
- columns (object): Pairs of string:array values to insert, where string is the column name and array is the values. Note, the array must have the same size as the number of entries in the DataFrame, otherwise an error is raised.
:)
declare function jsoniq_pandas:assign($to_insert as object) {};

(: sample returns a random sample from the DataFrame. We currently only support returning results from the first axis, that is rows of the DataFrame. We do not support weighted samples or fractional samples. We only support sampling with replacement.
Required params are:
- dataframe (DataFrame): the dataframe to sample from.
- n (integer): number of samples to return.
:)
declare function jsoniq_pandas:sample($dataframe as object*, $num as integer) {};


(: isnull returns a same-sized array indicating if values are null or not.
Required params are:
- dataframe (DataFrame): the dataframe to search nulls in.
:)
declare function jsoniq_pandas:isnull($dataframe as object*) {};


declare type jsoniq_pandas:fillna_params as {
    "value": "integer=0",
    "method": "string=none",
    "limit": "integer=1000"  
};

declare function jsoniq_pandas:fill_na_array($row_or_value as array, $params as object) {
    (: TODO: Error handling. Limit. Method :)
    typeswitch($row_or_value) 
        case array return
            for $value in $row_or_value
            return
                if (item-type($value) eq "object") then
                    jsoniq_pandas:fill_na_object($value, $params)
                else jsoniq_pandas:fill_na_array($value, $params)
        default return
            if ($row_or_value eq null) then $params.value
            else $row_or_value      
};

declare function jsoniq_pandas:fill_na_object($object as object, $params as object) {
    for $key in keys($object)
    let $value := $object.$key
    return
        if (item-type($value) eq "object") then 
            let $result := jsoniq_pandas:fill_na_object($value, $params)
            return {$key: $result}
        else
            let $result := jsoniq_pandas:fill_na_array($value, $params)
            return {$key: $result}
};

(: fillna replaces null values with specified values. It returns a new DataFrame with the replacement result.
Required params are:
- dataframe (DataFrame): the dataframe to fill nulls in.
Params is an object for optional arguments. These arguments are:
- value (integer): the value to replace null's with.
- method (string): bfill and ffill are supported. Method and value cannot be both specified!
- limit (integer): how many null's to fill. If unspecified, all nulls are replaced.
:)
declare function jsoniq_pandas:fillna($dataframe as object*, $params as object) {
    let $params := validate type jsoniq_pandas:fillna_params {$params}
    for $row in $dataframe
    return jsoniq_pandas:fill_na_array($row, $params)
};


declare type jsoniq_pandas:dropna_params as {
    "axis": "integer=0",
    "how": "string=any"
};
(: dropna removes rows or columns from DataFrames that contain nulls. The $axis parameter controls if rows or columns are removed, whereas the $how parameter controls the ruling for dropping the row or column.
Required params are:
- dataframe (DataFrame): the dataframe to drop nulls from.
Params is an object for optional arguments. These arguments are:
- axis (integer): the axis along to remove values from. Can only be 0 for rows or 1 for columns.
- how (string): 'any' or 'all' are supported.
(: :)
declare function jsoniq_pandas:dropna($dataframe as object*, $params as object) {
    let $params := validate type jsoniq_pandas:dropna_params {$params}
    return 
        if ($params.axis eq 0) then {
            (: Remove rows :)
            for $row in $dataframe
            return jsoniq_pandas:dropna_how($row, $params.how)
        } else {
            (: Remove columns :)
            for $column_name in keys($dataframe)
            return jsoniq_pandas:dropna_how($dataframe.$column_name, $params.how)
        }
}; :)

declare function jsoniq_pandas:dropna_how($data, $how as string) {
    $data
    (: if ($how eq "any") then
        typeswitch($data) 
            case object return
                for $key in keys($data)
                return if (jsoniq_pandas:has_na($data.$key)) then ()
                       else { $key: $data.$key }
            case array return
                if (jsoniq_pandas:has_na($data)) then ()
                else $data
            default return
                if ($data eq null) then ()
                else $data
    else
        typeswitch($data) 
            case object return
                for $key in keys($data)
                return if (not jsoniq_pandas:has_na($data.$key)) then { $key: $data.$key }
                       else ()
            case array return
                if (not jsoniq_pandas:has_na($data)) then $data
                else ()
            default return
                if ($data eq null) then ()
                else $data :)
};

(: declare function jsoniq_pandas:has_na($data) {
    variable $has_na := false; 
    typeswitch($data) 
        case object return
            for $key in keys($data)
            return if (jsoniq_pandas:has_na($data.$key)) then {$has_na := true; break loop;}
                   else ();
        case array return
            for $value in $data
            return
                if ($value eq null) then {$has_na := true; break loop;}
                else ();
        default return
            if ($data eq null) then $has_na := true;
            else ();
    exit returning $has_na;
}; :)