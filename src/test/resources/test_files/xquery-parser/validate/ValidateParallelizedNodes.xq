(:JIQS: ShouldRun; Output="(true, true)" :)
import schema namespace t = "urn:validate-test" at "ValidateImportedSchema.xsd";

(: Validated nodes carry schema type annotations, which Kryo must serialize to send them to executors. :)
for $root in parallelize((
    validate strict { <t:root><t:amount>1</t:amount></t:root> },
    validate strict { <t:root><t:amount>2</t:amount></t:root> }
), 2)
return data($root/t:amount) instance of t:Count
