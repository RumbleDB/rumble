(:JIQS: ShouldRun; Output="(true, true)" :)
let $e1 := <a attr1 = "abc1" xmlns="http://example.org/nametest-19"/>
let $e2 := <a attr1 = "abc1"/>
return (
  exists($e1/self::Q{http://example.org/nametest-19}*),
  exists($e2/self::Q{}*)
)
