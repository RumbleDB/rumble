for $d in delta-file("../../../queries/delta_benchmark_data/bigghTables/bigghTable128")
return (
    insert "ids" : [ { "repo_id" : $d.repo.id, "actor_id" : $d.actor.id, "id" : $d.id } ] into $d,
    delete $d.repo.id,
    delete $d.actor.id,
    delete $d.id
)