(:JIQS: ShouldRun; Output="83" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
sum(parallelize(("41", "42")) ! ex:CodeListOrInteger(.))
