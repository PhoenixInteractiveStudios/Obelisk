# Data Types
Throughout this documentation there are many tables describing the structure of objects. These are the data types that
are mentioned in these descriptions:

| Data Type  | Description                                                                                                   |
|------------|---------------------------------------------------------------------------------------------------------------|
| turtle     | A unique entity id                                                                                            |
| string     | A UTF-8 encoded string                                                                                        |
| bool       | A boolean; `true` or `false`                                                                                  |
| int32      | 32-bit integer                                                                                                |
| int64      | 64-bit integer                                                                                                |
| float32    | 32-bit floating-point number                                                                                  |
| float64    | 64-bit floating-point number                                                                                  |
| timestamp  | [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601) timestamp                                                  |
| snowflake  | A unique discord entity id (see [Discord API docs](https://discord.com/developers/docs/reference#snowflakes)) |
| UUID       | A [universally unique identifier](https://en.wikipedia.org/wiki/Universally_unique_identifier)                |
| array of X | Zero or more (unless specified otherwise in the field description) elements of X                              |
| one of X   | Meaning X is an enumeration that should be linked in the docs                                                 |

#### Optional Fields
If a field is prefixed by a `?` it means that the field is optional.

#### Nullable Data Types
If a data type is suffixed by a `?` it means that the value of that field may be `null`.