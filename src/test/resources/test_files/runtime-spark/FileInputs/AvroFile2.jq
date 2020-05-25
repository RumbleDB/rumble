(:JIQS: ShouldRun; Output="(Amanda, Albert)" :)
avro-file("src/test/resources/test_data/userdata1-limit.avro", {ignoreExtension: false}).first_name[1],
avro-file("src/test/resources/test_data/userdata1-limit.avro", {ignoreExtension: true}).first_name[2]

(: add ignoreExtension option :)
