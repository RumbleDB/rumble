(:JIQS: ShouldRun; Output="(500, 0)" :)
count(json-lines("../../../queries/confusion_sample.json")),
count(json-lines("../../../queries/confusion_sample_empty.json"))
