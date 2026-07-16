(:JIQS: ShouldParse :)
import schema namespace ex = "urn:example:schema" at "one.xsd", "two.xsd";
validate type ex:root { <root/> }
