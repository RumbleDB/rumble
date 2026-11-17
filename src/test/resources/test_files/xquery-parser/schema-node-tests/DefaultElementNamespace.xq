(:JIQS: ShouldRun; Output="true" :)
(: The unprefixed test name must resolve to the imported default element namespace. :)
import schema default element namespace "urn:declarations" at "Declarations.xsd";
validate strict { <member>1</member> } instance of schema-element(member)
