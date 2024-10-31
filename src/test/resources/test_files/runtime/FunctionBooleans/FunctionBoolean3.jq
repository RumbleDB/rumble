(:JIQS: ShouldRun; Output="(false, true, false)" :)
boolean(""),
boolean(anyURI("mailto:rumble")),
boolean(anyURI(""))

(: the zero-length string and equivalent anyURI implementation should return false, otherwise true :)
