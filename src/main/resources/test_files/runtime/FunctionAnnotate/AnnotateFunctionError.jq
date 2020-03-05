(:JIQS: ShouldCrash:)
annotateFile("src/main/resources/queries/validationAndAnnotation/JSoundSchema.json", {"picture": "bf98", "birthDate": "1999-12-31", "otherFieldObject": { "hello": "world!", "nullField": null}}, "targetType", false)

(: Annotation can't be done. The candidate instance is invalid against the provided schema :)
