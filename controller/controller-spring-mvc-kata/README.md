# Controller Kata: Spring MVC

This is a controller implementation using Spring MVC.

Important: this module should only depend upon `model`. It should not depend on
any service implementation since that will break the strict isolation we're trying
to achieve.

For the same reason this module should not explicitly depend upon a specific
implementation of the view. In fact the initial implementation doesn't provide
any content - it's all located in a resource-only 'thymeleaf-kata' module.

Demonstrated functionality
-

This module demonstrates

- a REST API implemented using Spring MVC
- an HTML website implemented using Spring MVC
- internationalization

The website content is located in the `thymeleaf-bootstrap-kata` module.

Libraries used
-

- Spring MVC
- Thymeleaf
