(:JIQS: ShouldRun; Output="empty" :)
typeswitch(())
case string return "string"
case () return "empty"
default return "other"