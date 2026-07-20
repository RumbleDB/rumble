(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
ex:TwoDigitCodeOrInteger("42") instance of xs:integer
