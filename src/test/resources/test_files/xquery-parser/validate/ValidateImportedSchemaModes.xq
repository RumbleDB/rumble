(:JIQS: ShouldRun; Output="(true, true, true, true)" :)
import schema namespace t = "urn:validate-test" at "ValidateImportedSchema.xsd";

(
    data(validate type t:Count { <value>42</value> }) instance of t:Count,
    data((validate type t:Record { <t:value><t:amount>42</t:amount></t:value> })/t:amount)
        instance of t:Count,
    data((validate strict { <t:root><t:amount>42</t:amount></t:root> })/t:amount)
        instance of t:Count,
    data((validate lax {
        <unknown xmlns="urn:unknown"><t:countValue>42</t:countValue></unknown>
    })/t:countValue) instance of t:Count
)
