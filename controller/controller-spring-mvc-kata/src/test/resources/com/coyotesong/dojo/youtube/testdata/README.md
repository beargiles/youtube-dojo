# Test Data Notes

This directory contains the information used to create the objects used
in integration tests.

## Data-Driven Approach

The unit tests use a data-driven approach. To the greatest extent possible
the tests will make no assumptions about data. All they should care
about is obtaining the `input`, `expected`, and `expectedException` values
and comparing the latter to the actual behavior.

"Convention over Configuration (or Coding)" can go a long way here. For instant the
test object can contain the test method's name, and a custom
classes for`@RunWith`(Junit 4) and `@ExtendWith` (Junit 5). A good
candidate for this is the
[ExternalResourceSupport](https://junit.org/junit5/docs/current/api/org.junit.jupiter.migrationsupport/org/junit/jupiter/migrationsupport/rules/ExternalResourceSupport.html)
extension that uses reflection to set a field property given the test's
class and method name.

## Database-Driven Approach

A data-driven approach raises a big question - where does the
test data come from?

The fastest approach is a file in the classpath. See this directory.

A more flexible approach is using an external database. This can
integrate with issue tracking software and does not require constant
modification of the source code.

If an external database isn't an option you could use a TestContainer
containing a database that's initialized with test data via Flyway
or a similar tool.

Another option is to use the jOOQ library's
[MockDataProvider](https://www.jooq.org/javadoc/latest/org.jooq/org/jooq/tools/jdbc/MockDataProvider.html)
interface. One option i
[MockFileDatabase](https://www.jooq.org/javadoc/latest/org.jooq/org/jooq/tools/jdbc/MockFileDatabase.html).
However since you only need to load the`input` and `expected` values
you'll only need to support `select` statements and the following methods.
The actual data can be data loaded from the JSON files used in the
initial approach.

- [Statement.executeQuery(string)](https://docs.oracle.com/en/java/javase/20/docs/api/java.sql/java/sql/Statement.html#executeQuery(java.lang.String))
- [PreparedStatement.executeQuery()](https://docs.oracle.com/en/java/javase/20/docs/api/java.sql/java/sql/PreparedStatement.html#executeQuery())

