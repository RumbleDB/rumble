(:JIQS: ShouldRun; Output="2013-08-20" :)

max(for $o in json-file("./src/test/resources/test_data/conf-ex.json") return $o.date)
