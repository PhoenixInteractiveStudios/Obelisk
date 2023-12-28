# Identifiers

Every resource has a universally unique identifier, called _turtle_. This identifier is usually presented in the form of
a decimal number. The only exceptions to this are child entities that directly inherit their ID from their parents.
This system is based on Twitter's [Snowflake ID service](https://github.com/twitter-archive/snowflake) and inherits most
of its advantages.

### Advantages

This ID system does not require a central authority. There may be many nodes in the network that generate new IDs and
while they are not always guaranteed to be unique, the embedded internal service id and the incremental value make it
unlikely to the point that it can be safely ignored. Still, some checks are routinely made to guarantee the integrity
of our databases.

Turtle IDs are also **sortable** and **time ordered**. Since the first 42 bits of any id represent the time it has been
generated, any turtle ID may be sorted into a sorted collection of IDs, regardless of their subject or origin. This
comes as a great advantage for both the backend databases and any applications that are managing large groups of turtle
entities, since they can be traversed via algorithms like binary search.

### Time based IDs and the Turtle Epoch

As mentioned, turtle IDs are mainly time-based. The first 42 bits of any ID represents the exact time in milliseconds
that this specific ID has been generated. Since logically, there cannot be any IDs older than this implementation, we
don't use the expected Unix Epoch. Instead, we use `2023-01-01T00:00:00Z` (or exactly `1672531200574` ms since the
beginning of the Unix Epoch), as our `0`. This will be referred to as the _Turtle Epoch_ within this documentation.

### Turtle ID structure

Breakdown in binary:

```
111111111111111111111111111111111111111111 11111111111111 11111111
^                                          ^              ^       ^
64                                         22             8       0
```

These 3 sections fulfill the following utilities:

| Area     | Bits | Offset                     | Description                                                      |
|----------|------|----------------------------|------------------------------------------------------------------|
| 64 to 23 | 42   | `turtle >> 22`             | Timestamp ([Turtle Epoch](#Time-based-IDs-and-the-Turtle-Epoch)) |
| 22 to 9  | 14   | `(turtle & 0x3FFF00) >> 8` | Internal service id                                              |
| 8 to 0   | 8    | `turtle & 0xFF`            | Increment                                                        |