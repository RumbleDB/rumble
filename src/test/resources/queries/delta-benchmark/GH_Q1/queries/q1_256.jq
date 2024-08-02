let $data := delta-file("../../../../queries/delta_benchmark_data/bigghTables/bigghTable256")
return
    for $d in $data
    return replace value of $d.public with (not $d.public)