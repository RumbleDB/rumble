(:JIQS: ShouldRun; Output="false" :)
copy $je := {"foo":"bar", "bar":"baz"}
modify ()
return exists(map:get(map:remove($je, xs:untypedAtomic("foo")), "foo"))
