(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_pandas = "jsoniq_pandas.jq";
import module namespace jsoniq_numpy = "jsoniq_numpy.jq";

declare type jsoniq_pandas:describe_params as {
    "include": "string=all",
    "percentiles": "array"
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
    for $column in keys($dataframe)
    return { $column:
        switch($params.include)
            case "all" return jsoniq_pandas:all_report([$dataframe.$column], $params)
            case "number" return jsoniq_pandas:number_report([$dataframe.$column], $params)
            case "object" return jsoniq_pandas:object_report([$dataframe.$column])
            default return error("Unrecognized include option. Only 'number' and 'object' are supported.")
    }
};

declare function jsoniq_pandas:describe($dataframe as object*) {
    jsoniq_pandas:describe($dataframe, {})
};

declare function jsoniq_pandas:all_report($column, $params as object) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:int" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:decimal" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:float" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:double" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:boolean" return jsoniq_pandas:categorical_report($column)
        default return jsoniq_pandas:categorical_report($column)
};

declare function jsoniq_pandas:number_report($column, $params as object) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:int" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:decimal" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:float" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:double" return jsoniq_pandas:numerical_report($column, $params)
        default return ()
};

declare function jsoniq_pandas:object_report($column) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:boolean" return jsoniq_pandas:categorical_report($column)
        case "xs:string" return jsoniq_pandas:categorical_report($column)
        default return ()
};

declare function jsoniq_pandas:numerical_report($column as array, $params as object) {
    (: Compute count, mean, std, min, .25, .5, .75, max :)
    let $count := size($column)
    let $mean := jsoniq_numpy:mean($column)
    let $std := jsoniq_pandas:std($column, $mean)
    let $min := jsoniq_numpy:min($column)
    let $max := jsoniq_numpy:max($column)
    let $sorted_arr := jsoniq_numpy:sort($column)
    let $percentiles := jsoniq_pandas:get_percentiles($params.percentiles)
    return {|
    {
        "count": $count,
        "mean": $mean,
        "std": $std,
        "min": $min,
        "max": $max
    },
    for $percentile in $percentiles[]
    return {string($percentile * 100) || "%": jsoniq_pandas:compute_percentile($sorted_arr, $percentile)}
    |}
};

declare function jsoniq_pandas:get_percentiles($params_percentiles) {
    if (empty($params_percentiles)) then [.25, .5, .75]
    else $params_percentiles
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
    let $occurences := jsoniq_pandas:count_occurences($column)
    let $top := $occurences[1].value
    let $frequency := $occurences[1].count
    return {
        "count": $count,
        "unique": $unique,
        "top": $top,
        "frequency": $frequency
    }
};

declare function jsoniq_pandas:count_occurences($column) {
    for $value in $column[]
    let $group_key := $value
    group by $group_key
    return {"value": $group_key, "count": count($value)}
};

declare function jsoniq_pandas:compute_percentile($arr as array, $percentile as double) {
    let $distance_of_min_to_max := size($arr) - 1
    let $percentile_index := $distance_of_min_to_max * $percentile + 1
    let $percentile_index_integer_part := floor($percentile_index)
    let $percentile_index_fractional_part := $percentile_index - $percentile_index_integer_part
    let $adjacent_difference := $arr[[$percentile_index]] + $percentile_index_fractional_part * ($arr[[$percentile_index_integer_part + 1]] - $arr[[$percentile_index_integer_part]])
    return $adjacent_difference
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

(: sample returns a random sample from the DataFrame. We currently only support returning results from the first axis, that is rows of the DataFrame. We do not support weighted samples or fractional samples. We only support sampling with replacement.
Required params are:
- dataframe (DataFrame): the dataframe to sample from.
- n (integer): number of samples to return.
:)
declare function jsoniq_pandas:sample($dataframe as object*, $num as integer) {
    if ($num lt 0) then ()
    else
        let $size_dataframe := count($dataframe)
        let $random_numbers := jsoniq_numpy:random_randint(1, {"size": [$size_dataframe], "high": $size_dataframe})
        for $i in 1 to $num
        return $dataframe[$random_numbers[$i]]
};

declare function jsoniq_pandas:sample($dataframe as object*, $num as integer, $seed as integer) {
    if ($num lt 0) then ()
        else
            let $size_dataframe := count($dataframe)
            let $random_numbers := random-between(1, $size_dataframe, $num, "integer", $seed)
            for $i in 1 to $num
            return $dataframe[$random_numbers[$i]]
};


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
    (: TODO: Add support for limited number of replacements :)
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
    (: TODO: Add support for limited number of replacements :)
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
    let $keys := keys($dataframe)
    return 
        if ($params.axis eq 0) then 
            jsoniq_pandas:remove_rows($dataframe, $params.how, $keys)
        else
            jsoniq_pandas:remove_columns($dataframe, $params.how, $keys)
};

declare function jsoniq_pandas:remove_columns($dataframe as object*, $how as string, $keys) {
    let $columns_to_remove :=
        for $column_name in $keys
        where jsoniq_pandas:column_has_null($dataframe.$column_name, $how) eq true
        return $column_name
    return
        if (count($columns_to_remove) gt 0) then remove-keys($dataframe, $columns_to_remove)
        else $dataframe
};

declare function jsoniq_pandas:remove_rows($dataframe as object*, $how as string, $keys) {
    if ($how eq "any") then {
        for $row in $dataframe
        where (count(keys($row)) eq count ($keys))
        return $row
    }
    else {
        for $row in $dataframe
        where (count(keys($row)) gt 0)
        return $row
    }
};

declare function jsoniq_pandas:column_has_null($column, $how as string) {
    if ($how eq "any") then {
        variable $i := 1;
        variable $size := count($column);
        variable $column_flat := flatten($column);
        while($i le $size) {
            if (is-null($column_flat[$i])) then exit returning true;
            else ();
            $i := $i + 1;
        }
        exit returning false;
    }
    else {
        variable $i := 1;
        variable $size := count($column);
        variable $column_flat := flatten($column);
        while($i le $size) {
            if (not is-null($column_flat[$i])) then exit returning false;
            else ();
            $i := $i + 1;
        }
        exit returning true;
    }
};