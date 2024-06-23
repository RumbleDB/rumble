(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_numpy = "jsoniq_numpy.jq";
import module namespace utils = "jsoniq_utils.jq";

declare function jsoniq_numpy:zeros($shape as array, $zero, $current_dimensionension as integer) {
    if ($current_dimensionension eq size($shape)) then
      let $accumulated_result :=
                      for $j in 1 to $shape[[$current_dimensionension]]
                      return $zero
            return [$accumulated_result]
    else
      let $accumulated_result := 
                    let $higher_dimension_result := jsoniq_numpy:zeros($shape, $zero, $current_dimensionension + 1)
                    for $j in 1 to $shape[[$current_dimensionension]]
                    return $higher_dimension_result
      return [$accumulated_result] 
};

declare type jsoniq_numpy:zeros_params as {
    "type": "string=integer"
};
(: Zeros method creates an array filled with 0 values.
Required parameters are:
- shape (array): contains the size of each dimension of the resulting array. For one dimensional arrays, a single value within is array is expected - e.g., [7] results in a 1 dimensional array with 7 zeroed elements.
Params is an object for optional arguments. These arguments are:
- type (string): the enforced type of the resulting array.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value:)
declare function jsoniq_numpy:zeros($shape as array, $params as object) {
    let $params := validate type jsoniq_numpy:zeros_params {$params}
    return {
        if (size($shape) eq 1) then
            let $zero := utils:cast-as(0, $params.type)
            let $accumulated_result := for $j in 1 to $shape[[1]]
                         return $zero
            return [$accumulated_result]
        else
            let $current_dimensionension := 1
            let $zero := utils:cast-as(0, $params.type)
            let $accumulated_result :=
                        let $higher_dimension_result := jsoniq_numpy:zeros($shape, $zero, $current_dimensionension + 1)
                        for $j in 1 to $shape[[$current_dimensionension]]
                        return $higher_dimension_result
            return [$accumulated_result]
    }
};

declare function jsoniq_numpy:zeros($shape as array) {
    jsoniq_numpy:zeros($shape, {})
};

declare function jsoniq_numpy:ones($shape as array, $one, $current_dimensionension as integer) {
    if ($current_dimensionension eq size($shape)) then
        let $accumulated_result :=
                      for $j in 1 to $shape[[$current_dimensionension]]
                      return $one
        return [$accumulated_result]
    else
      let $accumulated_result := 
                    let $higher_dimension_result := jsoniq_numpy:ones($shape, $one, $current_dimensionension + 1)
                    for $j in 1 to $shape[[$current_dimensionension]]
                    return $higher_dimension_result
      return [$accumulated_result]  

};
declare type jsoniq_numpy:ones_params as {
    "type": "string=integer"
};
(:
Required parameters are:
- shape (array): contains the size of each dimension of the resulting array. For one dimensional arrays, a single value within is array is expected - e.g., [7] results in a 1 dimensional array with 7 elements with value 1.
Params is an object for optional arguments. These arguments are:
- type (string): the enforced type of the resulting array.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value
:)
declare function jsoniq_numpy:ones($shape as array, $params as object) {
    let $params := validate type jsoniq_numpy:ones_params {$params}
    return {
        if (size($shape) eq 1) then
            let $one := utils:cast-as(1, $params.type)
            let $accumulated_result := for $j in 1 to $shape[[1]]
                        return $one
            return [$accumulated_result]
        else
            let $current_dimensionension := 1
            let $one := utils:cast-as(1, $params.type)
            let $accumulated_result :=
                        let $higher_dimension_result := jsoniq_numpy:ones($shape, $one, $current_dimensionension + 1)
                        for $j in 1 to $shape[[$current_dimensionension]]
                        return $higher_dimension_result
            return [$accumulated_result]  
    }
};

(: Ones method creates an integer array filled with 1 values. :)
declare function jsoniq_numpy:ones($shape as array) {
    jsoniq_numpy:ones($shape, {})
};


declare type jsoniq_numpy:linspace_params as {
    "num": "integer=50",
    "endpoint": "boolean=true",
    "retstep": "boolean=false"
};

declare function jsoniq_numpy:compute_linspace($start as double, $end as double, $params as object, $step_unit as integer) {
    let $range := $end - $start
    let $step := $range div $step_unit
    let $accumulated_result :=
                for $i in 1 to $params.num
                return
                    if ($i eq $params.num) then float($start + ($i - 1) * $step)
                    else $start + ($i - 1) * $step
    return
        if ($params.retstep eq true) then ([$accumulated_result], $step)
        else [$accumulated_result]
};

(:
Return evenly spaced numbers over a specified interval. Returns num evenly spaced samples, calculated over the interval [start, stop]. The endpoint of the interval can optionally be excluded.
Note: we currently only support one-dimensional results.
Required params are:
- start (double) - the start value bounding the generated sequence
- end (double) - the end value bounding the generated sequence
Params is an object for optional arguments. These arguments are:
- num (integer): number of samples to generate. Default is 50.
- endpoint (bool): if true, stop is the last sample. Otherwise it is not included. Default is true.
- retstep (bool): if true, returns the step used which denotes the spacing of values. Default is false.
- dtype UNSUPPORTED: return is always double.
- axis UNSUPPORTED.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value:)
declare function jsoniq_numpy:linspace($start as double, $end as double, $params as object) {
    let $params := validate type jsoniq_numpy:linspace_params {$params}
    return {
        if ($params.num lt 0) then
            error("InvalidFunctionCallErrorCode", "Function expects a num value to be greater than or equal to 0")
        else
            if ($params.endpoint eq true) then
                jsoniq_numpy:compute_linspace($start, $end, $params, $params.num - 1)
            else
                jsoniq_numpy:compute_linspace($start, $end, $params, $params.num)
    }
};

(:
Return evenly spaced numbers over a specified interval. Returns num evenly spaced samples, calculated over the interval [start, stop]. The endpoint of the interval can optionally be excluded.
Note: we currently only support one-dimensional results.
The following returns num=50 elements if no params are specified.
:)
declare function jsoniq_numpy:linspace($start as double, $end as double) {
    jsoniq_numpy:linspace($start, $end, {})
};

declare type jsoniq_numpy:arange_params as {
    "start": "double=0",
    "step": "double=1",
    "type": "string=integer"
};

declare function jsoniq_numpy:arange_negative_range($stop as double, $params as object) {
    if ($stop gt $params.start) then []
    else
        if ($params.start + $params.step lt $stop) then [$params.start]
        else
            let $range := $params.start - $stop
            let $num_values := integer(ceiling($range div abs($params.step)))
            let $accumulated_values_in_range := for $i in 1 to $num_values
                        return $params.start + ($i - 1) * $params.step
            return [$accumulated_values_in_range]
};

declare function jsoniq_numpy:arange_positive_range($stop as double, $params as object) {
    if ($params.start gt $stop) then []
    else
        if ($params.start + $params.step gt $stop or $params.step eq 0) then [$params.start]
        else
            let $range := $stop - $params.start
            let $num_values := integer(ceiling($range div $params.step))
            let $accumulated_values_in_range := for $i in 1 to $num_values
                        return $params.start + ($i - 1) * $params.step
            return [$accumulated_values_in_range]
};
(: 
arange can be called with a combination of parameters:
- arange(stop): Values are generated within the half-open interval [0, stop) (in other words, the interval including start but excluding stop).
- arange(start, stop): Values are generated within the half-open interval [start, stop).
- arange(start, stop, step) Values are generated within the half-open interval [start, stop), with spacing between values given by step.
Required params are:
- stop (double): marks the end of the interval
Params is an object for optional arguments. These arguments are:
- start (double): the start of the interval. Default value is 0.
- step (double): the spacing of output values. Default value is 1.
- dtype (string): type of the returned array. Default is integer.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value
:)
declare function jsoniq_numpy:arange($stop as double, $params as object) {
    let $params := validate type jsoniq_numpy:arange_params {$params}
    return {
        if ($params.step lt 0) then
            jsoniq_numpy:arange_negative_range($stop, $params)
        else
            jsoniq_numpy:arange_positive_range($stop, $params)
    }
};

(:
arange can be called with a combination of parameters:
- arange(stop): Values are generated within the half-open interval [0, stop) (in other words, the interval including start but excluding stop).
For other parameters, use the params argument.
:)
declare function jsoniq_numpy:arange($stop as double) {
    jsoniq_numpy:arange($stop, {})
};

(: Function generates a single random value of type double :)
declare function jsoniq_numpy:random() {
    fn:random()
};

(: Function generates a sequence of random values of type double.
Required params are:
- length (integer): the length of the resulting sequence :)
declare function jsoniq_numpy:random($size as array) {
    if (size($size) eq 0) then []
    else jsoniq_numpy:random_with_dimensions($size, 1)
};

declare function jsoniq_numpy:random_with_dimensions($current_dimensionensions as array, $current_dimension as integer) {
    if ($current_dimension eq size($current_dimensionensions)) then fn:random($current_dimensionensions[[$current_dimension]])
        else
            let $accumulated_result := 
                let $higher_dimension_result := jsoniq_numpy:random_with_dimensions($current_dimensionensions, $current_dimension + 1)
                for $i in 1 to $current_dimensionensions[[$current_dimension]]
                return $higher_dimension_result
            return [$accumulated_result]
};

declare function jsoniq_numpy:random_between_with_dimensions($low as double, $high as double, $type as string, $current_dimensionensions as array, $current_dimension as integer) {
    if ($current_dimension eq size($current_dimensionensions)) then fn:random-between($low, $high, $current_dimensionensions[[$current_dimension]], $type)
    else
        let $accumulated_result := 
            let $higher_dimension_result := jsoniq_numpy:random_between_with_dimensions($low, $high, $type, $current_dimensionensions, $current_dimension + 1)
            for $i in 1 to $current_dimensionensions[[$current_dimension]]
            return $higher_dimension_result
        return [$accumulated_result]
};

declare type jsoniq_numpy:random_uniform_params as {
    "low": "double=0",
    "high": "double=1",
    "size": "array"
};
(: Function generates a random sample from a uniform distribution between a lower and higher limit (not inclusive). Params is an object for optional arguments. These arguments are:
    - low (double): the lower bound for generated objects. Default value is 0.0
    - high (double): the upper bound for generated objects. Default value is 1.0
    - size (array): the size of the resulting array. Each value in the array represents the size for the specific dimension. :)
declare function jsoniq_numpy:random_uniform($params as object) {
    let $params := validate type jsoniq_numpy:random_uniform_params {$params}
    let $low := $params.low
    let $high := $params.high
    let $current_dimensionensions := $params.size
    return if (size($current_dimensionensions) eq 0) then []
           else jsoniq_numpy:random_between_with_dimensions($low, $high, "double", $current_dimensionensions, 1)
};

declare type jsoniq_numpy:random_randint_params as {
    "high": "double=1",
    "size": "array"
};
(: Function generates a random sample from a uniform distribution between a lower and higher limit (not inclusive), but with type int.
Required params are:
    - low (integer): the lower bound for generated objects if high is also present. Otherwise, it determines the upperbound. Without high, it makes the sequence be bounded by [0, low), otherwise it makes it bound by [low, high).
Params is an object for optional arguments. These arguments are:
    - high (integer): the upper bound for generated objects. Default value is 1
    - size (array): the size of the resulting array. Each value in the array represents the size for the specific dimension :)
declare function jsoniq_numpy:random_randint($low as integer, $params as object) {
    let $params := validate type jsoniq_numpy:random_randint_params {$params}
    let $high := $params.high
    let $current_dimensionensions := $params.size
    return if (size($current_dimensionensions) eq 0) then []
           else jsoniq_numpy:random_between_with_dimensions($low, $high, "integer", $current_dimensionensions, 1)
};

declare type jsoniq_numpy:logspace_params as {
    "num": "integer=50",
    "endpoint": "boolean=true"
};

declare function jsoniq_numpy:compute_logspace($start as double, $end as double, $params as object) {
    let $base := 10
    let $linspace_vals := jsoniq_numpy:linspace($start, $end, {"num": $params.num, "endpoint": $params.endpoint, "retstep": false})
    let $res :=
                for $i in 1 to $params.num
                return float(pow($base, $linspace_vals[[$i]]))
    return [$res]
};
(: Generate evenly spaced numbers on a log scale in base 10.
Required parameters are:
    - start (double): 10 ** start is the starting value of the sequence.
    - end (double): 10 ** end is the end value of the sequence.
Params is an object for optional arguments. These arguments are:
    - num (integer): number of samples to generate. Default is 50.
    - endpoint (bool): if true, stop is the last sample. Otherwise it is not included. Default is true.
    - base UNSUPPORTED: base 10 is always  used.
    - dtype UNSUPPORTED: return is always double.
    - axis UNSUPPORTED.:)
declare function jsoniq_numpy:logspace($start as double, $end as double, $params as object) {
    let $params := validate type jsoniq_numpy:logspace_params {$params}
    return {
        if ($params.num lt 0) then
            error("InvalidFunctionCallErrorCode", "Function expects a num value to be greater than or equal to 0")
        else
            jsoniq_numpy:compute_logspace($start, $end, $params)
    }
};

(:
Generate evenly spaced numbers on a log scale in base 10.
Required parameters are:
    - start (double): 10 ** start is the starting value of the sequence.
    - end (double): 10 ** end is the end value of the sequence.
The result is a sequence of 50 elements where the endpoint is also included. For refined versions, use the params argument.
:)
declare function jsoniq_numpy:logspace($start as double, $end as double) {
    jsoniq_numpy:logspace($start, $end, {})
};

declare function jsoniq_numpy:compute_full_on_sub_dimensions($current_dimensionensions as array, $fill_value, $current_dimension as integer) {
    let $current_dimensionension := size($current_dimensionensions)
    let $accumulated_result :=
        for $j in 1 to $current_dimensionensions[[$current_dimension]]
        return
            if ($current_dimension eq $current_dimensionension) then $fill_value
            else jsoniq_numpy:compute_full_on_sub_dimensions($current_dimensionensions, $fill_value, $current_dimension + 1)
    return [$accumulated_result]
};

declare type jsoniq_numpy:full_params as {
    "type": "string=integer"
};
(: The full method returns a new array of given shape and type, filled with fill_value.
Required arguments:
- shape (array): the dimension of the new array
- fill_value (atomic): the fill value
Optional arguments include:
- type (string): the type of the resulting array values. The default value is integer.
Unsupported arguments:
- order: ordering is implicitly done row-wise (C format)
:)
declare function jsoniq_numpy:full($shape as array, $fill_value, $params as object) {
    let $params := validate type jsoniq_numpy:full_params {$params}
    return {
            let $num_dimensions := size($shape)
            let $current_dimension := 1
            let $fill := utils:cast-as($fill_value, $params.type)
            let $sub_dimension_result :=
                        for $j in 1 to $shape[[$current_dimension]]
                        return
                            if ($num_dimensions eq 1) then
                                $fill
                            else
                                jsoniq_numpy:compute_full_on_sub_dimensions($shape, $fill, $current_dimension + 1)
            return [$sub_dimension_result]  
    }
};

(:
The full method returns a new array of given shape and type, filled with fill_value.
Required arguments:
- shape (array): the dimension of the new array
- fill_value (atomic): the fill value
The method returns an integer array by default. To change this, use the params argument.
:)
declare function jsoniq_numpy:full($shape as array, $fill_value) {
    jsoniq_numpy:full($shape, $fill_value, {})
};

declare type jsoniq_numpy:identity_params as {
    "type": "string=integer"
};
(:
Return the identity array. The identity array is a square array with ones on the main diagonal.
Required arguments:
- n (integer): the number of rows (and columns) in N x N output.
Optional arguments include:
- type (string): the type of the resulting array values
:)
declare function jsoniq_numpy:identity($n as integer, $params as object) {
    if ($n le 1) then []
    else
        let $params := validate type jsoniq_numpy:identity_params {$params}
        return {
            let $fill_one := utils:cast-as(1, $params.type)
            let $fill_zero := utils:cast-as(0, $params.type)
            let $accumulated_matrix := 
                for $row in 1 to $n
                let $accumulated_row :=
                    for $column in 1 to $n
                    return  if ($row eq $column) then $fill_one
                            else $fill_zero
                return [$accumulated_row]
            return [$accumulated_matrix]
        }
};

declare function jsoniq_numpy:identity($n as integer) {
    jsoniq_numpy:identity($n, {})
};

(: Binary search method. It performs binary search over the given arr parameter looking for the value of searched_element for it. The current behavior is to return the first matching position for a given searched_element even if more values of it are present.
Required params are:
- arr (array): the array to search for searched_element
- searched_element (any): the value to look for in the array arr
The returned value is an integer such that:
- if searched_element is present, the index where it first occurs is returned.
- if searched_element is not found:
    - if searched_element is smaller than all values, index 0 is returned.
    - if searched_element is greater than all values, index size(arr) + 1 is returned.:)
declare %an:sequential function jsoniq_numpy:binsearch($arr as array, $searched_element) {
    variable $low := 1;
    variable $high := size($arr) + 1;
    while ($low lt $high) {
        variable $mid_index := integer($low + ($high - $low) div 2);
        if ($arr[[$mid_index]] eq $searched_element) then exit returning $mid_index;
        else
            if ($searched_element le $arr[[$mid_index]]) then $high := $mid_index;
            else $low := $mid_index + 1;
    }
    exit returning if ($low eq 1) then 0 else $low;
};

(: returns i s.t. arr[i - 1] <= x < arr[i] :)
declare %an:sequential function jsoniq_numpy:searchsorted_left($arr as array, $searched_element) {
    variable $low := 1;
    variable $high := size($arr) + 1;
    while ($low lt $high) {
        variable $mid_index := integer($low + ($high - $low) div 2);
        if ($searched_element ge $arr[[$mid_index]]) then $low := $mid_index + 1;
        else $high := $mid_index;
    }
    exit returning if ($low eq 1) then 0 else $low;
};

(: returns i s.t. arr[i - 1] < x <= arr[i] :)
declare %an:sequential function jsoniq_numpy:searchsorted_right($arr as array, $searched_element) {
    variable $low := 1;
    variable $high := size($arr) + 1;
    while ($low lt $high) {
        variable $mid_index := integer($low + ($high - $low) div 2);
        if ($searched_element le $arr[[$mid_index]]) then $high := $mid_index;
        else $low := $mid_index + 1;
    }
    exit returning if ($low eq 1) then 0 else $low;
};


declare function jsoniq_numpy:digitize_monotonically_increasing_left($x as array, $bins as array) {
    let $bin_indexes := for $i in 1 to size($x)
                        return jsoniq_numpy:searchsorted_left($bins, $x[[$i]])
    return [$bin_indexes]
};

declare function jsoniq_numpy:digitize_monotonically_increasing_right($x as array, $bins as array) {
    let $bin_indexes :=
        for $i in 1 to size($x)
        return jsoniq_numpy:searchsorted_right($bins, $x[[$i]])
    return [$bin_indexes]
};

declare function jsoniq_numpy:digitize_in_reverse($x as array, $bins as array, $right as boolean) {
    let $bins_rev := [fn:reverse($bins[])]
    let $bin_indexes :=
        for $i in 1 to size($x)
        let $searchsorted_res := 
                if ($right eq false) then jsoniq_numpy:searchsorted_left($bins_rev, $x[[$i]])
                else jsoniq_numpy:searchsorted_right($bins_rev, $x[[$i]])
        let $bin_index := jsoniq_numpy:compute_index($searchsorted_res, size($bins))
        return $bin_index
    return [$bin_indexes]
};
declare type jsoniq_numpy:digitize_params as {
    "right": "boolean=false"
};
(:
Return the indices of the bins to which each value in input array belongs.
Required arguments:
- x (array): input array to be binned (currently only 1 dimension is supported)
- bins (array): one dimensional monotonic, array
Optional arguments include:
- right (boolean): indicates whether the intervals include the right or the left bin edge.
Values outside of the bins bounds return position 1 or size(bins) + 1 according to their relation.
:)
declare function jsoniq_numpy:digitize($x as array, $bins as array, $params as object) {
    let $monotonic := jsoniq_numpy:monotonic($bins)
    let $params := validate type jsoniq_numpy:digitize_params {$params}
    return {
        if ($monotonic eq 0) then
            fn:error("Bins must be monotonically increasing or decreasing!")
        else
            if ($monotonic eq 1) then
                if ($params.right eq false) then jsoniq_numpy:digitize_monotonically_increasing_left($x, $bins)
                else
                    jsoniq_numpy:digitize_monotonically_increasing_right($x, $bins)
            else
                jsoniq_numpy:digitize_in_reverse($x, $bins, $params.right)
    }
};

declare function jsoniq_numpy:digitize($x as array, $bins as array) {
    jsoniq_numpy:digitize($x, $bins, {})
};

declare function jsoniq_numpy:compute_index($result as integer, $size as integer) {
    (: the value is out of the bounds on the left => size + 1:)
    if ($result eq 0) then $size + 1
    else
        (: the value is out of bounds on the right => 1 :)
        if ($result eq ($size + 1)) then 1
        else
            $size - $result + 2
};

declare function jsoniq_numpy:non_decreasing($arr as array) {
    variable $i := 1;
    while ($i lt (size($arr) - 1)) {
        if ($arr[[$i]] gt $arr[[$i + 1]]) then {
            exit returning 0;
        } else {
            $i := $i + 1;
            continue loop;
        }
    }
    1
};

declare function jsoniq_numpy:non_increasing($arr as array) {
    variable $i := 1;
    while ($i lt (size($arr) - 1)) {
        if ($arr[[$i]] lt $arr[[$i + 1]]) then {
            exit returning 0;
        } else {
            $i := $i + 1;
            continue loop;
        }
    }
    -1
};

declare function jsoniq_numpy:monotonic($arr as array) {
    if (jsoniq_numpy:non_decreasing($arr) eq 0) then
        jsoniq_numpy:non_increasing($arr)
    else
        1
};

declare function jsoniq_numpy:product_of_all_values($arr as array) {
    variable $product := 1;
    variable $i := 1;
    while ($i lt size($arr)) {
        $product := $product * $arr[[$i]];
        $i := $i + 1;
    }
    $product
};


declare function jsoniq_numpy:reshape_sub_dimension($arr, $current_dimensionensions as array, $current_dimension as integer) {
    if ($current_dimension gt size($current_dimensionensions)) then
        $arr
    else
        if ($current_dimensionensions[[$current_dimension]] eq 1) then
            [jsoniq_numpy:reshape_sub_dimension($arr, $current_dimensionensions, $current_dimension + 1)]
        else
            let $sub_dimension_result :=
                let $size_arr := count($arr)
                let $size_subarr := $size_arr div $current_dimensionensions[[$current_dimension]]
                for $j in 0 to ($current_dimensionensions[[$current_dimension]] - 1)
                return jsoniq_numpy:reshape_sub_dimension(subsequence($arr, $j * $size_subarr + 1, $size_subarr), $current_dimensionensions, $current_dimension + 1)
            return [$sub_dimension_result]
};
(: Gives a new shape to an array. The shape argument should have the product of its dimension sizes equal to the number of elements found in arr.
- arr (array): the array to reshape
- shape (array): the dimension sizes to resize to. :)
declare function jsoniq_numpy:reshape($arr as array, $shape as array) {
    let $flattened_arr := flatten($arr)
    let $number_of_elements := count($flattened_arr)
    let $product_of_all_values := jsoniq_numpy:product_of_all_values($shape)
    return
        if (($number_of_elements mod $product_of_all_values) eq 0) then
            jsoniq_numpy:reshape_sub_dimension($flattened_arr, $shape, 1)
        else
            error("InvalidFunctionCallErrorCode", "Invalid call to reshape. The shape array must result in a size equivalent to the size of the array.")

};

(: Helper method for argwhere :)
declare function jsoniq_numpy:argwhere($current_dimension, $positions_at_each_dimension as array) {
    typeswitch($current_dimension) 
        case array return {
            for $i in 1 to size($current_dimension)
            let $sub_dimension := $current_dimension[[$i]]
            let $positions_at_each_dimension := [$positions_at_each_dimension[], $i]
            return jsoniq_numpy:argwhere($sub_dimension, $positions_at_each_dimension)
        }
        case integer return if ($current_dimension gt 0) then $positions_at_each_dimension
                            else ()
        default return ()
};

(: Returns the indexes of non-zero elements with respect to the dimension. The result is a [N, nr_dim] array, where N is the number of non-zero elements and nr_dim is the number of dimensions.
Required params are:
- arr (array): the array to look into :)
declare function jsoniq_numpy:argwhere($arr as array) {
    [jsoniq_numpy:argwhere($arr, [])]
};

(: Axis based methods :)

(: Method expects array to be of the same size. :)
declare function jsoniq_numpy:compute_min_values_of_arrays($array1, $array2) {
    typeswitch($array1)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:compute_min_values_of_arrays($array1[[$i]], $array2[[$i]])
                          return [$accumulated_sub_dimensions]
        default return if ($array1 lt $array2) then $array1
                            else $array2
};
(: Helper method to compute minimum of two arrays. The minimum is computed per index, so the minimum value for a specific index is taken. If the minimum is greater than initial, initial is returned instead as the minimum. :)
declare function jsoniq_numpy:compute_min_values_of_arrays($array1, $array2, $initial) {
    typeswitch($array1)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:compute_min_values_of_arrays($array1[[$i]], $array2[[$i]], $initial)
                          return [$accumulated_sub_dimensions]
        default return if ($array1 lt $array2) then 
                            if ($initial lt $array1) then $initial
                            else $array1
                        else
                            if ($initial lt $array2) then $initial
                            else $array2
};

(: Helper method to compute the minimum on the right axis. :)
declare function jsoniq_numpy:compute_min_along_axis($array as array, $axis as integer, $current_dimension as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($current_dimension eq $max_dim) then exit returning min(flatten($array));
        else {
            if ($current_dimension eq $axis) then {
                (: Take the first array as minimum :)
                variable $mini := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $mini := jsoniq_numpy:compute_min_values_of_arrays($mini, $array[[$i]]);
                    $i := $i + 1;
                }
                exit returning $mini;
            } else {
                let $accumulated_sub_dimensions :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:compute_min_along_axis($array[[$i]], $axis, $current_dimension + 1, $max_dim)
                return exit returning [$accumulated_sub_dimensions];
            }
        }
};

(: Helper method to compute the minimum on the right axis and using initial. :)
declare function jsoniq_numpy:compute_min_along_axis($array as array, $axis as integer, $current_dimension as integer, $max_dim as integer, $initial as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($current_dimension eq $max_dim) then exit returning min((flatten($array), $initial));
        else {
            if ($current_dimension eq $axis) then {
                (: Take the first array as minimum :)
                variable $mini := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $mini := jsoniq_numpy:compute_min_values_of_arrays($mini, $array[[$i]], $initial);
                    $i := $i + 1;
                }
                exit returning $mini;
            } else {
                let $accumulated_sub_dimensions :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:compute_min_along_axis($array[[$i]], $axis, $current_dimension + 1, $max_dim, $initial)
                return exit returning [$accumulated_sub_dimensions];
            }
        }
};

declare function jsoniq_numpy:compute_min_along_axis_wrapper($array as array, $axis as integer) {
    if ($axis eq -1) then min(flatten($array[]))
    else 
        jsoniq_numpy:compute_min_along_axis($array, $axis, 0, size(utils:shape($array)) - 1)
};

declare function jsoniq_numpy:compute_min_along_axis_wrapper($array as array, $axis as integer, $initial as integer) {
    if ($axis eq -1) then min((flatten($array[]), $initial))
    else
        jsoniq_numpy:compute_min_along_axis($array, $axis, 0, size(utils:shape($array)) - 1, $initial)
        
};

declare type jsoniq_numpy:min_params as {
    "axis": "integer=-1",
    "initial": "integer=-2147483648"
};

(: Min returns the minimum value of an array along an axis. Without an axis, it returns the minimum value in the array.
Required params are:
- array (array): The array to look into
Params is an object for optional arguments. These arguments are:
- axis (integer): The axis along which to compute the minimum. Only values greater than 0 are accepted.
- initial (integer): The maximum value returned as output. If a minimum value is greater than initial, initial is returned. We reserve the value -2147483648 for the default, unset value of initial.:)
declare function jsoniq_numpy:min($array as array, $params as object) {
    let $params := validate type jsoniq_numpy:min_params {$params}
    return {
        if ($params.initial eq -2147483648) then
            jsoniq_numpy:compute_min_along_axis_wrapper($array, $params.axis)
        else
            jsoniq_numpy:compute_min_along_axis_wrapper($array, $params.axis, $params.initial)
    }
};

declare function jsoniq_numpy:min($array as array) {
    jsoniq_numpy:min($array, {})
};

(: MAX :)

(: Helper method to compute maximum of two arrays. The maximum is computed per index, so the maximum value for a specific index is taken. :)
declare function jsoniq_numpy:compute_max_values_of_arrays($array1, $array2) {
    typeswitch($array1)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:compute_max_values_of_arrays($array1[[$i]], $array2[[$i]])
                          return [$accumulated_sub_dimensions]
        default return if ($array1 gt $array2) then $array1
                            else $array2
};
(: Helper method to compute maximum of two arrays. The maximum is computed per index, so the maximum value for a specific index is taken. If the maximum is greater than initial, initial is returned instead as the maximum. :)
declare function jsoniq_numpy:compute_max_values_of_arrays($array1, $array2, $initial) {
    typeswitch($array1)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:compute_max_values_of_arrays($array1[[$i]], $array2[[$i]], $initial)
                          return [$accumulated_sub_dimensions]
        default return if ($array1 gt $array2) then 
                            if ($initial gt $array1) then $initial
                            else $array1
                        else
                            if ($initial gt $array2) then $initial
                            else $array2
};

(: Helper method to compute the maximum on the right axis. :)
declare function jsoniq_numpy:compute_max_along_axis($array as array, $axis as integer, $current_dimension as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($current_dimension eq $max_dim) then exit returning max(flatten($array));
        else {
            if ($current_dimension eq $axis) then {
                (: Take the first array as maximum :)
                variable $max_arr := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $max_arr := jsoniq_numpy:compute_max_values_of_arrays($max_arr, $array[[$i]]);
                    $i := $i + 1;
                }
                exit returning $max_arr;
            } else {
                let $accumulated_sub_dimensions :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:compute_max_along_axis($array[[$i]], $axis, $current_dimension + 1, $max_dim)
                return exit returning [$accumulated_sub_dimensions];
            }
        }
};

(: Helper method to compute the maximum on the right axis and using initial. :)
declare function jsoniq_numpy:compute_max_along_axis($array as array, $axis as integer, $current_dimension as integer, $max_dim as integer, $initial as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($current_dimension eq $max_dim) then exit returning max((flatten($array), $initial));
        else {
            if ($current_dimension eq $axis) then {
                (: Take the first array as maximum :)
                variable $max_arr := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $max_arr := jsoniq_numpy:compute_max_values_of_arrays($max_arr, $array[[$i]], $initial);
                    $i := $i + 1;
                }
                exit returning $max_arr;
            } else {
                let $accumulated_sub_dimensions :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:compute_max_along_axis($array[[$i]], $axis, $current_dimension + 1, $max_dim, $initial)
                return exit returning [$accumulated_sub_dimensions];
            }
        }
};

(: Helper method that invokes maximum with axis only :)
declare function jsoniq_numpy:compute_max_along_axis_wrapper($array as array, $axis as integer) {
    if ($axis eq -1) then max(flatten($array[]))
    else 
        jsoniq_numpy:compute_max_along_axis($array, $axis, 0, size(utils:shape($array)) - 1)
};

(: Helper method that invokes maximum with axis and initial :)
declare function jsoniq_numpy:compute_max_along_axis_wrapper($array as array, $axis as integer, $initial as integer) {
    if ($axis eq -1) then max((flatten($array[]), $initial))
    else
        jsoniq_numpy:compute_max_along_axis($array, $axis, 0, size(utils:shape($array)) - 1, $initial)
        
};

declare type jsoniq_numpy:max_params as {
    "axis": "integer=-1",
    "initial": "integer=2147483647"
};

(: max returns the maximum value of an array along an axis. Without an axis, it returns the maximum value in the array.
Required params are:
- array (array): The array to look into
Params is an object for optional arguments. These arguments are:
- axis (integer): The axis along which to compute the maximum. Only values greater than 0 are accepted.
- initial (integer): The maximum value returned as output. If a maximum value is smaller than initial, initial is returned. We reserve the value 2147483647 for the default, unset value of initial.:)
declare function jsoniq_numpy:max($array as array, $params as object) {
    let $params := validate type jsoniq_numpy:max_params {$params}
    return {
        if ($params.initial eq 2147483647) then
            jsoniq_numpy:compute_max_along_axis_wrapper($array, $params.axis)
        else
            jsoniq_numpy:compute_max_along_axis_wrapper($array, $params.axis, $params.initial)
    }
};

declare function jsoniq_numpy:max($array as array) {
    jsoniq_numpy:max($array, {})
};


(: MEAN :)

declare function jsoniq_numpy:sum_arrays($array1, $array2) {
    typeswitch($array1)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:sum_arrays($array1[[$i]], $array2[[$i]])
                          return [$accumulated_sub_dimensions]
        default return $array1 + $array2
};

(: Helper method to compute average on an array given the number of values. :)
declare function jsoniq_numpy:compute_mean_of_array($array1, $count) {
    typeswitch($array1)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:compute_mean_of_array($array1[[$i]], $count)
                          return [$accumulated_sub_dimensions]
        default return $array1 div $count
};

(: Helper method to compute the mean on the right axis. :)
declare function jsoniq_numpy:compute_mean_along_axis($array as array, $axis as integer, $current_dimension as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($current_dimension eq $max_dim) then exit returning avg(flatten($array));
        else {
            if ($current_dimension eq $axis) then {
                (: Take the first array as sum :)
                variable $mean := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $mean := jsoniq_numpy:sum_arrays($mean, $array[[$i]]);
                    $i := $i + 1;
                }
                $mean := jsoniq_numpy:compute_mean_of_array($mean, size($array));
                exit returning $mean;
            } else {
                let $accumulated_sub_dimensions :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:compute_mean_along_axis($array[[$i]], $axis, $current_dimension + 1, $max_dim)
                return exit returning [$accumulated_sub_dimensions];
            }
        }
};

(: Helper method that invokes maximum with axis only :)
declare function jsoniq_numpy:compute_mean_along_axis_wrapper($array as array, $axis as integer) {
    if ($axis eq -1) then avg(flatten($array))
    else 
        jsoniq_numpy:compute_mean_along_axis($array, $axis, 0, size(utils:shape($array)) - 1)
};

declare type jsoniq_numpy:mean_params as {
    "axis": "integer=-1"
};

(: mean returns the mean (average) of an array along an axis. Without an axis, it returns the mean of the array.
Required params are:
- array (array): The array to look into
Params is an object for optional arguments. These arguments are:
- axis (integer): The axis along which to compute the mean. Only values greater than 0 are accepted.:)
declare function jsoniq_numpy:mean($array as array, $params as object) {
    let $params := validate type jsoniq_numpy:mean_params {$params}
    return jsoniq_numpy:compute_mean_along_axis_wrapper($array, $params.axis)
};

declare function jsoniq_numpy:mean($array as array) {
    jsoniq_numpy:mean($array, {})
};

(: Returns the array in absolute value.
Required params are:
- array (array): the array to perform absolute value on.
Other numpy equivalent numpy params are unsupported.
:)
declare function jsoniq_numpy:absolute($array) {
    typeswitch($array)
        case array return if (size($array) eq 0) then []
                          else
                                let $accumulated_sub_dimensions :=
                                    for $i in 1 to size($array)
                                    return jsoniq_numpy:absolute($array[[$i]])
                                return [$accumulated_sub_dimensions]
        default return abs($array)
};

declare function jsoniq_numpy:sort($array as array, $low as integer, $high as integer) as array {
    if ($low ge $high or $low lt 1) then $array
    else {
        variable $partition_res := jsoniq_numpy:partition($array, $low, $high);
        $array := [flatten(subsequence($partition_res, 1, 1))];
        variable $pivot := subsequence($partition_res, 2, 2);
        $array := jsoniq_numpy:sort($array, $low, $pivot - 1);
        $array := jsoniq_numpy:sort($array, $pivot + 1, $high);
        $array
    }
};

declare function jsoniq_numpy:partition($array as array, $low as integer, $high as integer) {
    variable $pivot := jsoniq_numpy:random_randint($low, {"high": $high + 1, "size": [1]})[[1]];
    variable $end := $array[[$high]];
    replace json value of $array[[$high]] with $array[[$pivot]];
    replace json value of $array[[$pivot]] with $end;

    variable $i := $low;
    for $j in $low to $high - 1
    return {
        if ($array[[$j]] le $array[[$high]]) then {
            variable $aux := $array[[$i]];
            replace json value of $array[[$i]] with $array[[$j]];
            replace json value of $array[[$j]] with $aux;
            $i := $i + 1;
        } else ();
    }
    variable $aux := $array[[$i]];
    replace json value of $array[[$i]] with $array[[$high]];
    replace json value of $array[[$high]] with $aux;
    exit returning ($array, $i);
};

declare function jsoniq_numpy:sort($array) {
    jsoniq_numpy:sort([flatten($array)], 1, size($array))
};

(: Count non-zero :)

declare function jsoniq_numpy:count_nonzero_values($array1) {
    typeswitch($array1)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:count_nonzero_values($array1[[$i]])
                          return [$accumulated_sub_dimensions]
        case string return if ($array1 eq "") then 0
                           else 1
        case boolean return if ($array1) then 1
                            else 0
        default return if ($array1 ne 0) then 1
                       else 0
};

declare function jsoniq_numpy:count_nonzero_values_along_axis($array as array, $axis as integer, $current_dimension as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($current_dimension eq $max_dim) then exit returning sum(flatten(jsoniq_numpy:count_nonzero_values($array)));
        else {
            if ($current_dimension eq $axis) then {
                variable $count_nonzero_accumulated := jsoniq_numpy:count_nonzero_values($array[[1]]);
                variable $i := 2;
                while ($i le size($array)) {
                    variable $curr_count := jsoniq_numpy:count_nonzero_values($array[[$i]]); 
                    $count_nonzero_accumulated := jsoniq_numpy:sum_arrays($curr_count, $count_nonzero_accumulated);
                    $i := $i + 1;
                }
                exit returning $count_nonzero_accumulated;
            } else {
                let $accumulated_sub_dimensions :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:count_nonzero_values_along_axis($array[[$i]], $axis, $current_dimension + 1, $max_dim)
                return exit returning [$accumulated_sub_dimensions];
            }
        }
};

declare function jsoniq_numpy:count_nonzero_along_axis_wrapper($array as array, $axis as integer) {
    if ($axis eq -1) then sum(flatten(jsoniq_numpy:count_nonzero_values($array)))
    else 
        jsoniq_numpy:count_nonzero_values_along_axis($array, $axis, 0, size(utils:shape($array)) - 1)
};

declare type jsoniq_numpy:count_nonzero_params as {
    "axis": "integer=-1"
};

(: count_nonzero returns the the number of non-zero elements in the array. Non-zero is interpreted as being any value greater than 0, has a boolean value of True or is a non-empty string.
Required params are:
- array (array): The array to look into
Params is an object for optional arguments. These arguments are:
- axis (integer): The axis along which to compute the count on. Only values greater than 0 are accepted.:)
declare function jsoniq_numpy:count_nonzero($array as array, $params as object) {
    let $params := validate type jsoniq_numpy:count_nonzero_params {$params}
    return jsoniq_numpy:count_nonzero_along_axis_wrapper($array, $params.axis)
};

declare function jsoniq_numpy:count_nonzero($array as array) {
    jsoniq_numpy:count_nonzero($array, {})
};

(: Unique :)

(: unique returns a 1 dimensional array containing the unique elements of the array, sorted ascendingly. Currently, we only support flattening of the unique array as axis behavior is more intricate for jsoniq to support.
Required params are:
- array (array): the array to look for unique values.:)
declare function jsoniq_numpy:unique($array as array) {
    jsoniq_numpy:sort([distinct-values(flatten($array))])
};

(: Median :)
(: Helper method to merge two arrays. Destination may have higher dimension than source, thus we may append to it if it is already an array type. :)
declare function jsoniq_numpy:merge_arrays($source, $dest) {
    typeswitch($source)
        case array return let $accumulated_sub_dimensions :=
                                for $i in 1 to size($source)
                                return jsoniq_numpy:merge_arrays($source[[$i]], $dest[[$i]])
                          return [$accumulated_sub_dimensions]
        default return typeswitch($dest)
                            case array return [$source, flatten($dest)]
                            default return [$source, $dest]
};

declare function jsoniq_numpy:compute_median($array) {
    if (size(utils:shape($array)) gt 1) then
        let $accumulated_sub_dimensions :=
            for $i in 1 to size($array)
            return jsoniq_numpy:compute_median($array[[$i]])
        return [$accumulated_sub_dimensions]
    else
        jsoniq_numpy:compute_median_without_axis($array)
};

declare function jsoniq_numpy:compute_median_along_axis($array as array, $axis as integer, $current_dimension as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($current_dimension eq $max_dim) then exit returning jsoniq_numpy:compute_median_without_axis($array);
        else {
            if ($current_dimension eq $axis) then {
                variable $accumulated_subarrays := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $accumulated_subarrays := jsoniq_numpy:merge_arrays($array[[$i]], $accumulated_subarrays);
                    $i := $i + 1;
                }
                if ($i eq 2) then
                    (: no merging was done, no median is needed for this axis :)
                    exit returning $accumulated_subarrays;
                else
                    exit returning jsoniq_numpy:compute_median($accumulated_subarrays);
            } else {
                let $accumulated_sub_dimensions :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:compute_median_along_axis($array[[$i]], $axis, $current_dimension + 1, $max_dim)
                return exit returning [$accumulated_sub_dimensions];
            }
        }
};

declare function jsoniq_numpy:compute_median_without_axis($array as array) {
    let $sorted_arr := jsoniq_numpy:sort($array)
    let $middle_index := size($sorted_arr) div 2
    return 
        if (size($sorted_arr) mod 2 eq 0) then
            ($sorted_arr[[$middle_index]] + $sorted_arr[[$middle_index + 1]]) div 2
        else
            $sorted_arr[[$middle_index + 1]]
};

declare function jsoniq_numpy:median_on_axis($array as array, $axis as integer) {
    if ($axis eq -1) then jsoniq_numpy:compute_median_without_axis([flatten($array)])
    else 
        jsoniq_numpy:compute_median_along_axis($array, $axis, 0, size(utils:shape($array)) - 1)
};

declare type jsoniq_numpy:median_params as {
    "axis": "integer=-1"
};

(: median computes the median along the specified axis. Given an array V of length N, the median of V is the middle value of a sorted copy of V, V_sorted - i e., V_sorted[(N-1)/2], when N is odd, and the average of the two middle values of V_sorted when N is even.
Required params are:
- array (array): The array to look into
Params is an object for optional arguments. These arguments are:
- axis (integer): The axis along which to compute median on. Only values greater than 0 are accepted.:)
declare function jsoniq_numpy:median($array as array, $params as object) {
    let $params := validate type jsoniq_numpy:median_params {$params}
    return jsoniq_numpy:median_on_axis($array, $params.axis)
};

declare function jsoniq_numpy:median($array as array) {
    jsoniq_numpy:median($array, {})
};