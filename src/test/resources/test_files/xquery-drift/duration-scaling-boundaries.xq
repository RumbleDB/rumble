(:JIQS: ShouldRun; Output="true":)
deep-equal(
  (
    xs:dayTimeDuration("P200000D") * 1,
    xs:dayTimeDuration("P200000D") div 2,
    xs:dayTimeDuration("-P200000D") * 0.5,
    xs:dayTimeDuration("-PT0.5S") * 3,
    xs:dayTimeDuration("PT0.000000001S") div 2,
    xs:dayTimeDuration("P200000D") * -0e0,
    xs:dayTimeDuration("P200000D") div xs:double("INF")
  ),
  (
    xs:dayTimeDuration("P200000D"),
    xs:dayTimeDuration("P100000D"),
    xs:dayTimeDuration("-P100000D"),
    xs:dayTimeDuration("-PT1.5S"),
    xs:dayTimeDuration("PT0S"),
    xs:dayTimeDuration("PT0S"),
    xs:dayTimeDuration("PT0S")
  )
)
