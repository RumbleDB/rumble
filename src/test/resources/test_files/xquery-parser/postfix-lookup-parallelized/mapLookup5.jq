(:JIQS: ShouldRun; Output="(1, third)" :)
parallelize(map{"first" : "1", "second": "third", "fourth": 5})  ? ("first", "second", "nothere")