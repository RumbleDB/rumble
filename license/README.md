# Third-party license inventory

`../LICENSE.txt` identifies RumbleDB's license, its direct dependencies, the
Apache Xerces upstream project, and the Evolved Binary Xerces distribution.
`THIRD-PARTY.md` lists the resolved direct and transitive Maven dependencies,
including classifiers and compile/runtime, provided, and test scopes.
Build plugins are build tooling and are outside this project-dependency inventory.

`THIRD-PARTY-NOTICES.txt` preserves legal files found inside the resolved JARs,
including notices for libraries shaded into those JARs. Identical files are
deduplicated with every original artifact and path retained. The inventory uses
SHA-256 IDs to link to these texts. Existing standalone license files are retained
for source/data attributions and supplementary terms.

The inventory records the license declarations supplied by upstream projects;
multiple declared licenses are preserved without choosing an alternative.
The four corrections in `third-party-overrides.properties` come from the actual
released JARs, whose license files are included in the collected notices. In
particular, Jakarta ORO 2.0.8 declares **Apache Software License 1.1**, not Apache 2.0.

## Refresh and verify

From the repository root:

```sh
python3 tools/update-third-party-licenses.py
python3 tools/update-third-party-licenses.py --check
```

The script resolves dependencies through Maven, reads inherited POM license
declarations with the MojoHaus license plugin, updates the direct-dependency
section of `LICENSE.txt`, and collects legal files from the exact resolved JARs.
It fails if a resolved artifact has no license declaration. Investigate such a
dependency's published terms before adding a documented override.

The checked-in notices and `LICENSE.txt` are packaged under
`META-INF/rumbledb/` in both the ordinary JAR and the executable assembly.

## Sources and distribution records

For any listed Maven coordinate, its published POM, binary, and source artifacts
are available under `https://repo.maven.apache.org/maven2/`: replace dots in the
group ID with slashes, then append the artifact ID and version. A `sources`
classifier is a publication record, not proof that matching source is present.
Consult the project's upstream source repository when a source archive is absent.

Specific new components:

- [Evolved Binary Xerces 2.12.2.2 distribution](https://repo.maven.apache.org/maven2/com/evolvedbinary/thirdparty/xerces/xercesImpl/2.12.2.2/).
  RumbleDB uses `jdk14-xml-schema-1.1`; matching source is published as
  `jdk14-xml-schema-1.1-sources`. Its Apache license and NOTICE are preserved
  verbatim in `LICENSE-Xerces.txt` and `NOTICE-Xerces.txt`.
- [Evolved Binary Eclipse WST XPath2 1.2.1.1 distribution](https://repo.maven.apache.org/maven2/com/evolvedbinary/thirdparty/org/eclipse/wst/xml/xpath2/1.2.1.1/).
  Its POM declares EPL 1.0, whose terms are in `LICENSE-Eclipse-XPath2.txt`.
  **Upstream source availability gap:** the published `sources.jar` contains only
  a README stating that the matching PsychoPath source seems unavailable. It
  points to the [Xerces project](https://xerces.apache.org/xerces2-j/) and
  [PsychoPath project](https://wiki.eclipse.org/PsychoPathXPathProcessor).
  We have not verified a corresponding source tree for this exact binary.
  The inventory and included license terms do not resolve that upstream gap.
- [Java CUP 10k distribution](https://repo.maven.apache.org/maven2/edu/princeton/cup/java-cup/10k/)
  and [upstream source and license](https://www.cs.princeton.edu/~appel/modern/java/CUP/).
  `LICENSE-Java-CUP.txt` reproduces its copyright, permission, and disclaimer.

The inventory covers the Maven dependency graph. It does not independently
identify every component within shaded binaries; their publishers' bundled
license and NOTICE files are preserved for that purpose.
