(:JIQS: ShouldRun; Output="" :)

max(for $o in json-lines("../../../../queries/confusion_sample_empty.json") return $o.date)

(: empty file :)

