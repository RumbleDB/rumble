(:JIQS: ShouldRun; Output="({ "id" : 1, "age" : 20, "weight" : 68.8, "name" : "a", "isAlien" : null, "isAlive" : true }, { "id" : 2, "age" : 35, "weight" : 72.4, "name" : "a", "isAlien" : null, "isAlive" : true }, { "id" : 3, "age" : 50, "weight" : 76.3, "name" : "a", "isAlien" : null, "isAlive" : true })" :)
annotate(
    (
        {"id": 1, "age":  20, "weight": 68.8, "name": "a", "isAlien": null, "isAlive": true},
        {"id": 2, "age":  35, "weight": 72.4, "name": "a", "isAlien": null, "isAlive": true},
        {"id": 3, "age":  50, "weight": 76.3, "name": "a", "isAlien": null, "isAlive": true}
    ),
    {
        "id": "integer",
        "age": "integer",
        "weight": "double",
        "name": "string",
        "isAlien": "null",
        "isAlive": "boolean"
    }
)
