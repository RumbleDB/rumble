(:JIQS: ShouldRun; Output="5" :)
parallelize(1 to 1000000)[position() eq (1 to 5)[position() eq last()]]

