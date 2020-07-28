(:JIQS: ShouldRun; Output="(Amanda, Albert)" :)
avro-file("../../../queries/userdata1-limit.avro", {ignoreExtension: false}).first_name[1],
avro-file("../../../queries/userdata1-limit.avro", {ignoreExtension: true}).first_name[2]

(: add ignoreExtension option :)
