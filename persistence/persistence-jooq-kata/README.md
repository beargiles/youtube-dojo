Persistence Kata: jOOQ
=

jOOQ is a popular alternative to JPA, Spring Data et al. The main benefits are that

- jOOQ allows the developer to use the full power of SQL
- jOOQ 'fills in' missing functionality, e.g., `merge`
- jOOQ allows the transparent use of custom java class <-> sql type conversion
- jOOQ is database agnostic

On the other hand jOOQ does require a bit more work than JPA annotations.

Demonstrated functionality
-

This kata:

- implements the standard persistence mechanisms
- uses 'flyway' for database schema management
- uses [TestContainers](https://testcontainers.org/) for functional tests
- uses Spring Boot `@Testcontainer` and `@ServiceConnection`

The standard TestContainer is extended to initialize the database via flyway.

Work-in-progress limitations
-

This kata explicitly maintains cross-reference tables. I'm sure that jOOQ can
handle this - but for now I'm doing it manually for a bit more experience.

Libraries used
-

- spring framework
- jOOQ
- flyway
- testcontainers

Using the jOOQ maven plugin for code generation
-

This project uses jOOQ maven plugin for code generation. The "single source of truth"
is the 'flyway' scripts in `src/resources/db/migration`. (**Not** the 'model' classes!).

Usage:

`$ mvn -Pcodegen generate-sources`

Open questions
-

**Loading initial data using `copy` and flyway**

It is best practice to load initial data from an external file instead of explicit
`insert` or inline `copy` statements in the flyway scripts. This reduces the risk
of introducing transcription errors.

This approach works great with TestContainers. (We simply copy the external files
from our test classpath to the docker container.)

However this approach can easily fail when running the application since the path
to the external files will be different. We need to explicitly copy these files
to the same location on the host computer. This isn't hard - I'm sure there's a
maven plugin that can copy the files - but might violate security policy if the
initial data contains sensitive information.
