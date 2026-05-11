for $data in parquet-file("../../../queries/delta_benchmark_data/customer.parquet")
count $c
where $c le 32
return $data