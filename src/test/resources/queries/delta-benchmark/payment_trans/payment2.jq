let $h_amt := 343
for $c in delta-file("../../../queries/delta_benchmark_data/customerTable2")
return
    (
        replace value of $c.C_BALANCE with $c.C_BALANCE + $h_amt,
        if ($c.C_CREDIT eq "BC")
        then
            replace value of $c.C_DATA with $c.C_DATA || $c.C_ID || $c.C_D_ID || $c.C_W_ID || $h_amt
        else
            ()
    )
