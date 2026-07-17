# Project Specifications: Java & Web Application SDLC Agent System

## 1. Overview
The goal of this project is to build a suite of GitHub-integrated SDLC (Software Development Life Cycle) agents that automate the development, testing, review, and deployment of Java web applications (utilizing Spring Boot backend frameworks and HTML/CSS/JavaScript frontends).

These agents will interact with human developers and the codebase via GitHub Issues, Pull Requests, and Actions, running in an automated, spec-driven loop.

---

## 2. Pipeline Architecture

The multi-agent system follows a streamlined execution chain where context passes forward from epic creation, to features, user stories, coding, code review, validation/testing, and release:

```
 @00-epic
       │
       ▼ (Handoff: "Create Features")
 @01-feature
       │
       ▼ (Handoff: "Create User Stories")
 @02-user-story
       │
       ▼ (Handoff: "Write Code")
 @03-build
       │
       ▼ (Handoff: "Review Code")
 @04-code-review
       │
       ▼ (Handoff: "Generate Tests")
 @05-test
       │
       ▼ (Handoff: "Deploy & Release")
 @06-release
```

---

## 3. SDLC Agent Personas

### Agent 0: `@00-epic` (`.github/agents/00-epic.agent.md`)
* **Role**: Analyzes high-level goals or business requirements and breaks them down into structured Epics.
* **Output**: Epic definitions, system integrations, and business value documentation.
* **Handoff**: Hands off to `@01-feature`.

### Agent 1: `@01-feature` (`.github/agents/01-feature.agent.md`)
* **Role**: Takes an approved Epic and designs technical system features and designs (specs).
* **Output**: Feature specifications, API REST endpoints, database schemas (DDL), and architectural dependencies.
* **Handoff**: Hands off to `@02-user-story`.

### Agent 2: `@02-user-story` (`.github/agents/02-user-story.agent.md`)
* **Role**: Breaks features down into clear, actionable, and logically separated User Stories (estimated via Fibonacci sequence, maximum of 8 story points per story).
* **Output**: User Story details containing Gherkin BDD Scripts, unit test TDD Scripts, Story Point estimates (Fibonacci series), Acceptance Criteria, and technical design notes.
* **Handoff**: Hands off to `@03-build`.

### Agent 3: `@03-build` (`.github/agents/03-build.agent.md`)
* **Role**: Primary developer agent writing Java backend (controllers, services, repositories) and web frontend pages (HTML, CSS, JS) to fulfill BDD scenarios and TDD scripts.
* **Output**: Generated Java and Web source code (marked as new/existing) and build reports mapping implementation to BDD/TDD specs.
* **Handoff**: Hands off to `@04-code-review`.

### Agent 4: `@04-code-review` (`.github/agents/04-code-review.agent.md`)
* **Role**: Static code analyzer and gatekeeper enforcing security, input validation, resource management, BDD/TDD alignment, and DevSecOps compliance rules.
* **Output**: Code review reports verifying compliance with security guidelines, Gherkin BDD scenarios, TDD script outlines, and Java PMD/SpotBugs/Checkstyle/ESLint static code scanner findings.
* **Handoff**: Hands off to `@05-test`.

### Agent 5: `@05-test` (`.github/agents/05-test.agent.md`)
* **Role**: Quality assurance agent generating robust JUnit 5 tests, Mockito mocks, frontend integration test suites, and performing system regression testing.
* **Output**: JUnit test classes and suites (marked as new/existing) ensuring coverage of all Gherkin BDD scenarios, implementing TDD outlines, and generating a test report documenting regression verification outcomes.
* **Handoff**: Hands off to `@06-release`.

### Agent 6: `@06-release` (`.github/agents/06-release.agent.md`)
* **Role**: Handles validation, packaging (JAR/WAR creation via Maven), and release reporting.
* **Output**: Compilation logs, Git command sequences, package lists, and release notes explicitly listing component statuses (new vs. existing).

---

## 4. Operational Flow in VS Code & GitHub

1. **Epic Analysis**: Invoke the first agent:
   `@00-epic Gathers requirements and creates the Epic definition.`
2. **Feature Design**: Hand off to `@01-feature` to convert the Epic into technical features.
3. **Story Mapping**: Hand off to `@02-user-story` to write individual stories.
4. **Development**: Invoke `@03-build` on a user story to build Java classes and static website pages:
   `@03-build Create a UserController class to return user profiles and an index.html view.`
5. **Code Review**: Hand off to `@04-code-review` to check SQL injection, XSS vulnerability, thread safety, and PMD Java patterns.
6. **Testing**: Hand off to `@05-test` to write JUnit and Mockito test classes to achieve coverage.
7. **Release**: Hand off to `@06-release` to compile, run tests, and package the application via Maven.

---

## 5. Output Location Rules

All agent-generated planning, design, and validation documentation must be stored under folders named after the specific feature or user story identifier:
* **Epics**: `docs/epics/<EPIC-ID>/00_epic_spec.md`
* **Features**: `docs/features/<FEATURE-ID>/01_feature_design.md`
* **User Stories**: `docs/features/<FEATURE-ID>/user-stories/<STORY-ID>/02_story_details.md` and build details as `03_build_details.md`.
* **Execution Logs & Reports**: Store review reports as `04_review_report.md`, test logs as `05_test_report.md`, and deployment logs as `06_deployment_report.md` in `docs/features/<FEATURE-ID>/user-stories/<STORY-ID>/logs/`.
* **Application Source Code**: Keep actual Java (`.java`), configurations, HTML templates, and static resources (`.html`, `.css`, `.js`) in the Maven structure under `src/main/` and test source code under `src/test/`, mapping and documenting their modifications in the corresponding User Story folder.
* **Continuous Documentation**: Global business flows and technical reference maps must be updated concurrently by agents:
  - **Functional Reference**: Kept up-to-date in `docs/functional/` folder (e.g., business rules, flow charts, user manuals).
  - **Technical Reference**: Kept up-to-date in `docs/technical/` folder (e.g., entity relationship diagrams, Java API endpoints, component specs, database schema scripts).

---

## 6. Product Specifications & Features

### Feature F03: Clinic Billing & Invoicing
* **Domain Entity**: `org.springframework.samples.petclinic.model.Invoice`
* **Repository**: `org.springframework.samples.petclinic.repository.InvoiceRepository`
* **Controllers**: `org.springframework.samples.petclinic.web.InvoiceController`
* **DDL Table**: `invoices` (id, owner_id, visit_id, appointment_id, amount, issue_date, due_date, payment_status, payment_date, description)
* **REST Endpoints**:
  - `GET /owners/{ownerId}/invoices`
  - `GET /owners/{ownerId}/invoices/{invoiceId}`
  - `POST /owners/{ownerId}/invoices/{invoiceId}/pay`
  - `GET /admin/invoices`
  - `POST /admin/invoices/new`
* **JSP Templates**:
  - `invoices/invoiceList.jsp`
  - `invoices/invoiceDetails.jsp`
  - `invoices/paymentForm.jsp`
  - `invoices/invoiceForm.jsp`
* **Security & Architectural Constraints**: SQL injection prevention using parameterized queries, dynamic inputs sanitization for XSS prevention, double payment verification, and transactional context mapping.

