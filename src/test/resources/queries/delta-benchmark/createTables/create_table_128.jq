for $data in parquet-file("../../../queries/delta_benchmark_data/biggh.parquet")
count $c
where $c le 128
return $data