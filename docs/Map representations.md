# Map representations

All general maps use the Capsule CHAMP implementation in `PersistentMapItem`.
There are two ways to hold that storage:

| Item                | Purpose                                                                                                                                            |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| `PersistentMapItem` | Immutable map value. Functional updates share unchanged trie nodes.                                                                                |
| `MutableMapItem`    | Mutable identity and update metadata around a persistent root. In-place updates replace the root; previously returned map values remain unchanged. |

The former `MapItem` implementation, with parallel key/value lists, a hash index,
and separate object-shape caches, has been removed. Mutable map snapshots now
reuse their persistent root instead of copying every entry for each independent
functional update. Both forms use the same key equivalence, duplicate detection,
object-shape tracking, lookup, and logical-entry serialization.

Two object specializations remain:

- `ObjectItem` preserves object field order, restricts fields to string keys and
  singleton values, reports the JSONiq object dynamic type, and carries update
  target metadata. Its constructors can wrap multiple values in arrays. Directly
  substituting a generic map would change these behaviors. The map's mutability
  flag alone does not provide these object contracts.
- `LazyObjectItem` defers evaluation of fields. The binary classification metrics
  function uses it so accessing a scalar metric does not compute all the Spark
  curves. Converting it to an eager trie would change evaluation and cost.

`AnnotatedItem` is a type annotation wrapper that delegates to its contained Item;
it does not implement independent map storage. `AbstractMapItem` supplies shared
structural equality/hashCode, rather than another backing representation.

Removing the object classes would require ordered-object and lazy-field adapters,
plus measurements for small JSON objects and object lookup/serialization. Keeping
these adapters would still leave distinct behaviors even if they shared trie
storage. The current consolidation deliberately preserves object ordering,
dynamic types, laziness, and update semantics.
