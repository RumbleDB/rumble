(:JIQS: ShouldRun; Output="correct" :)
typeswitch(duration("P4Y5MT5M"))
case string | decimal? | hexBinary* return "not this"
case base64Binary+ return "not even this"
default return "correct"

(: simple case with sequenceType union without variables :)
