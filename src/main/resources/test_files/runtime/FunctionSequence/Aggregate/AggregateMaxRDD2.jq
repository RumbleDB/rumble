(:JIQS: ShouldRun; Output="" :)

max(for $o in json-file("../../../../queries/confusion_sample_empty.json") return $o.date)

(: empty file :)

