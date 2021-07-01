(:JIQS: ShouldRun; Output="({ "foo" : "foo", "bar" : "foobar", "int" : 1 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "bar" : "foobar", "foo" : "foo", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "bar" : "def", "int" : 42, "foo" : "foo" }, { "bar" : "foobar", "int" : 42, "foo" : "foo" })" :)
declare type local:a as { "!foo" : "string", "bar" : "string=def", "int" : "integer=42" };
declare type local:b as { "foo" : "string=def" };
declare type local:c as { "foo" : "string" };

validate type local:a { { "foo" : "foo", "bar" : "foobar", "int" : 1 } },
validate type local:a { { "foo" : "foo" } },
validate type local:a { parallelize({ "foo" : "foo", "bar" : "foobar" }) },
validate type local:a { parallelize({ "foo" : "foo" }) },
validate type local:a { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a { validate type local:c* { { "foo" : "foo" } } },

validate type local:a* { { "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" } }(:,
validate type local:a* { parallelize(({ "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" })) },
validate type local:a* { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a* { validate type local:c* { { "foo" : "foo" } } }:)
