(:JIQS: ShouldRun; Output="(true, true)" :)
let $fileDoc := doc("../../../queries/xml/helloworld.xml")
return (
  ends-with(string(fn:base-uri($fileDoc)), "helloworld.xml"),
  ends-with(string(fn:base-uri($fileDoc/helloworld)), "helloworld.xml")
)
