(:JIQS: ShouldRun; Output="2013-08-20" :)

min(for $o in json-lines("../../../../queries/conf-ex.json") return $o.date)
