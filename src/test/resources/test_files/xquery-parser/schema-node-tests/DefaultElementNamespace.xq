(:JIQS: ShouldRun; Output="true" :)
import schema default element namespace "urn:declarations" at "Declarations.xsd";
validate strict { <member>1</member> } instance of schema-element(member)
