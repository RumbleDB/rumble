(:JIQS: ShouldCrash:)
validate("src/main/resources/queries/validationAndAnnotation/JSoundSchema.json", {"picture": "bf98", "birthDate": "1999-12-31", "otherFieldObject": { "hello": "world!", "nullField": null}}, "targetType", true)

(: validating with compact syntax when schema is defined with extended syntax :)
