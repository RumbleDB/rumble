(:JIQS: ShouldRun; Output="string" :)
typeswitch("Hello world!")
case hexBinary? return hexBinary("0123")
case boolean+ return true
case decimal return 1
case string* return "string"
default return "default"

(: simple case without variables :)
