(:JIQS: ShouldRun; Output="(true, true, true)" :)
duration("P20M") eq duration("P1Y8M"),
duration(()) ne duration("PT0S"),
duration(()) eq duration("PT0S"),
duration("P0Y0M0D") eq duration("PT0S"),
duration("P0Y0M0D") ne null

(: general tests :)