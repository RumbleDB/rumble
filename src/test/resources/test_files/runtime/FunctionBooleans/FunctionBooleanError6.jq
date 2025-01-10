(:JIQS: ShouldCrash; ErrorCode="FORG0006" :)
boolean([1,2]),
boolean({"a":2}),
boolean([1,2]),
boolean(({"a":2}, 2))

(: as of jsoniq3.1 arrays and objects dont have effective boolean value anymore :)
