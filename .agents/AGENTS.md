# Agent Response Format Rules

Always follow the required response format in all replies. Every single response must end with or be structured using these four sections:

## Plan
- [Planned step 1]
- [Planned step 2]
- ... (between 3 to 7 bullet points in total)

## What I need from you
- [Questions, feedback, or approvals needed, or "None" if nothing is needed]

## Next action
- [Exactly one clear next step]

## Errors
- [Simple explanation of any errors or issues encountered, or "None" if no errors occurred]

# Commenting and Documentation Standards
For any source code file changes or new source code file creations (e.g., Java, XML, SQL, JSP files), you MUST include a Javadoc header at the top section containing the following metadata. Do NOT include this Javadoc header block in markdown reports or planning files (e.g., .md files):
- **Version Number**: `1.0.0` (or appropriate version increment)
- **User Story Number**: `US01`
- **Date & Time of Change**: `2026-07-13T07:57:32+05:30` (or actual change time)
- **User Name**: The logged-in username of the session executing the Agent (retrieved dynamically via git config user.name or system username)
- **Brief Description of Change**: A concise summary of changes made.

# Code Review, Test, and Release Report Standards
For any Code Review (`04_review_report.md`), Test (`05_test_report.md`), or Release/Deployment (`06_deployment_report.md`) reports generated:
1. **Top Section Verdict**: The report must contain a final verdict section at the top.
   - If it passes: Display `🟢 PASS` (Green round icon).
   - If it fails: Display `🔴 FAIL` (Red round icon) accompanied by markdown links within the document pointing directly to the failed checklist items or test cases.
2. **Iteration Tracking**: The report must support multiple execution iterations by organizing findings under dated run headings (newest runs placed at the top of the document, below the Javadoc header but above older runs) or maintaining a clear iteration status table.


