for $d in delta-file("../../../queries/delta_benchmark_data/districtTable")
where $d.D_ID eq 3 and $d.D_W_ID eq 1
return replace value of $d.D_NEXT_O_ID with ($d.D_NEXT_O_ID + 1)