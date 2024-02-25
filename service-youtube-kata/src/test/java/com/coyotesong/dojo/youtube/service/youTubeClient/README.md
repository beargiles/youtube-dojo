# YouTube REST Client Test Notes

The actual execution is tested in the `com.coyotesong.dojo.youtube.service` classes
at the moment. In an ideal world those tests would use a mocked YouTubeClient while
the actual integration tests are performed here but that's not the case for now since
doing it properly would take some time without much benefit.

This package contains tests for the `convert` methods used to convert YouTube API
objects to our objects.

TODO: implement remaining tests
