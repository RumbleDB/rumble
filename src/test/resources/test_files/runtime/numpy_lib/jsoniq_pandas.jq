(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_pandas = "jsoniq_pandas.jq";
import module namespace jsoniq_numpy = "jsoniq_numpy.jq";
import module namespace functx = "functx.jq";

(: describe generates descriptive statistics about a dataset. Statistics summarize the central tendency, dispersion and shape of a dataset, excluding null values. Provides a string/dataframe as result.
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

declare type jsoniq_pandas:describe_params as {
    "include": "string=all",
    "percentiles": "array"
};

declare function jsoniq_pandas:describe($dataframe as object*) {
    jsoniq_pandas:describe($dataframe, {})
};

declare function jsoniq_pandas:all_report($column, $params as object) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:int" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:integer" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:decimal" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:float" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:double" return jsoniq_pandas:numerical_report($column, $params)
        case "xs:boolean" return jsoniq_pandas:categorical_report($column)
        default return jsoniq_pandas:categorical_report($column)
};

declare function jsoniq_pandas:number_report($column, $params as object) {
    let $column_type := item-type($column)
    return switch($column_type)
        case "xs:integer" return jsoniq_pandas:numerical_report($column, $params)
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

declare function jsoniq_pandas:categorical_report($column as array) {
    let $count := size($column)
    let $unique := count(functx:distinct-deep($column[]))
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
    let $column_values := $column[]
    return if ($column_values instance of atomic*) then jsoniq_pandas:count_occurences_for_atomic($column_values)
           else if ($column_values instance of json-item*) then jsoniq_pandas:count_occurences_for_structured_item($column_values)
           else fn:error("Unrecognized type for dataframe. Only atomic and item types are supported.")
};

declare function jsoniq_pandas:count_occurences_for_atomic($column) {
    for $value in $column
    let $group_key := $value
    group by $group_key
    return {"value": $group_key, "count": count($value)}
};

declare function jsoniq_pandas:count_occurences_for_structured_item($column) {
    variable $counts :=
        let $distinct_values := functx:distinct-deep($column)
        for $value in $distinct_values
        return {"count": jsoniq_pandas:count_value($value, $column), "value": $value};
    for $count in $counts
    order by $count.count descending
    return $count
};

declare function jsoniq_pandas:count_value($value, $column) {
    let $frequency :=
        for $column_value in $column
        where deep-equal($value, $column_value)
        return 1
    return count($frequency)
};

declare function jsoniq_pandas:compute_percentile($arr as array, $percentile as double) {
    let $distance_of_min_to_max := size($arr) - 1
    let $percentile_index := $distance_of_min_to_max * $percentile + 1
    let $percentile_index_integer_part := floor($percentile_index)
    let $percentile_index_fractional_part := $percentile_index - $percentile_index_integer_part
    let $adjacent_difference := $arr[[$percentile_index]] + $percentile_index_fractional_part * ($arr[[$percentile_index_integer_part + 1]] - $arr[[$percentile_index_integer_part]])
    return $adjacent_difference
};

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

(: sample returns a random sample from the DataFrame with a seed. We currently only support returning results from the first axis, that is rows of the DataFrame. We do not support weighted samples or fractional samples. We only support sampling with replacement.
Required params are:
- dataframe (DataFrame): the dataframe to sample from.
- n (integer): number of samples to return.
- seed (integer): seed to be used for random number sampling.
:)
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
declare function jsoniq_pandas:isnull($dataframe as object*) as object* {
    let $keys := keys($dataframe)
    for $row in $dataframe
    return jsoniq_pandas:isnull_row($row, $keys)
};

declare function jsoniq_pandas:isnull_row($row as object, $keys) as object {
    {|
        for $key in $keys
        return if (empty($row.$key)) then {$key: true}
               else if ($row.$key instance of atomic) then 
                    if ($row.$key eq null) then {$key: true}
                    else {$key: false}
               else {$key: false}
    |}
};


(: fillna replaces null values with specified values. It returns a new DataFrame with the replacement result.
Required params are:
- dataframe (DataFrame): the dataframe to fill nulls in.
Params is an object for optional arguments. These arguments are:
- value (integer): the value to replace null's with.
:)
declare function jsoniq_pandas:fillna($dataframe as object*, $params as object) as object*{
    let $params := validate type jsoniq_pandas:fillna_params {$params}
    let $keys := keys($dataframe)
    for $row in $dataframe
    return jsoniq_pandas:fillna_row($row, $params, $keys)
};

declare function jsoniq_pandas:fillna($dataframe as object*) as object*{
    jsoniq_pandas:fillna($dataframe, {})
};

declare type jsoniq_pandas:fillna_params as {
    "value": "item"
};

declare function jsoniq_pandas:fillna_row($row as object, $params as object, $keys) as object{
    {|
        for $key in $keys
        return if (empty($row.$key)) then {$key: $params.value}
               else if ($row.$key instance of atomic) then 
                    if ($row.$key eq null) then {$key: $params.value}
                    else {$key: $row.$key}
               else {$key: $row.$key}
    |}
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

declare type jsoniq_pandas:dropna_params as {
    "axis": "integer=0",
    "how": "string=any"
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

