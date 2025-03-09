# Pulldown Menu Content Notes

## Internationalization

These files contain the pulldown menu contents.

Note: there's a huge problem with traditional
[ResourceBundle](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ResourceBundle.html)s
despite being intended for I18N support - they do not support UTF-8 encoding.
This is a very odd oversight since it breaks many languages - see the
`langOptions.properties` file for an example.

Nevertheless the
[ResourceBundleMessageSource](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/support/ResourceBundleMessageSource.html)
is an excellent starting point since the hardest step in I18N and L10N is
getting started. If the library/framework is used properly then correctly
implementing one language will ensure that all languages work. (With the
possible exception of LTR vs RTL.)

A future kata will address this with a custom
[MessageSource](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html).
It will use a (cached) database for storage.

Another future kata will take advantage of YouTube's `i18nLanguages.List`
and `i18nRegions.List` API calls. They can be loaded on launch (see
`StartupController`) and used in a separate MessageSource from above.
There use will be much more limited - probably little more than to
populate the appropriate pulldown menus.

## Usage

On the implementation front... it is possible to explicitly add
the pulldown menu options to every HTML page where they're needed.
However this adds a bit of complexity and creates larger files.

A better approach is to use a bit of dynamic HTML that makes an
AJAX call for the contents of these menus and then modifies the
contents of the DOM.

This is faster since the rendered page is smaller and the browser
will (hopefully) cache the contents of the REST call. I need to
research the last point though since it's probably not enabled
by default.
