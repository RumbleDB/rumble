let $no_o_id := 4
let $w_id := 1
let $d_id_to_ol_total := (
    for $d in delta-file("../../../queries/delta_benchmark_data/districtTable"),
        $ol in delta-file("../../../queries/delta_benchmark_data/orderLineTable")[ $$.OL_D_ID eq $d.D_ID ]
    where $ol.OL_W_ID eq $w_id and $ol.OL_O_ID eq $no_o_id
    group by $d_id := $d.D_ID
    return { "d_id" : $d_id, "ol_total" : sum($ol.OL_AMOUNT) }
)
for $d in $d_id_to_ol_total,
    $c in delta-file("../../../queries/delta_benchmark_data/customerTable4")[ $$.C_D_ID eq $d.d_id ]
where $c.C_W_ID eq $w_id
return replace value of $c.C_BALANCE with ($c.C_BALANCE + $d.ol_total)
