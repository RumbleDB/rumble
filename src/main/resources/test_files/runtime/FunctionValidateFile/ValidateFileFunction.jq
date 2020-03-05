(:JIQS: ShouldRun; Output="(true, true, false, true)" :)
validateFile("src/main/resources/queries/validationAndAnnotation/JSoundSchema.json", "src/main/resources/queries/validationAndAnnotation/CandidateInstance.json", "targetType", false),
validateFile("src/main/resources/queries/validationAndAnnotation/JSoundSchemaCompact.json", "src/main/resources/queries/validationAndAnnotation/CandidateInstance.json", "targetType", true),
validateFile("src/main/resources/queries/validationAndAnnotation/JSoundSchema.json", "src/main/resources/queries/validationAndAnnotation/InvalidCandidateInstance.json", "targetType", false),
validateFile("src/main/resources/queries/RedditJSoundSchema.json", "src/main/resources/queries/Reddit.json", "targetType", false)

(: general tests :)
