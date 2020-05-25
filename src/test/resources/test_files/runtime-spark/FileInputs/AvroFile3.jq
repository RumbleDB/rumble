(:JIQS: ShouldRun; Output="(Amanda, Albert)" :)
avro-file("src/test/resources/test_data/userdata1-limit.avro", {}).first_name[1],
avro-file("src/test/resources/test_data/userdata1-limit.avro", {irrelevant: "foo"}).first_name[2]

(: empty object and invalid option (should be ignored) :)
