(:JIQS: ShouldRun; Output="({ "guess" : "Finnish", "target" : "Finnish", "country" : "AU", "choices" : [ "Finnish", "Kurdish", "Turkish", "Yiddish" ], "sample" : "4a3dfa40ed621940a0a38408956b86e0", "date" : "2013-08-19" }, { "guess" : "Finnish", "target" : "Croatian", "country" : "AU", "choices" : [ "Croatian", "Estonian", "Finnish", "Samoan" ], "sample" : "a0d2070edd63b46a3e71b4fb7ffe5375", "date" : "2013-08-20" }, { "guess" : "Finnish", "target" : "Finnish", "country" : "AU", "choices" : [ "Bosnian", "Finnish", "Hungarian", "Slovak" ], "sample" : "d47ff7f3aa1abf002cad40ef24122c08", "date" : "2013-08-20" }, { "guess" : "Finnish", "target" : "Finnish", "country" : "AU", "choices" : [ "Amharic", "Finnish", "Russian", "Samoan" ], "sample" : "65b5bf9df0f8ec61111d20c6b48db671", "date" : "2013-08-20" })":)
let $f := function($x) { $x.guess eq "Finnish" }
for $o in json-file("../../../queries/confusion_sample.json")
where $f($o)
return $o
