jsoniq version "3.1";
(:JIQS: ShouldRun; Output="(xs:string, xs:anyAtomicType, js:object, xs:int, xs:dateTime, xs:string, xs:decimal, xs:date, xs:time, xs:duration, xs:boolean, js:null, xs:base64Binary, xs:hexBinary, xs:anyURI, xs:dateTimeStamp, xs:dayTimeDuration, xs:yearMonthDuration, js:object, xs:int)":)

fn:item-type(["2", "3", "4"]),
fn:item-type([1, "2", 3]),
fn:item-type({"a": "boo"}),
fn:item-type(3),
fn:item-type(dateTime("2001-12-12T23:00:00")),
fn:item-type("test"),
fn:item-type(3.234),
fn:item-type(date("2001-12-12-10:00")),
fn:item-type(time("13:20:30.5555")),
fn:item-type(duration("P3Y5M") ),
fn:item-type(true),
fn:item-type(null),
fn:item-type(base64Binary("abcdEFGH")),
fn:item-type(hexBinary("ab88")),
fn:item-type(anyURI("example.com/")),
fn:item-type(dateTimeStamp("2004-04-12T13:20:00-05:00")),
fn:item-type(dayTimeDuration("P3DT5H6.001S")),
fn:item-type(yearMonthDuration("P2Y4M")),
fn:item-type(json-lines("../../../queries/rumbleML/sample-ml-data-age-weight.json")),
fn:item-type(json-lines("../../../queries/rumbleML/sample-ml-data-age-weight.json")."age")