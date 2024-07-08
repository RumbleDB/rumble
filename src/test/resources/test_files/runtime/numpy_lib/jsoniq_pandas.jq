(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_pandas = "jsoniq_pandas.jq";
import module namespace jsoniq_numpy = "jsoniq_numpy.jq";

declare type jsoniq_pandas:describe_params as {
    "include": "string=all"
};
(: describe generates descriptive statistics about a dataset. Statistics summarize the central tendency, dispersion and shape of a dataset, excluding null values. Provides a string/dataframe as result.
TODO: Supported percentiles are only [.25, .5, .75].
Required params are:
- dataframe (DataFrame): The dataframe to look into
Params is an object for optional arguments. These arguments are:
- include (array) - type of data to include in the description. To return only numeric summaries for numeric types, submit 'number'. To return only object summaries for object types, submit 'categorical'. To return all types, submit 'all'.
- exclude - not supported :)
declare function jsoniq_pandas:describe($dataframe as object*, $params as object) {
    let $params := validate type jsoniq_pandas:describe_params {$params}
    for $row in $dataframe
    for $column in keys($row)
    return { $column:
        switch($params.include)
        case "all" return jsoniq_pandas:all_report($row.$column)
        case "number" return jsoniq_pandas:number_report($row.$column)
        case "object" return jsoniq_pandas:object_report($row.$column)
        default return error("Unrecognized include option. Only 'number' and 'object' are supported.")
    }
};

declare function jsoniq_pandas:describe($dataframe as object*) {
    jsoniq_pandas:describe($dataframe, {})
};

declare function jsoniq_pandas:all_report($column) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:int" return jsoniq_pandas:numerical_report($column)
        case "xs:decimal" return jsoniq_pandas:numerical_report($column)
        case "xs:float" return jsoniq_pandas:numerical_report($column)
        case "xs:double" return jsoniq_pandas:numerical_report($column)
        case "xs:boolean" return jsoniq_pandas:categorical_report($column)
        default return jsoniq_pandas:categorical_report($column)
};

declare function jsoniq_pandas:number_report($column) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:int" return jsoniq_pandas:numerical_report($column)
        case "xs:decimal" return jsoniq_pandas:numerical_report($column)
        case "xs:float" return jsoniq_pandas:numerical_report($column)
        case "xs:double" return jsoniq_pandas:numerical_report($column)
        default return ()
};

declare function jsoniq_pandas:object_report($column) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:boolean" return jsoniq_pandas:categorical_report($column)
        case "xs:string" return jsoniq_pandas:categorical_report($column)
        default return ()
};

declare function jsoniq_pandas:numerical_report($column) {
    (: Compute count, mean, std, min, .25, .5, .75, max :)
    let $count := size($column)
    let $mean := jsoniq_numpy:mean($column)
    let $std := jsoniq_pandas:std($column, $mean)
    let $min := jsoniq_numpy:min($column)
    let $max := jsoniq_numpy:max($column)
    let $sorted_arr := jsoniq_numpy:sort($column)
    let $percentile_25 := jsoniq_pandas:compute_percentile($sorted_arr, .25)
    let $percentile_50 := jsoniq_pandas:compute_percentile($sorted_arr, .50)
    let $percentile_75 := jsoniq_pandas:compute_percentile($sorted_arr, .75)
    return {
        "count": $count,
        "mean": $mean,
        "std": $std,
        "min": $min,
        "max": $max,
        "25%": $percentile_25,
        "50%": $percentile_50,
        "75%": $percentile_75
    }
};

declare function jsoniq_pandas:std($arr as array, $mean as double) {
    let $accumulated :=
        for $value in $arr[]
        return pow($value - $mean, 2)
    let $sum_accumulated := fn:sum($accumulated)
    let $sample_size := size($arr) - 1
    return float(sqrt($sum_accumulated div $sample_size))
};

declare function jsoniq_pandas:categorical_report($column) {
    let $count := size($column)
    let $unique := size(jsoniq_numpy:unique($column))
    let $top := jsoniq_numpy:max($column)
    let $frequency := jsoniq_pandas:count_occurences($column, $top)
    return {
        "count": $count,
        "unique": $unique,
        "top": $top,
        "frequency": $frequency
    }
};

declare function jsoniq_pandas:compute_percentile($arr as array, $percentile) {
    let $distance_of_min_to_max := size($arr) - 1
    let $percentile_index := $distance_of_min_to_max * $percentile + 1
    let $percentile_index_integer_part := floor($percentile_index)
    let $percentile_index_fractional_part := $percentile_index - $percentile_index_integer_part
    let $adjacent_difference := $arr[[$percentile_index]] + $percentile_index_fractional_part * ($arr[[$percentile_index_integer_part + 1]] - $arr[[$percentile_index_integer_part]])
    return $adjacent_difference
};

declare function jsoniq_pandas:count_occurences($arr as array, $value) {
    variable $count := 0;
    variable $index := 1;
    while ($index le size($arr)) {
        if ($arr[[$index]] eq $value) then $count := $count + 1;
        else ();
        $index := $index + 1;
    }
    exit returning $count;
};


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
declare function jsoniq_pandas:isnull($dataframe as object*) {
    for $row in $dataframe
    return jsoniq_pandas:isnull_object($row)
};


declare function jsoniq_pandas:isnull_value($value) {
    if ($value eq null) then true
    else false
};

declare function jsoniq_pandas:isnull_array($array as array) {
    (: TODO: Error handling. Limit. :)
    for $value in $array[]
    return typeswitch($value)
        case array return jsoniq_pandas:isnull_array($value)
        case object return jsoniq_pandas:isnull_object($value)
        default return jsoniq_pandas:isnull_value($value)
};

declare function jsoniq_pandas:isnull_object($object as object) {
    {|
        for $key in keys($object)
        let $value := $object.$key
        return
            typeswitch($value)
            case object return 
                let $result := jsoniq_pandas:isnull_object($value)
                return {$key: $result}
            case array return
                let $result := jsoniq_pandas:isnull_array($value)
                return {$key: $result}
            default return
                let $result := jsoniq_pandas:isnull_value($value)
                return {$key: $result}
    |}
};


declare type jsoniq_pandas:fillna_params as {
    "value": "item",
    "limit": "integer=1000"  
};

declare function jsoniq_pandas:fillna_value($value, $params as object) {
    if ($value eq null) then $params.value
    else $value  
};

declare function jsoniq_pandas:fillna_array($array as array, $params as object) {
    (: TODO: Error handling. Limit. :)
    for $value in $array[]
    return typeswitch($value)
        case array return jsoniq_pandas:fillna_array($value, $params)
        case object return jsoniq_pandas:fillna_object($value, $params)
        default return jsoniq_pandas:fillna_value($value, $params)
};

declare function jsoniq_pandas:fillna_object($object as object, $params as object) {
    {|
        for $key in keys($object)
        let $value := $object.$key
        return
            typeswitch($value)
            case object return 
                let $result := jsoniq_pandas:fillna_object($value, $params)
                return {$key: $result}
            case array return
                let $result := jsoniq_pandas:fillna_array($value, $params)
                return {$key: $result}
            default return
                let $result := jsoniq_pandas:fillna_value($value, $params)
                return {$key: $result}
    |}
};

(: fillna replaces null values with specified values. It returns a new DataFrame with the replacement result.
Required params are:
- dataframe (DataFrame): the dataframe to fill nulls in.
Params is an object for optional arguments. These arguments are:
- value (integer): the value to replace null's with.
- limit (integer): how many null's to fill. If unspecified, all nulls are replaced.
:)
declare function jsoniq_pandas:fillna($dataframe as object*, $params as object) {
    let $params := validate type jsoniq_pandas:fillna_params {$params}
    for $row in $dataframe
    return jsoniq_pandas:fillna_object($row, $params)
};

declare function jsoniq_pandas:fillna($dataframe as object*) {
    jsoniq_pandas:fillna($dataframe, {})
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
:)
declare function jsoniq_pandas:dropna($dataframe as object*, $params as object) {
    let $params := validate type jsoniq_pandas:dropna_params {$params}
    return 
        if ($params.axis eq 0) then {
            (: Remove rows :)
            for $row in $dataframe
            return jsoniq_pandas:dropna_how_object($row, $params.how)
        } else {
            (: Remove columns :)
            for $column_name in keys($dataframe)
            return jsoniq_pandas:dropna_how_array($dataframe.$column_name, $params.how)
        }
};

declare function jsoniq_pandas:dropna_how_object($object as object, $how as string) {
    {|
        for $key in keys($object)
        return
            if ($how eq "any") then
                if (jsoniq_pandas:has_na($object.$key)) then ()
                else {$key: $object.$key}
            else
                if (jsoniq_pandas:has_all_na($object.$key)) then ()
                else {$key: $object.$key}
    |}
};

declare function jsoniq_pandas:dropna_how_array($array as array, $how as string) {
    [
        for $value in $array[]
        return
            if ($how eq "any") then
                if (jsoniq_pandas:has_na($value)) then ()
                else $value
            else
                if (jsoniq_pandas:has_all_na($value)) then ()
                else $value
    ]
};

declare function jsoniq_pandas:has_na($data) {
    variable $res := false;
    typeswitch($data)
        case object return
            for $key in keys($data)
            return if (jsoniq_pandas:has_na($data.$key)) then {$res := true; break loop;}
            else ();
        case array return
            for $value in $data[]
            return if (jsoniq_pandas:has_na($value)) then {$res := true; break loop;}
            else ();
        default return if ($data eq null) then $res := true;
                       else $res := false;
    exit returning $res;
};

declare function jsoniq_pandas:has_all_na($data) {
    variable $res := false;
    typeswitch($data)
        case object return
            for $key in keys($data)
            return if (jsoniq_pandas:has_all_na($data.$key)) then $res := true;
            else {$res := false; break loop;}
        case array return
            for $value in $data[]
            return if (jsoniq_pandas:has_all_na($value)) then $res := true;
            else {$res := false; break loop;}
        default return if ($data eq null) then $res := true;
                       else $res := false;
    exit returning $res;
};