
# Android Testing Rules

## General Principles

- Follow the existing testing conventions in the project.
- Inspect existing tests before proposing new testing patterns.
- Prefer focused tests that verify observable behavior.
- Do not introduce new testing frameworks without approval.
- Do not create tests for unrelated functionality.
- Do not rewrite existing tests without justification.
- Prefer deterministic tests without real network or external service dependencies.

## Unit Testing

- Use the testing libraries already configured in the project.
- Follow existing Mockito or MockK conventions where applicable.
- Do not mix mocking frameworks within a test without justification.
- Prefer testing behavior and results over implementation details.
- Avoid excessive mocking.
- Cover meaningful success, failure, and boundary scenarios.
- Use descriptive test names consistent with the project.

## Coroutines and Flow

- Use kotlinx-coroutines-test when available.
- Control coroutine execution with appropriate test dispatchers.
- Avoid Thread.sleep and arbitrary delays.
- Verify emitted states and values when testing Flow.
- Test cancellation behavior when relevant.
- Ensure tests do not depend on execution timing.

## MVVM Testing

- Test ViewModel behavior through its public interface.
- Verify relevant state changes after user actions.
- Verify interactions with dependencies when meaningful.
- Avoid testing private implementation details.
- Preserve existing ViewModel testing conventions.

## MVI Testing

- Inspect the existing MVI implementation before writing tests.
- Test Reducer state transitions independently when possible.
- Verify emitted Commands and News where applicable.
- Test Actor behavior using controlled dependencies.
- Verify asynchronous results and error handling.
- Do not assume a specific MVI testing pattern without inspecting the code.

## Paging 3 Testing

- Follow existing Paging 3 testing patterns.
- Test relevant data transformations and loading behavior.
- Prefer deterministic PagingData test scenarios.
- Avoid introducing duplicate production state solely to simplify tests.
- Do not require UI tests when unit tests adequately verify the behavior.

## Analytics Testing

- Verify analytics event names and parameters.
- Reuse existing analytics test doubles or mocks.
- Do not send real Firebase Analytics events from unit tests.
- Test analytics integration at the appropriate architectural boundary.
- Avoid coupling business logic tests to Firebase SDK internals.

## Test Execution

- Identify the relevant test task before execution.
- Request approval before running Gradle commands.
- Run focused tests before broader test suites when appropriate.
- Report which tests were actually executed.
- Distinguish passing tests from unverified assumptions.
- Do not modify production code solely to make a failing test pass without explaining the cause.

## When Adding Tests

Before creating new tests:

1. Inspect existing tests and dependencies.
2. Identify the behavior that needs verification.
3. Propose relevant test cases.
4. Explain which files will be created or modified.
5. Wait for approval before implementation.
