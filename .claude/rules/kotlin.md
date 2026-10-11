
# Kotlin Development Rules

## General

- Follow the Kotlin conventions already established in the project.
- Prefer simple, readable, and maintainable code.
- Use idiomatic Kotlin without unnecessary complexity.
- Preserve existing public APIs unless a change is approved.
- Avoid introducing abstractions for hypothetical future requirements.
- Do not refactor unrelated code.

## Null Safety

- Prefer safe calls, Elvis operators, and explicit null handling.
- Avoid the non-null assertion operator (!!).
- Do not use force casts unless their safety is guaranteed.
- Do not silently replace missing values with arbitrary defaults.

## Coroutines

- Use structured concurrency.
- Do not use GlobalScope.
- Use lifecycle-aware coroutine scopes where appropriate.
- Avoid blocking operations on the main thread.
- Do not use runBlocking in production application code.
- Handle coroutine cancellation correctly.
- Do not swallow CancellationException.

## Flow

- Follow existing StateFlow and SharedFlow patterns.
- Do not introduce additional state holders without justification.
- Avoid unnecessary Flow transformations and intermediate streams.
- Use appropriate dispatchers for blocking operations.
- Preserve existing state ownership and data flow.

## Error Handling

- Handle errors at the appropriate architectural layer.
- Do not silently ignore exceptions.
- Do not catch Throwable unless explicitly justified.
- Preserve meaningful error information.
- Avoid exposing internal implementation details to the UI.

## Code Quality

- Prefer val over var when mutation is unnecessary.
- Keep functions focused on a clear responsibility.
- Avoid deeply nested conditional logic.
- Avoid duplicated business logic.
- Use descriptive names consistent with existing code.
- Do not add dependencies or utility classes without approval.

## Changes and Verification

- Explain proposed changes before implementation.
- Modify only files relevant to the approved task.
- Preserve existing behavior unless a change is explicitly requested.
- Identify affected tests when behavior changes.
- Do not claim that code compiles or tests pass without verification.
