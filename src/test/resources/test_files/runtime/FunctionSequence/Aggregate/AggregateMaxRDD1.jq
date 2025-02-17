(:JIQS: ShouldRun; Output="2013-08-20" :)

max(for $o in json-file("../../../../queries/conf-ex.json") return $o.date)
