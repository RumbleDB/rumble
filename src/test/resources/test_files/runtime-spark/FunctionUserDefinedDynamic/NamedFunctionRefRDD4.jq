(:JIQS: ShouldRun; Output="8" :)
declare function fn ($i as double?, $j as decimal) as double+ {$i + $j};
parallelize(fn#2(3,5))
