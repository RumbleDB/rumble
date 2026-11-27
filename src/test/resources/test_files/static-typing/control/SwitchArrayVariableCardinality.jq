xquery version "3.1";
(:JIQS: ShouldRun; Output="20" :)
(: The array atomizes to 1, so the matching branch binds two items to $v. :)
let $v := switch ([1])
          case 1 return (10, 20)
          default return 0
return $v[2]
