xquery version "3.1";
(:JIQS: ShouldRun; Output="20" :)
(: Float and double values can match, so $v must retain the matching branch's cardinality. :)
let $v := switch (xs:float(1))
          case xs:double(1) return (10, 20)
          default return 0
return $v[2]
