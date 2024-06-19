(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_numpy = "jsoniq_numpy.jq";
import module namespace utils = "jsoniq_utils.jq";
(: Helper function for zeros :)
declare function jsoniq_numpy:zeros($shape as array, $zero, $i as integer) {
    if ($i eq size($shape)) then
      let $join :=
                      for $j in 1 to $shape[[$i]]
                      return $zero
            return [$join]
    else
      let $join := 
                    let $partial_res := jsoniq_numpy:zeros($shape, $zero, $i + 1)
                    for $j in 1 to $shape[[$i]]
                    return $partial_res
      return [$join] 
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
            let $join := for $j in 1 to $shape[[1]]
                         return $zero
            return [$join]
        else
            let $i := 1
            let $zero := utils:cast-as(0, $params.type)
            let $join :=
                        let $partial_res := jsoniq_numpy:zeros($shape, $zero, $i + 1)
                        for $j in 1 to $shape[[$i]]
                        return $partial_res
            return [$join]
    }
};

(: Zeros method creates an integer array filled with 0 values. :)
declare function jsoniq_numpy:zeros($shape as array) {
    jsoniq_numpy:zeros($shape, {})
};

(: Helper function for ones :)
declare function jsoniq_numpy:ones($shape as array, $one, $i as integer) {
    if ($i eq size($shape)) then
      let $join :=
                      for $j in 1 to $shape[[$i]]
                      return $one
            return [$join]
    else
      let $join := 
                    let $partial_res := jsoniq_numpy:ones($shape, $one, $i + 1)
                    for $j in 1 to $shape[[$i]]
                    return $partial_res
      return [$join]  

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
            let $join := for $j in 1 to $shape[[1]]
                        return $one
            return [$join]
        else
            let $i := 1
            let $one := utils:cast-as(1, $params.type)
            let $join :=
                        let $partial_res := jsoniq_numpy:ones($shape, $one, $i + 1)
                        for $j in 1 to $shape[[$i]]
                        return $partial_res
            return [$join]  
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
        if ($params.endpoint eq true) then
            if ($params.num lt 0) then
                (: error case :)
                []
            else
                let $num := $params.num
                let $range := $end - $start
                let $step := $range div ($num - 1)
                let $res :=
                            for $i in 1 to $num
                            return if ($i eq $num) then float($start + ($i - 1) * $step)
                                else $start + ($i - 1) * $step
                return
                    if ($params.retstep eq true) then ([$res], $step)
                    else [$res]
        else
            if ($params.num lt 0) then
                (: error case :)
                []
            else
                let $num := $params.num
                let $range := $end - $start
                let $step := $range div $num
                let $res :=
                            for $i in 1 to $num
                            return if ($i eq $num) then float($start + ($i - 1) * $step)
                                else $start + ($i - 1) * $step
                return
                    if ($params.retstep eq true) then ([$res], $step)
                    else [$res]
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
        if ($params.start gt $stop) then []
        else
            if ($params.start + $params.step gt $stop or $params.step eq 0) then [$params.start]
            else
                let $num_values := integer(ceiling(($stop - $params.start) div $params.step))
                let $res := for $i in 1 to $num_values
                            return $params.start + ($i - 1) * $params.step
                return [$res]
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
declare function jsoniq_numpy:random($length as integer) {
    fn:random($length)
};

(:  TODO: any dimension array :)
declare function jsoniq_numpy:random_size($size as array) {

};

declare type jsoniq_numpy:random_uniform_params as {
    "low": "double=0",
    "high": "double=1",
    "size": "integer=10"
};
(: Function generates a random sample from a uniform distribution between a lower and higher limit (not inclusive). Params is an object for optional arguments. These arguments are:
    - low (double): the lower bound for generated objects. Default value is 0.0
    - high (double): the upper bound for generated objects. Default value is 1.0
    - size (integer): the size of the resulting array. Default is 10.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value :)
declare function jsoniq_numpy:random_uniform($params as object) {
    let $params := validate type jsoniq_numpy:random_uniform_params {$params}
    let $low := $params.low
    let $high := $params.high
    let $size := $params.size
    return fn:random-between($low, $high, $size, "double")
};

declare type jsoniq_numpy:random_randint_params as {
    "high": "double=1",
    "size": "integer=10"
};
(: Function generates a random sample from a uniform distribution between a lower and higher limit (not inclusive), but with type int.
Required params are:
    - low (integer): the lower bound for generated objects if high is also present. Otherwise, it determines the upperbound. Without high, it makes the sequence be bounded by [0, low), otherwise it makes it bound by [low, high).
Params is an object for optional arguments. These arguments are:
    - high (integer): the upper bound for generated objects. Default value is 1
    - size (integer): the size of the resulting array. Default is 10
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value :)
declare function jsoniq_numpy:random_randint($low as integer, $params as object) {
    let $params := validate type jsoniq_numpy:random_randint_params {$params}
    let $high := $params.high
    let $size := $params.size
    return fn:random-between($low, $high, $size, "integer")
};

declare type jsoniq_numpy:logspace_params as {
    "num": "integer=50",
    "endpoint": "boolean=true"
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
        if ($params.endpoint eq true) then
            if ($params.num lt 0) then
                (: error case :)
                []
            else
                let $num := $params.num
                let $base := 10
                let $linspace_vals := jsoniq_numpy:linspace($start, $end, {"num": $num, "endpoint": true, "retstep": false})
                let $res :=
                            for $i in 1 to $num
                            return pow($base, $linspace_vals[[$i]])
                return [$res]
        else
            if ($params.num lt 0) then
                (: error case :)
                []
            else
                let $num := $params.num
                let $base := 10
                let $linspace_vals := jsoniq_numpy:linspace($start, $end, {"num": $num, "endpoint": false, "retstep": false})
                let $res :=
                            for $i in 1 to $num
                            return pow($base, $linspace_vals[[$i]])
                return [$res]
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

(: Helper method for the full function :)
declare function jsoniq_numpy:full($shape as array, $fill_value, $i as integer, $unused as integer) {
    if ($i eq size($shape)) then
        let $join := for $j in 1 to $shape[[$i]]
                     return $fill_value
        return [$join]
    else
        let $join := 
                    for $j in 1 to $shape[[$i]]
                    return jsoniq_numpy:full($shape, $fill_value, $i + 1, $unused)
        return [$join]  
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
        if (size($shape) eq 1) then
            let $fill := utils:cast-as($fill_value, $params.type)
            let $join := for $j in 1 to $shape[[1]]
                        return $fill
            return [$join]
        else
            let $i := 1
            let $fill := utils:cast-as($fill_value, $params.type)
            let $join :=
                        for $j in 1 to $shape[[$i]]
                        return jsoniq_numpy:full($shape, $fill, $i + 1, $i)
            return [$join]  
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
    let $params := validate type jsoniq_numpy:identity_params {$params}
    return {
        if ($n le 1) then []
        else
            let $fill_one := utils:cast-as(1, $params.type)
            let $fill_zero := utils:cast-as(0, $params.type)
            let $join := 
                for $i in 1 to $n
                let $join2 :=
                    for $j in 1 to $n
                    return  if ($i eq $j) then $fill_one
                            else $fill_zero
                return [$join2]
            return [$join]
    }
};

declare function jsoniq_numpy:identity($n as integer) {
    jsoniq_numpy:identity($n, {})
};

(: Binary search method. It performs binary search over the given arr parameter looking for the value of x for it. The current behavior is to return the first matching position for a given x even if more values of it are present.
Required params are:
- arr (array): the array to search for x
- x (any): the value to look for in the array arr
The returned value is an integer such that:
- if x is present, the index where it first occurs is returned.
- if x is not found:
    - if x is smaller than all values, index 0 is returned.
    - if x is greater than all values, index size(arr) + 1 is returned.:)
declare %an:sequential function jsoniq_numpy:binsearch($arr as array, $x) {
    variable $low := 1;
    variable $high := size($arr) + 1;
    while ($low lt $high) {
        variable $mid := integer($low + ($high - $low) div 2);
        if ($arr[[$mid]] eq $x) then exit returning $mid;
        else
            if ($x le $arr[[$mid]]) then $high := $mid;
            else $low := $mid + 1;
    }
    exit returning if ($low eq 1) then 0 else $low;
};

(: returns i s.t. arr[i - 1] <= x < arr[i] :)
declare %an:sequential function jsoniq_numpy:searchsorted_left($arr as array, $x) {
    variable $low := 1;
    variable $high := size($arr) + 1;
    while ($low lt $high) {
        variable $mid := integer($low + ($high - $low) div 2);
        if ($x ge $arr[[$mid]]) then $low := $mid + 1;
        else $high := $mid;
    }
    exit returning if ($low eq 1) then 0 else $low;
};

(: returns i s.t. arr[i - 1] < x <= arr[i] :)
declare %an:sequential function jsoniq_numpy:searchsorted_right($arr as array, $x) {
    variable $low := 1;
    variable $high := size($arr) + 1;
    while ($low lt $high) {
        variable $mid := integer($low + ($high - $low) div 2);
        if ($x ge $arr[[$mid]]) then $low := $mid + 1;
        else $high := $mid;
    }
    exit returning if ($low eq 1) then 0 else $low;
};


(:
Return the indices of the bins to which each value in input array belongs.
Required arguments:
- x (array): input array to be binned (currently only 1 dimension is supported)
- bins (array): one dimensional monotonic, array
Optional arguments include:
- right (boolean): indicates whether the intervals include the right or the left bin edge
Values outside of the bins bounds return position 1 or size(bins) + 1 according to their relation.
(: TODO: right param:)
:)
declare function jsoniq_numpy:digitize($x as array, $bins as array) {
    let $monotonic := jsoniq_numpy:monotonic($bins)
    return {
        if ($monotonic eq 0) then
            fn:error("Bins must be monotonically increasing or decreasing!")
        else
            if ($monotonic eq 1) then
                let $join := for $i in 1 to size($x)
                            return jsoniq_numpy:searchsorted_left($bins, $x[[$i]])
                return [$join]
            else
                (: reverse case requires reversing the array :)
                let $bins_rev := [fn:reverse($bins[])]
                let $join := for $i in 1 to size($x)
                            let $searchsorted_res := jsoniq_numpy:searchsorted_left($bins_rev, $x[[$i]])
                            let $bin_index := jsoniq_numpy:compute_index($searchsorted_res, size($bins))
                            return $bin_index
                return [$join]
    }
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


declare function jsoniq_numpy:reshape_recursively($arr, $shape as array, $current_index as integer) {
    if ($current_index gt size($shape)) then
        $arr
    else
        if ($shape[[$current_index]] eq 1) then
            [jsoniq_numpy:reshape_recursively($arr, $shape, $current_index + 1)]
        else
            let $join := let $size_arr := count($arr)
                    let $size_subarr := $size_arr div $shape[[$current_index]]
                    for $j in 0 to ($shape[[$current_index]] - 1)
                    return jsoniq_numpy:reshape_recursively(subsequence($arr, $j * $size_subarr + 1, $size_subarr), $shape, $current_index + 1)
            return [$join]
};
(: Gives a new shape to an array. The shape argument should have the product of its dimension sizes equal to the number of elements found in arr.
- arr (array): the array to reshape
- shape (array): the dimension sizes to resize to. :)
declare function jsoniq_numpy:reshape($arr as array, $shape as array) {
    let $flatten_arr := flatten($arr)
    let $flatten_size := count($flatten_arr)
    let $product := jsoniq_numpy:product_of_all_values($shape)
    return
        if (($flatten_size mod $product) eq 0) then
            (: construct array :)
            jsoniq_numpy:reshape_recursively($flatten_arr, $shape, 1)
        else
            error("InvalidFunctionCallErrorCode", "Invalid call to reshape. The shape array must result in a size equivalent to the size of the array.")

};

(: Helper method for argwhere :)
declare function jsoniq_numpy:argwhere($arr, $res as array) {
    typeswitch($arr) 
        case array return {
            for $i in 1 to size($arr)
            let $sub_arr := $arr[[$i]]
            let $next_res := [$res[], $i]
            return jsoniq_numpy:argwhere($sub_arr, $next_res)
        }
        case integer return if ($arr gt 0) then $res
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

(: Helper method to compute minimum of two arrays. The minimum is computed per index, so the minimum value for a specific index is taken. :)
declare function jsoniq_numpy:min_array_rec($array1, $array2) {
    typeswitch($array1)
        case array return let $join :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:min_array_rec($array1[[$i]], $array2[[$i]])
                          return [$join]
        default return if ($array1 lt $array2) then $array1
                            else $array2
};
(: Helper method to compute minimum of two arrays. The minimum is computed per index, so the minimum value for a specific index is taken. If the minimum is greater than initial, initial is returned instead as the minimum. :)
declare function jsoniq_numpy:min_array_rec($array1, $array2, $initial) {
    typeswitch($array1)
        case array return let $join :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:min_array_rec($array1[[$i]], $array2[[$i]], $initial)
                          return [$join]
        default return if ($array1 lt $array2) then 
                            if ($initial lt $array1) then $initial
                            else $array1
                        else
                            if ($initial lt $array2) then $initial
                            else $array2
};

(: Helper method to compute the minimum on the right axis. :)
declare function jsoniq_numpy:min_rec($array as array, $axis as integer, $dim as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($dim eq $max_dim) then exit returning min(flatten($array[]));
        else {
            if ($dim eq $axis) then {
                (: Take the first array as minimum :)
                variable $mini := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $mini := jsoniq_numpy:min_array_rec($mini, $array[[$i]]);
                    $i := $i + 1;
                }
                exit returning $mini;
            } else {
                let $join :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:min_rec($array[[$i]], $axis, $dim + 1, $max_dim)
                return exit returning [$join];
            }
        }
};

(: Helper method to compute the minimum on the right axis and using initial. :)
declare function jsoniq_numpy:min_rec($array as array, $axis as integer, $dim as integer, $max_dim as integer, $initial as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($dim eq $max_dim) then exit returning min((flatten($array[]), $initial));
        else {
            if ($dim eq $axis) then {
                (: Take the first array as minimum :)
                variable $mini := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $mini := jsoniq_numpy:min_array_rec($mini, $array[[$i]], $initial);
                    $i := $i + 1;
                }
                exit returning $mini;
            } else {
                let $join :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:min_rec($array[[$i]], $axis, $dim + 1, $max_dim, $initial)
                return exit returning [$join];
            }
        }
};

(: Helper method that invokes minimum with axis only :)
declare function jsoniq_numpy:min_($array as array, $axis as integer) {
    if ($axis eq -1) then min(flatten($array[]))
    else 
        jsoniq_numpy:min_rec($array, $axis, 0, size(utils:shape($array)) - 1)
};

(: Helper method that invokes minimum with axis and initial :)
declare function jsoniq_numpy:min_($array as array, $axis as integer, $initial as integer) {
    if ($axis eq -1) then min((flatten($array[]), $initial))
    else
        jsoniq_numpy:min_rec($array, $axis, 0, size(utils:shape($array)) - 1, $initial)
        
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
            jsoniq_numpy:min_($array, $params.axis)
        else
            jsoniq_numpy:min_($array, $params.axis, $params.initial)
    }
};

declare function jsoniq_numpy:min($array as array) {
    jsoniq_numpy:min($array, {})
};

(: MAX :)

(: Helper method to compute maximum of two arrays. The maximum is computed per index, so the maximum value for a specific index is taken. :)
declare function jsoniq_numpy:max_array_rec($array1, $array2) {
    typeswitch($array1)
        case array return let $join :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:max_array_rec($array1[[$i]], $array2[[$i]])
                          return [$join]
        default return if ($array1 gt $array2) then $array1
                            else $array2
};
(: Helper method to compute maximum of two arrays. The maximum is computed per index, so the maximum value for a specific index is taken. If the maximum is greater than initial, initial is returned instead as the maximum. :)
declare function jsoniq_numpy:max_array_rec($array1, $array2, $initial) {
    typeswitch($array1)
        case array return let $join :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:max_array_rec($array1[[$i]], $array2[[$i]], $initial)
                          return [$join]
        default return if ($array1 gt $array2) then 
                            if ($initial gt $array1) then $initial
                            else $array1
                        else
                            if ($initial gt $array2) then $initial
                            else $array2
};

(: Helper method to compute the maximum on the right axis. :)
declare function jsoniq_numpy:max_rec($array as array, $axis as integer, $dim as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($dim eq $max_dim) then exit returning max(flatten($array[]));
        else {
            if ($dim eq $axis) then {
                (: Take the first array as maximum :)
                variable $maxii := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $maxii := jsoniq_numpy:max_array_rec($maxii, $array[[$i]]);
                    $i := $i + 1;
                }
                exit returning $maxii;
            } else {
                let $join :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:max_rec($array[[$i]], $axis, $dim + 1, $max_dim)
                return exit returning [$join];
            }
        }
};

(: Helper method to compute the maximum on the right axis and using initial. :)
declare function jsoniq_numpy:max_rec($array as array, $axis as integer, $dim as integer, $max_dim as integer, $initial as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($dim eq $max_dim) then exit returning max((flatten($array[]), $initial));
        else {
            if ($dim eq $axis) then {
                (: Take the first array as maximum :)
                variable $maxii := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $maxii := jsoniq_numpy:max_array_rec($maxii, $array[[$i]], $initial);
                    $i := $i + 1;
                }
                exit returning $maxii;
            } else {
                let $join :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:max_rec($array[[$i]], $axis, $dim + 1, $max_dim, $initial)
                return exit returning [$join];
            }
        }
};

(: Helper method that invokes maximum with axis only :)
declare function jsoniq_numpy:max_($array as array, $axis as integer) {
    if ($axis eq -1) then max(flatten($array[]))
    else 
        jsoniq_numpy:max_rec($array, $axis, 0, size(utils:shape($array)) - 1)
};

(: Helper method that invokes maximum with axis and initial :)
declare function jsoniq_numpy:max_($array as array, $axis as integer, $initial as integer) {
    if ($axis eq -1) then max((flatten($array[]), $initial))
    else
        jsoniq_numpy:max_rec($array, $axis, 0, size(utils:shape($array)) - 1, $initial)
        
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
            jsoniq_numpy:max_($array, $params.axis)
        else
            jsoniq_numpy:max_($array, $params.axis, $params.initial)
    }
};

declare function jsoniq_numpy:max($array as array) {
    jsoniq_numpy:max($array, {})
};


(: MEAN :)
(: TODO: Currently, only the axis argument is supported. :)
(: Helper method to sum two arrays together. :)
declare function jsoniq_numpy:sum_array_rec($array1, $array2) {
    typeswitch($array1)
        case array return let $join :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:sum_array_rec($array1[[$i]], $array2[[$i]])
                          return [$join]
        default return $array1 + $array2
};

(: Helper method to compute average on an array given the number of values. :)
declare function jsoniq_numpy:mean_array_rec($array1, $count) {
    typeswitch($array1)
        case array return let $join :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:mean_array_rec($array1[[$i]], $count)
                          return [$join]
        default return $array1 div $count
};

(: Helper method to compute the mean on the right axis. :)
declare function jsoniq_numpy:mean_rec($array as array, $axis as integer, $dim as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($dim eq $max_dim) then exit returning avg(flatten($array));
        else {
            if ($dim eq $axis) then {
                (: Take the first array as sum :)
                variable $mean := $array[[1]];
                variable $i := 2;
                while ($i le size($array)) {
                    $mean := jsoniq_numpy:sum_array_rec($mean, $array[[$i]]);
                    $i := $i + 1;
                }
                $mean := jsoniq_numpy:mean_array_rec($mean, size($array));
                exit returning $mean;
            } else {
                let $join :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:mean_rec($array[[$i]], $axis, $dim + 1, $max_dim)
                return exit returning [$join];
            }
        }
};

(: Helper method that invokes maximum with axis only :)
declare function jsoniq_numpy:mean_($array as array, $axis as integer) {
    if ($axis eq -1) then avg(flatten($array))
    else 
        jsoniq_numpy:mean_rec($array, $axis, 0, size(utils:shape($array)) - 1)
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
    return jsoniq_numpy:mean_($array, $params.axis)
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
                                let $join :=
                                    for $i in 1 to size($array)
                                    return jsoniq_numpy:absolute($array[[$i]])
                                return [$join]
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
    variable $pivot := jsoniq_numpy:random_randint($low, {"high": $high + 1, "size": 1})[[1]];
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

(: TODO: Test with strings :)
(: Helper method to compute count nonzero on an array given the number of values. :)
declare function jsoniq_numpy:count_nonzero_rec_array($array1) {
    typeswitch($array1)
        case array return let $join :=
                                for $i in 1 to size($array1)
                                return jsoniq_numpy:count_nonzero_rec_array($array1[[$i]])
                          return [$join]
        case string return if ($array1 eq "") then 0
                           else 1
        case boolean return if ($array1) then 1
                            else 0
        default return if ($array1 ne 0) then 1
                       else 0
};

(: Helper method to compute the mean on the right axis. :)
declare function jsoniq_numpy:count_nonzero_rec($array as array, $axis as integer, $dim as integer, $max_dim as integer) {
    if ($axis gt $max_dim) then error("InvalidFunctionCallErrorCode","Axis value higher than maximum dimension! Choose a value fitting the dimensions of your array.");
    else
        if ($dim eq $max_dim) then exit returning sum(flatten(jsoniq_numpy:count_nonzero_rec_array($array)));
        else {
            if ($dim eq $axis) then {
                variable $count := jsoniq_numpy:count_nonzero_rec_array($array[[1]]);
                variable $i := 2;
                while ($i le size($array)) {
                    variable $curr_count := jsoniq_numpy:count_nonzero_rec_array($array[[$i]]); 
                    $count := jsoniq_numpy:sum_array_rec($curr_count, $count);
                    $i := $i + 1;
                }
                exit returning $count;
            } else {
                let $join :=
                    let $size := size($array)
                    for $i in 1 to $size
                    return jsoniq_numpy:count_nonzero_rec($array[[$i]], $axis, $dim + 1, $max_dim)
                return exit returning [$join];
            }
        }
};

(: Helper method that invokes maximum with axis only :)
declare function jsoniq_numpy:count_nonzero_($array as array, $axis as integer) {
    if ($axis eq -1) then sum(flatten(jsoniq_numpy:count_nonzero_rec_array($array)))
    else 
        jsoniq_numpy:count_nonzero_rec($array, $axis, 0, size(utils:shape($array)) - 1)
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
    return jsoniq_numpy:count_nonzero_($array, $params.axis)
};

declare function jsoniq_numpy:count_nonzero($array as array) {
    jsoniq_numpy:count_nonzero($array, {})
};


(: unique returns a 1 dimensional array containing the unique elements of the array, sorted ascendingly. Currently, we only support flattening of the unique array as axis behavior is more intricate for jsoniq to support.
Required params are:
- array (array): the array to look for unique values.:)
declare function jsoniq_numpy:unique($array as array) {
    jsoniq_numpy:sort([distinct-values(flatten($array))])
};