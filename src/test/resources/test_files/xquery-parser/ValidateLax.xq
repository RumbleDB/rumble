(:JIQS: ShouldRun; Output="true" :)
let $validated := validate lax { <unknown><child/></unknown> }
return
  $validated instance of element()
  and data($validated/child) instance of xs:untypedAtomic
