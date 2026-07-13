# Agent Operating Guide (Java & Web Application Edition)

These instructions help turn human prompts into reliable, repeatable Java Backend and Website delivery systems.

AI can guess.
This system is designed to behave.

---

# How the Agent Is Structured

The system has three layers:

## How this works (simple)

– **Instructions** = what we want to happen (in `.github/agents/`)
– **Decision** = pick the right Java class, API endpoint, or static web asset to modify or build
– **Actions** = the real work (Java and Web sources in `src/main/` and `src/test/`)

The agent can plan, but it must execute by creating and compiling code using Maven. No one-off code.

---

# File Structure

– `.github/` ➔ GitHub Specific Configurations
  – `workflows/` ➔ CI/CD workflows and automated agent pipelines
  – `copilot-instructions.md` ➔ Global repository instructions parsed natively by GitHub Copilot
– `src/main/java/` ➔ Java Backend Source Code (controllers, services, models, repositories)
– `src/main/resources/` ➔ Application config, templates, and static resources
  – `static/` ➔ Website frontend assets (HTML, CSS, JS, images, fonts)
  – `templates/` ➔ HTML templates (e.g. Thymeleaf views)
  – `application.properties` or `application.yml` ➔ Configuration settings
– `src/test/java/` ➔ Java JUnit and integration unit tests
– `pom.xml` ➔ Maven Project Object Model configuration
– `docs/` ➔ All Agent planning, specifications, designs, and execution logs
  – `functional/` ➔ Continuous functional specs, process flows, and business capabilities
  – `technical/` ➔ Continuous technical designs, API specs, database schemas
  – `epics/` ➔ Epic folders containing high-level specs (e.g., `docs/epics/EPIC-100/`)
  – `features/` ➔ Feature folders containing tech specs (e.g., `docs/features/F01/01_feature_design.md`)
    – `user-stories/` ➔ User story folders (e.g., `docs/features/F01/user-stories/US01/`) containing story specs (`02_story_details.md`), build details (`03_build_details.md`), reviews (`04_review_report.md`), test reports (`05_test_report.md`), and deployment reports (`06_deployment_report.md`)
– `.tmp/` ➔ Temporary log files and compilation/test verification results
– `project_specs.md` ➔ Full project definition

---

# Step 1: Define the Project First

Before writing any Java or web code, you must:

1. Create/update the file `project_specs.md`
2. Clearly define:
   — What Java classes, REST controllers, databases, or frontend files are being added/modified
   — What Spring APIs, HTTP requests, or query mappings are needed
   — UI wireframes, CSS style guides, or web pages
   — Expected user interactions and system behaviors
   — Core validation rules, database transaction rules, and security checks (e.g. SQL Injection prevention, XSS protection, CSRF)
   — Testing strategy (JUnit 5 tests, Mockito mocks, Integration/Integration slice tests)
   — Target deployment package (JAR, WAR, Docker container)
   — What “done” looks like
3. Show the file
4. Wait for approval

No code should be written before this file is approved.

---

# Development Rules

## Rule 1: Always Read First
Always read:
– `instructions.md`
– `project_specs.md`

Before taking action.

---

## Rule 2: Java & Web Frontends Only
All code must be written in Java (for backend controllers, services, database models, and repository interfaces) and HTML/CSS/JavaScript (for website frontend assets, scripts, and views).

---

## Rule 3: Strict Testing & Coverage
Every Java source class containing business logic must have a corresponding JUnit test class in `src/test/java/`.
- Target at least 80% unit test coverage.
- Tests must include robust assertions using JUnit 5 `Assertions` (e.g., `assertEquals()`, `assertTrue()`, `assertThrows()`).
- All tests must use Mockito for isolating unit dependencies. Avoid sharing mutable states or databases in unit tests.
- Run tests via Maven (`mvn test`) before any packaging validation.

---

## Rule 4: Java & Web Best Practices
- **SQL Injection Prevention**: Never concatenate variables into SQL strings. Always use parameterized queries, PreparedStatement, or Spring Data JPA parameter bindings.
- **XSS Prevention**: Clean and sanitize all user input before outputting to HTML. Utilize Thymeleaf dynamic attributes (`th:text` automatically escapes HTML) or frontend encoders.
- **Resource Management**: Always use try-with-resources when handling streams, database connections, or HTTP sockets to prevent memory leaks.
- **Security Check**: Enforce authentication and role-based checks on REST controllers. Protect sensitive HTTP operations from CSRF.
- **N+1 Query Avoidance**: When fetching object structures, use JOIN FETCH queries or entity graphs rather than lazy loading loops.

---

## Rule 5: Deployment & Packaging Checklist
Before releasing or packaging:

1. Run Maven compiler and tests (`mvn clean test`) to ensure all classes compile and pass.
2. Verify application configuration properties and dependency bindings are defined.
3. Show the Maven package command (e.g., `mvn clean package`).
4. Wait for approval.
5. Compile and package (generate JAR/WAR in `target/` directory).
6. Verify deployment status and run manual sanity checks of the website in the local/development server.
7. **Release Notes Documentation**: Ensure the generated `release_notes_<timestamp>.md` details the exact pre-deployment and post-deployment manual actions (e.g., environment variables, SQL schema migrations, config changes) that the operations team must perform.
8. **Package Manifest**: Ensure a list of compiled artifacts (JAR paths, library version updates) is created under the parent Feature directory, capturing all components included in the release.
9. **Git Command List**: Ensure a standard `git_commands_<timestamp>.md` file is created under the parent Feature directory, providing the exact Git sequences needed to stage, commit, and push changes to the local and remote feature branch.

---

## Rule 6: Strictly Limit Scope to User Requests
- Never execute pipeline actions, write code, run code reviews, or generate documentation/log files for adjacent user stories, features, or tasks unless explicitly directed to do so by the user.
- If the user commands work on a localized target (e.g., executing testing only for US01), strictly restrict all modifications, test classes, execution logs, and reports to that target user story folder. Do not preemptively expand the scope.
- **Do not proceed with subsequent agent execution phases (e.g., User Story generation, Build, Code Review, Test, Release) or generate their respective files/code without explicit instruction from the user. The agent must only draft/show the features (Feature Design Specification) and stop.**

---

## Rule 7: End-to-End Traceability (Epic to Release)
- Ensure complete traceability from Epic specifications down to the Release package and release notes.
- Every User Story must link back to its parent Feature Design Spec (`01_feature_design.md`), and the Feature Spec must link back to its parent Epic (`00_epic_spec.md`).
- Test reports (`05_test_report.md`) and deployment reports (`06_deployment_report.md`) must refer directly to the User Story ID and parent Feature ID.

---

## Rule 8: Pipeline Numbering Prefix on Files
All documentation and log files generated by the agents must be prefixed with the phase number of the agent that produced them:
- `00_epic_spec.md` (Product / Epic)
- `01_feature_design.md` (Technical / Feature)
- `02_story_details.md` (User Story)
- `03_build_details.md` (Construction / Build)
- `04_review_report.md` (Code Review)
- `05_test_report.md` (Quality / Test)
- `06_deployment_report.md` (Release)
- Package manifest (represented by `mvn` target artifact or dependency checklist), Release Notes `release_notes_<timestamp>.md`, and Git deployment list `git_commands_<timestamp>.md` directly under the parent Feature folder (Release)

## Rule 9: Fibonacci Estimation & Logical Story Split
- Every User Story must have a Story Point (SP) estimate following the Fibonacci sequence (1, 2, 3, 5, 8).
- The maximum estimate for any single user story is **8 story points**.
- If a story's estimated effort exceeds 8 story points, you must logically separate it into smaller, independent user stories (e.g. separating backend API endpoints from frontend Views, or splitting by user roles/flows) unless it is technically impossible.

---


# When Something Breaks

1. Fix the Java compilation error or web server startup issue.
2. Improve unit tests to catch the edge case so it doesn't fail the same way again.
3. Re-run tests via `mvn test`.
4. Update instructions if a platform configuration issue or runtime library exception was encountered.

Errors are feedback.
Each fix should make the system stronger.

---

# Response Format

When replying, always use:

**Plan** (3-7 bullet points)
**What I need from you** (if anything)
**Next action** (one clear step)
**Errors** (explained simply)

---

# Core Principle

Define clearly.
Build in small steps.
Test before moving on.

Reliable systems are built intentionally.

---

# Quick Reference

## Directory & File Map
* **Project Specs**: [project_specs.md](project_specs.md) (Define before writing code!)
* **Workflow Logic**: `.github/agents/` (Markdown files describing agent rules)
* **Agent Outputs/Docs**: `docs/` (Planning, specifications, designs)
  * **Epics**: `docs/epics/<EPIC-ID>/` (e.g. `docs/epics/EPIC-101/`)
  * **Features**: `docs/features/<FEATURE-ID>/` (e.g. `docs/features/F01/`)
  * **User Stories**: `docs/features/<FEATURE-ID>/user-stories/<STORY-ID>/` (e.g. `docs/features/F01/user-stories/US01/`)
* **Functional Docs**: `docs/functional/` (Continuous business and functional indexes)
* **Technical Docs**: `docs/technical/` (Continuous code references and APIs)

* **GitHub Configs**: `.github/` (Workflows and Copilot native instructions)
* **Maven POM**: `pom.xml` (Dependencies and compiler rules)
* **Java Classes**: `src/main/java/` (Java backend classes)
* **Static Assets**: `src/main/resources/static/` (HTML, CSS, JS frontend assets)
* **Dynamic Views**: `src/main/resources/templates/` (Thymeleaf/web template files)
* **Unit Tests**: `src/test/java/` (JUnit and Mockito tests)
* **Local Temp Data**: `.tmp/` (For temporary files and logs)

## Workflow Checklist
1. **Define**: Specify the new workflow in [project_specs.md](project_specs.md) and get approval.
2. **Write Instructions**: Create/Update agent scripts in `.github/agents/`.
3. **Write Code**: Create Java classes and HTML files in `src/main/`.
4. **Local Test**: Run unit tests via Maven (`mvn test`) and verify web UI locally.
5. **CLI Package**: Show compile command, package the app, get approval, and deploy.

## Standard Response Format
Always include these four parts in replies:
1. **Plan** (3-7 bullet points of planned steps)
2. **What I need from you** (questions or inputs needed)
3. **Next action** (exactly one clear next step)
4. **Errors** (simple explanation of any issues)
