 (:JIQS: ShouldRun; Output="{ "name" : "MA", "stores" : 1 }" :)
declare function for-let() {
    variable $res := ();
    let $stores := [  { "store number" : 1, "state" : "MA" } ]
    let $nations := [  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ]
    for $nation in $nations[], $store allowing empty in $stores[]
        [ $$.state eq $nation.state ]
    return exit returning {$res := ($res, { "name": $nation.state, "stores": $store."store number" }); $res};
    exit returning 10;
};
for-let()