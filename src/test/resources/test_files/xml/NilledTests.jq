(:JIQS: ShouldRun; Output="(false, false, false, false, false, false, true, true, true, true)" :)
fn:nilled(<root/>),
fn:nilled(<root size="5"/>),
fn:nilled(<root xsi:nil="true"/>),
fn:nilled(<root xsi:nil="false"/>),
(<root/>)/fn:nilled(),
(fn:nilled#1)(<root/>),
empty(fn:nilled(document { <root/> })),
empty(fn:nilled(<root attr="val"/>/@attr)),
empty(fn:nilled(<root>text</root>/text())),
empty(fn:nilled(()))
