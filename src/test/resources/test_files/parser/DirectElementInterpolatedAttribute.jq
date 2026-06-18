(:JIQS: ShouldParse :)
declare variable $n as xs:integer external := 10;
<out>{
  for $x allowing empty at $p in 1 to $n
  return <a class="item" position="{$p}" label="item {$p}" literal="{{item}}">{$x}</a>
}</out>
