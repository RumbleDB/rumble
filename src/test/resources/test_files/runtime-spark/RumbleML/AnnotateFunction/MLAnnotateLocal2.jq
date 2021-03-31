(:JIQS: ShouldRun; Output="({ "id" : 1, "name" : "a", "isAlien" : null, "isAlive" : true }, { "id" : 2, "name" : "a", "isAlien" : null, "isAlive" : true }, { "id" : 3, "name" : "a", "isAlien" : null, "isAlive" : true })" :)
annotate(
    (
        {"id": 1, "name": "a", "isAlien": null, "isAlive": true},
        {"id": 2, "name": "a", "isAlien": null, "isAlive": true},
        {"id": 3, "name": "a", "isAlien": null, "isAlive": true}
    ),
    {
        "id": "integer",
        "name": "string",
        "isAlien": "null",
        "isAlive": "boolean"
    }
)

(: More atomic types :)
