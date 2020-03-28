(:JIQS: ShouldRun; Output="(true, true, false, true)" :)
validate("src/main/resources/queries/validationAndAnnotation/JSoundSchema.json", {"first": "James", "last": "Kirk", "picture": "af88", "birthDate": "2001-02-10", "maritalStatus": null}, "targetType", false),
validate("src/main/resources/queries/validationAndAnnotation/JSoundSchemaCompact.json", {"first": "James", "last": "Kirk", "picture": "af88", "birthDate": "2001-02-10", "maritalStatus": null}, "targetType", true),
validate("src/main/resources/queries/validationAndAnnotation/JSoundSchema.json", {"picture": "bf98", "birthDate": "1999-12-31", "otherFieldObject": { "hello": "world!", "nullField": null}}, "targetType", false),
validate("src/main/resources/queries/RedditJSoundSchema.json", {"score_hidden":false,"name":"t1_cnas8zv","link_id":"t3_2qyr1a","body":"Most of us have some family members like this. *Most* of my family is like this. ","downs":0,"created_utc":"1420070400","score":14,"author":"YoungModern","distinguished":null,"id":"cnas8zv","archived":false,"parent_id":"t3_2qyr1a","subreddit":"exmormon","author_flair_css_class":null,"author_flair_text":null,"gilded":0,"retrieved_on":1425124282,"ups":14,"controversiality":0,"subreddit_id":"t5_2r0gj","edited":false}, "targetType", false)

(: general tests :)
