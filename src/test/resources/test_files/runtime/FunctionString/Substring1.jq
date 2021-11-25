(:JIQS: ShouldRun; Output="(car, ada, 234, 12, , 1, )" :)
substring("motor car", 7),
substring("metadata", 4, 3),
substring("12345", 1.5, 2.6),
substring("12345", 0, 3),
substring("12345", 5, -3),
substring("12345", -3, 5),
substring((), 1, 3),
substring("12345", 0 div 0E0, 3),
substring("12345", 1, 0 div 0E0),
substring("12345", -42, 1 div 0E0)
