(:JIQS: ShouldRun; Output="" :)

max(for $o in json-file("./src/test/resources/test_data/confusion_sample_empty.json") return $o.date)

(: empty file :)

