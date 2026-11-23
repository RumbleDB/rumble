(:JIQS: ShouldRun; Output="42" :)
import module namespace answer = "urn:inferred-answer" at "answer.xqm";
answer:value() is statically xs:integer
