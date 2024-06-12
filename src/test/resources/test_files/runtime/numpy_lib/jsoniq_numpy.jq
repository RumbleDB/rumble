(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace jsoniq_numpy = "jsoniq_numpy.jq";
(: Helper function for zeros :)
declare function jsoniq_numpy:zeros($shape as array, $zero, $i as integer) {
    if ($i eq size($shape)) then
      let $join :=
                      for $j in 1 to $shape[[$i]]
                      return $zero
            return [$join]
    else
      let $join := 
                    for $j in 1 to $shape[[$i]]
                    return jsoniq_numpy:zeros($shape, $zero, $i + 1)
      return [$join] 
};
(: Zeros method creates an array filled with 0 values.
Required parameters are:
- shape (array): contains the size of each dimension of the resulting array. For one dimensional arrays, a single value within is array is expected - e.g., [7] results in a 1 dimensional array with 7 zeroed elements.
Params is an object for optional arguments. These arguments are:
- type (string): the enforced type of the resulting array.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value:)
declare function jsoniq_numpy:zeros($shape as array, $type as string) {
    variable $zero;
    switch ($type)
        case "string" return $zero := 0 cast as string;
        case "integer" return $zero := 0 cast as integer;
        case "decimal" return $zero := 0 cast as decimal;
        case "double" return $zero := 0 cast as double;
        default return $zero := 0 cast as integer;
    if (size($shape) eq 1) then
        let $join := for $j in 1 to $shape[[1]]
                     return $zero
        return [$join]
    else
        let $i := 1
        let $join :=
                    for $j in 1 to $shape[[$i]]
                    return jsoniq_numpy:zeros($shape, $zero, $i + 1)
        return [$join]  
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
                    for $j in 1 to $shape[[$i]]
                    return jsoniq_numpy:ones($shape, $one, $i + 1)
      return [$join]  

};
(:
Required parameters are:
- shape (array): contains the size of each dimension of the resulting array. For one dimensional arrays, a single value within is array is expected - e.g., [7] results in a 1 dimensional array with 7 elements with value 1.
Params is an object for optional arguments. These arguments are:
- type (string): the enforced type of the resulting array.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value
:)
declare function jsoniq_numpy:ones($shape as array, $type as string) {
    variable $one;
    switch ($type)
        case "string" return $one := 1 cast as string;
        case "integer" return $one := 1 cast as integer;
        case "decimal" return $one := 1 cast as decimal;
        case "double" return $one := 1 cast as double;
        default return $one := 1 cast as integer;
    if (size($shape) eq 1) then
        let $join := for $j in 1 to $shape[[1]]
                    return $one
        return [$join]
    else
        let $i := 1
        let $join :=
                    for $j in 1 to $shape[[$i]]
                    return jsoniq_numpy:ones($shape, $one, $i + 1)
        return [$join]  
};


(: 
declare function local:min_array($array1 as array, $array2 as array) {

};

declare function local:min_rec($array as array, $axis as integer, $dim as integer) {
    if ($dim eq 0) then {();}
    else {
        if ($dim eq $axis) then {
            (: Take the first array as minimum :)
            variable $mini := $array[[1]];
            for $i in 2 to $dim
            return $mini := local:min_array($mini, $array[[$i]]);
            exit returning $mini;
        } else {
            for $i in 1 to $dim
            return [local:min_rec($array[[$i]], $axis, size($array[[$i]]))];
        }
    }
};

declare function local:min($array as array, $axis as integer) {
    local:min_res($array, $axis, size($array))
}; 
:)

(:
Return evenly spaced numbers over a specified interval. Returns num evenly spaced samples, calculated over the interval [start, stop]. The endpoint of the interval can optionally be excluded.
Note: we currently only support one-dimensional results.
Required params are:
- start (integer) - the start value bounding the generated sequence
- end (integer) - the end value bounding the generated sequence
Params is an object for optional arguments. These arguments are:
- num (integer): number of samples to generate. Default is 50.
- endpoint (bool): if true, stop is the last sample. Otherwise it is not included. Default is true.
- retstep (bool): if true, returns the step used which denotes the spacing of values. Default is false.
- dtype UNSUPPORTED: return is always double.
- axis UNSUPPORTED.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value:)
declare function jsoniq_numpy:linspace($start as integer, $end as integer, $params as object) {
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
- stop (double): the end of the interval.
- step (double): the spacing of output values. Default value is 1.
- dtype (string): type of the returned array. The possible values include "integer" or "double".
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value
:)
declare function jsoniq_numpy:arange($stop as double, $params as object) {
    (: ADD LOGIC FOR WHEN PARAMS ARE MISSING :)
    if ($params.start gt $stop) then []
    else
        if ($params.start + $params.step gt $stop or $params.step eq 0) then [$params.start]
        else
            let $num_values := integer(ceiling(($stop - $params.start) div $params.step))
            let $res := for $i in 1 to $num_values
                        return $params.start + ($i - 1) * $params.step
            return [$res]
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


(: Function generates a random sample from a uniform distribution between a lower and higher limit (not inclusive). Params is an object for optional arguments. These arguments are:
    - low (double): the lower bound for generated objects. Default value is 0.0
    - high (double): the upper bound for generated objects. Default value is 1.0
    - size (integer): the size of the resulting array. Default is 10.
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value :)
declare function jsoniq_numpy:random_uniform($params as object) {
    let $low := $params.low
    let $high := $params.high
    let $size := $params.size
    return fn:random-between($low, $high, $size, "double")
};

(: Function generates a random sample from a uniform distribution between a lower and higher limit (not inclusive), but with type int.
Required params are:
    - low (integer): the lower bound for generated objects if high is also present. Otherwise, it determines the upperbound. Without high, it makes the sequence be bounded by [0, low), otherwise it makes it bound by [low, high).
Params is an object for optional arguments. These arguments are:
    - high (integer): the upper bound for generated objects. Default value is 1
    - size (integer): the size of the resulting array. Default is 10
To submit optional parameters to this method, a JSON object must be passed, where the argument is the key and its value the pertaining value :)
    declare function jsoniq_numpy:random_randint($low as integer, $params as object) {
        let $high := $params.high
        let $size := $params.size
        return fn:random-between($low, $high, $size, "integer")
    };

(: Generate evenly spaced numbers on a log scale in base 10.
Required parameters are:
    - start (integer): 10 ** start is the starting value of the sequence.
    - end (integer): 10 ** end is the end value of the sequence.
Params is an object for optional arguments. These arguments are:
    - num (integer): number of samples to generate. Default is 50.
    - endpoint (bool): if true, stop is the last sample. Otherwise it is not included. Default is true.
    - base UNSUPPORTED: base 10 is always  used.
    - dtype UNSUPPORTED: return is always double.
    - axis UNSUPPORTED.:)
declare function jsoniq_numpy:logspace($start as integer, $end as integer, $params as object) {
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
    "type": "string=string"
};
(: The full method returns a new array of given shape and type, filled with fill_value.
Required arguments:
- shape (array): the dimension of the new array
- fill_value (atomic): the fill value
Optional arguments include:
- type (string): the type of the resulting array values
Unsupported arguments:
- order: ordering is implicitly done row-wise (C format)
:)
declare function jsoniq_numpy:full($shape as array, $fill_value, $params as object) {
    let $params := validate type jsoniq_numpy:full_params {$params}
    return { variable $fill;
    switch ($params.type)
        case "string" return $fill := $fill_value cast as string;
        case "integer" return $fill := $fill_value cast as integer;
        case "decimal" return $fill := $fill_value cast as decimal;
        case "double" return $fill := $fill_value cast as double;
        default return $fill := $fill_value cast as integer;
    if (size($shape) eq 1) then
        let $join := for $j in 1 to $shape[[1]]
                    return $fill
        return [$join]
    else
        let $i := 1
        let $join :=
                    for $j in 1 to $shape[[$i]]
                    return jsoniq_numpy:full($shape, $fill, $i + 1, $i)
        return [$join]  
    }
};

(:
Return the identity array. The identity array is a square array with ones on the main diagonal.
Required arguments:
- n (integer): the number of rows (and columns) in N x N output.
Optional arguments include:
- type (string): the type of the resulting array values
:)
declare function jsoniq_numpy:identity($n as integer, $params as object) {
    variable $fill_one, $fill_zero;
    if ($n le 1) then [];
    else
        switch ($params.type)
            case "string" return {$fill_one := 1 cast as string; $fill_zero := 0 cast as string;}
            case "integer" return {$fill_one := 1 cast as integer; $fill_zero := 0 cast as integer;}
            case "decimal" return {$fill_one := 1 cast as decimal; $fill_zero := 0 cast as decimal;}
            case "double" return {$fill_one := 1 cast as double; $fill_zero := 0 cast as double;}
            default return {$fill_one := 1 cast as integer; $fill_zero := 0 cast as integer;}
        let $join := 
            for $i in 1 to $n
            let $join2 :=
                for $j in 1 to $n
                return  if ($i eq $j) then $fill_one
                        else $fill_zero
            return [$join2]
        return [$join]
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
            if ($x lt $arr[[$mid]]) then $high := $mid;
            else $low := $mid + 1;
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
:)
declare function jsoniq_numpy:digitize($x as array, $bins as array) {
    let $join :=
        for $i in 1 to size($x)
        return jsoniq_numpy:binsearch($bins, $x[[$i]])
    return [$join]
};