## How to get the latest schema?

#### Introspection

Once Apollo GraphQL dependency is in place, Gradle will get a special Apollo task for fetching schemas. If your server supports introspection, the task can be run with:

```console
$ ./gradlew downloadApolloSchema \
    --endpoint="https://api.github.com/graphql" \
    --schema="src/commonMain/resources/github/schema.graphqls" \
    --header="Authorization: Bearer <token>" # if needed
```

#### Direct download

The schema from GitHub is also available directly at [their GraphQL docs](https://docs.github.com/public/schema.docs.graphql).

## Queries

GraphQL queries are written in this directory as well, `.graphql` files. From those queries (and using the `.graphqls` schemas) Kotlin files get generated by Apollo and placed in the classpath. The generated files are then used by the `ApolloClient` to execute the queries.
