# Notes

I think this is a great start with the `DependencyTest` it guarantees that the lines of the architecture are respected, right Cockburn?
https://alistair.cockburn.us/hexagonal-architecture/

## Todo

Not in any order.

- Select another domain as example as the current one is confusing.
- Upgrade to the latest version of Spring Boot.
- Use in-memory H2 database for development and test.
- Have exception handling that converts to HTTP status code.
- Make API be documented with Open API.
- Create a GUI that uses a port.
- Let logging go through a port.
- Write a ReadMe.md.
- Let `DependencyTest` autoconfigure the root package by reflection on itself.
- Refactor `DependencyTest` for better readability.
