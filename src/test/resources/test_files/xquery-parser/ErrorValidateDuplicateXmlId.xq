(:JIQS: ShouldCrash; ErrorCode="XQDY0027" :)
validate lax {
  document {
    <root>
      <first xml:id="duplicate"/>
      <second xml:id="duplicate"/>
    </root>
  }
}
