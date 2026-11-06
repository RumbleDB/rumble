(:JIQS: ShouldRun; Output="foo" :)
head(for $i in parallelize(("foo", "bar", "baz")) return anyURI($i))

(: RDD-based test :)
