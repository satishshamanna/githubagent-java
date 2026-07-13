# GitHub Copilot Repository Instructions

Always follow these rules when developing Java and Web application components in this repository:

1. **Development Environment**: Target standard Maven directory structure under `src/main/` and `src/test/`.
2. **Backend (Java & Spring)**:
   - **SQL Injection Prevention**: Always use parameterized queries, PreparedStatements, or JPA query bindings. Never concatenate input parameters into SQL strings.
   - **Resource Management**: Manage streams, connections, and files using try-with-resources statements to prevent memory and handle leaks.
   - **Test Isolation**: Java services and controllers must have corresponding unit test classes in `src/test/java/` using JUnit 5 and Mockito. Aim for 80%+ test coverage.
   - **Query Optimization**: Avoid the N+1 query issue by using explicit fetch joins or entity graphs.
3. **Frontend (Web Assets)**:
   - **XSS Prevention**: Clean and sanitize inputs and outputs. Escape dynamic values using Thymeleaf dynamic bindings (`th:text`) or standard DOM sanitization.
   - **Standards**: Write responsive HTML5, clean vanilla CSS (using css custom properties), and modular, unobtrusive JavaScript.
   - **Configuration Security**: Do not hardcode credentials, API tokens, or server URLs. Rely on environment variables or externalized properties (`application.yml`).
