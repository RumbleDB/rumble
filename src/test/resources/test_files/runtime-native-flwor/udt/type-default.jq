(:JIQS: ShouldRun; Output="({ "foo" : "foo", "bar" : "foobar", "int" : 1 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 1 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "def", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 42 }, { "foo" : "foo", "bar" : "foobar", "int" : 1, "date" : 2021-01-01 }, { "foo" : "foo", "date" : 2020-12-31, "bar" : "def" }, { "foo" : "foo", "bar" : "foobar", "date" : 2021-01-01 }, { "foo" : "foo", "bar" : "def", "date" : 2021-01-01 }, { "foo" : "foo", "bar" : "foobar", "int" : 42, "date" : 2021-01-01 }, { "foo" : "foo", "bar" : "def", "date" : 2021-01-01 }, { "foo" : "foo", "bar" : "def", "date" : 2021-01-01 }, { "foo" : "foo", "bar" : "foobar", "date" : 2021-01-01 }, { "foo" : "foo", "bar" : "def", "date" : 2021-01-01 }, { "foo" : "foo", "bar" : "foobar", "date" : 2021-01-01 })" :)
declare type local:a as { "!foo" : "string", "bar" : "string=def", "int" : "integer=42" };
declare type local:b as { "foo" : "string=def" };
declare type local:c as { "foo" : "string" };

declare type local:a-verbose as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    {
      "name" : "foo",
      "required" : true,
      "type" : "string"
    },
    {
      "name" : "bar",
      "type" : "string",
      "default" : "def"
    },
    {
      "name" : "int",
      "type" : "integer",
      "default" : 42
    }
  ]
};


declare type local:a-open as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    {
      "name" : "foo",
      "required" : true,
      "type" : "string"
    },
    {
      "name" : "bar",
      "type" : "string",
      "default" : "def"
    },
    {
      "name" : "date",
      "type" : "date",
      "default" : "2021-01-01"
    }
  ],
  "closed" : false
};


validate type local:a { { "foo" : "foo", "bar" : "foobar", "int" : 1 } },
validate type local:a { { "foo" : "foo" } },
validate type local:a { parallelize({ "foo" : "foo", "bar" : "foobar" }) },
validate type local:a { parallelize({ "foo" : "foo" }) },
validate type local:a { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a { validate type local:c* { { "foo" : "foo" } } },

validate type local:a* { { "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" } },
validate type local:a* { parallelize(({ "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" })) }(:,
validate type local:a* { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a* { validate type local:c* { { "foo" : "foo" } } }:),

validate type local:a-verbose { { "foo" : "foo", "bar" : "foobar", "int" : 1 } },
validate type local:a-verbose { { "foo" : "foo" } },
validate type local:a-verbose { parallelize({ "foo" : "foo", "bar" : "foobar" }) },
validate type local:a-verbose { parallelize({ "foo" : "foo" }) },
validate type local:a-verbose { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a-verbose { validate type local:c* { { "foo" : "foo" } } },

validate type local:a-verbose* { { "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" } },
validate type local:a-verbose* { parallelize(({ "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" })) }(:,
validate type local:a-verbose* { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a-verbose* { validate type local:c* { { "foo" : "foo" } } }:),

validate type local:a-open { { "foo" : "foo", "bar" : "foobar", "int" : 1 } },
validate type local:a-open { { "foo" : "foo", "date" : "2020-12-31" } },
validate type local:a-open { parallelize({ "foo" : "foo", "bar" : "foobar" }) },
validate type local:a-open { parallelize({ "foo" : "foo" }) },
validate type local:a-open { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a-open { validate type local:c* { { "foo" : "foo" } } },

validate type local:a-open* { { "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" } },
validate type local:a-open* { parallelize(({ "foo" : "foo" }, { "foo" : "foo", "bar" : "foobar" })) }(:,
validate type local:a-open* { validate type local:a* { { "foo" : "foo", "bar" : "foobar" } } },
validate type local:a-open* { validate type local:c* { { "foo" : "foo" } } }:)
