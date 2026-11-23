# Advanced Spring API Contract

The published contract of the Advanced Spring project: the types that cross the HTTP boundary, the
operations that carry them, and the OpenAPI document generated from both. It holds no business logic
and no persistence, and it is versioned and released independently of the service that implements it.

The service lives in a separate repository, [advanced-spring-multimodule](https://github.com/KamenIvanov/advanced-spring-multimodule),
and depends on this one. The split exists so that a consumer can be given something to build against
before any endpoint answers a request: the jar for Java callers, the generated OpenAPI document for
everyone else. Neither artifact requires the service to be running, or even to be written.

Both repositories are the codebase behind the **Evolutionary Architecture** series, where every
decision here is argued in the chapter that introduced it.

## Modules

| Module | What it holds |
| --- | --- |
| `dto` | The request and response types, with the three abstract bases that decide which fields a type has. Depends on Jackson and Swagger annotations only. |
| `rest-api` | The operations, as Spring-annotated interfaces, plus the JSON mapper the service and its clients share. |
| `events` | Reserved for the asynchronous payloads. Declared but not yet populated. |
| `openapi` | A documentation-only Spring Boot application and the build that reads an OpenAPI document out of it. Neither installed nor deployed as a jar. |

## Requirements

JDK 25 and Maven 3.9. Both are enforced by the build rather than assumed.

## Building

```
mvn clean install
```

This produces `dto`, `events` and `rest-api` in the local repository. The `openapi` module is built
but deliberately not installed, since nothing should ever depend on it.

## Generating the documentation

```
mvn clean verify -pl openapi -am
```

The `openapi` module starts the documentation application on a port reserved at build time, reads
`/v3/api-docs` and `/v3/api-docs.yaml` from it, stops it again, and writes a complete Swagger UI
directory to `openapi/target/swagger-ui`. Opening `index.html` from that directory needs no server
and no network.

The integration tests then assert the generated document against the claims this contract makes,
including that the inherited CRUD types resolved to real schemas, that every decimal is a string,
that the search parameters are flattened into query parameters, and that `servers[0].url` is not the
build server's random port.

## Publishing

`mvn deploy` sends the three jars to the configured Maven repository and uploads the Swagger UI
directory to a raw repository under `${documentation.path}/${project.version}`, so that each
contract version keeps its own browsable copy of its documentation.

The repository URLs and identifiers in the POMs are placeholders. Replace them, or set `wagon.skip`
to `true` to build without the upload.

## Conventions worth knowing before changing anything

- **The HTTP mapping belongs to the interface.** Spring reads `@RequestMapping` and the parameter
  annotations from a controller interface, and splitting them between the interface and its
  implementation is the documented way to break it. The paths themselves are constants in
  `RestUrl`, so a path is written once.
- **Money is a string on the wire.** `BigDecimal` is serialised with `ToStringSerializer`, so no
  client parses a price into a double. The generated schema is kept in step by one global mapping in
  the documentation application rather than by an annotation on every field.
- **Instants have one format.** `ApiInstantFormat` pins it to `yyyy-MM-dd'T'HH:mm:ss.SSS'Z'` in UTC,
  for both directions.
- **No Java type names on the wire.** The REST types carry no `@JsonTypeInfo`, so a class name can
  never reach a client or a generated schema.
- **Headers are described, not passed.** `If-Match` is declared on the two operations it applies to.
  The caller's identification applies to the whole secured surface, so it is added by a customizer
  to every contract implementing `SecuredRestService` instead of being repeated on each operation.
- **A missing mapping fails the build.** The documentation application refuses to document an
  operation with no HTTP mapping, and refuses to produce an empty document when it finds no
  contracts at all. An absent operation leaves no trace in a generated file, so neither is allowed
  to pass quietly.

## Tags

Each chapter of the series is a tag, so any state the articles describe can be checked out as it was.

| Tag | Chapter |
| --- | --- |
| `chapter-15-api-contract` | 15 — the types, the mapper and the repository split |
| `chapter-16-openapi` | 16 — the generated OpenAPI document and the Swagger UI |

## License

MIT. See [LICENSE](LICENSE).