(:JIQS: ShouldRun; Output="(1, third, 5)" :)
parallelize(map{"first" : "1", "second": "third", "fourth": 5})  ? ("first", "second", "fourth")