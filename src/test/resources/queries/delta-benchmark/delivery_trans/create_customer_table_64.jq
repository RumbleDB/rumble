for $data in parquet-file("../../../queries/delta_benchmark_data/customer.parquet")
count $c
where $c le 64
return $data