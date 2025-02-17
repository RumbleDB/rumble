(:JIQS: ShouldRun; Output="({ "registration_dttm" : "2016-02-03T07:55:29Z", "id" : 1, "first_name" : "Amanda", "last_name" : "Jordan", "email" : "ajordan0@com.com", "gender" : "Female", "ip_address" : "1.197.201.2", "cc" : 6759521864920116, "country" : "Indonesia", "birthdate" : "3\/8\/1971", "salary" : 49756.53, "title" : "Internal Auditor", "comments" : "1E+02" }, 100)" :)
avro-file("../../../queries/userdata1-limit.avro")[1],
count(avro-file("../../../queries/userdata1-limit.avro"))

(: read in file without arguments :)
