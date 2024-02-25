# YouTube Service Test Notes

Despite the (current) class names these are functional/integration tests, not
unit tests, and require both access to the YouTube server and valid credentials.

## Preconditions and Postconditions

Unit tests will always produce hard failures - the code works or it doesn't.
New hard failures should trigger immediate reviews while all recent changes
are fresh in everyone's minds. Hard failures are handled with the `assert*` methods.

Integration tests are more complex since they require external resources.
A test should not be marked as failing if that external resource is unavailable.

This is typically handled writing our test methods in three blocks:

- preconditions (using `assumeThat`)
- the test
- postconditions (using `assertThat`)

A failing precondition will not mark the test as either passing or failing.

## Authentication Properties

The authentication properties are currently loaded from the default `YouTubeContext`
class. We should support a test-specific mechanism so we can verify the proper
handling of authentication failures.

Authentication failures are currently treated as hard failures.

## Quotas

YouTube developer accounts have a daily quota. The service methods will throw
a `YouTubeQuotaExceededException` if the requests failed because we've exceeded
it. This should be considered a soft failure.

AFAIK there's no way to determine that we've hit our quota prior to making
one of the currently supported REST calls, and any test call would burn into
our daily quota. That's why the tests use a try/catch block and `assumeThat(false)`
instead of the traditional approach. 