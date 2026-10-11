
# Jetpack Compose Development Rules

## General

- Follow the existing Compose conventions in the project.
- Prefer simple, readable composable functions.
- Keep UI implementation consistent with the existing design.
- Do not introduce new UI libraries without approval.
- Do not refactor unrelated composables.

## State Management

- Prefer unidirectional data flow.
- Keep screen-level state in the existing ViewModel or state holder.
- Use state hoisting when it improves ownership and reusability.
- Do not duplicate ViewModel state inside composables.
- Use remember for local UI state that does not need to survive recomposition.
- Use rememberSaveable when local UI state must survive supported state restoration.
- Do not introduce additional sources of truth without justification.

## Recomposition

- Keep composables free of unnecessary calculations.
- Use remember for expensive calculations when appropriate.
- Use derivedStateOf only when it provides a measurable or clear benefit.
- Avoid creating unnecessary state objects.
- Do not add optimization mechanisms without identifying a real problem.
- Do not use side effects during composition.

## Side Effects

- Use LaunchedEffect for coroutine-based effects tied to composition.
- Use DisposableEffect for effects requiring cleanup.
- Use SideEffect only for appropriate post-composition operations.
- Choose effect keys based on the intended lifecycle.
- Do not trigger navigation, analytics, or network requests directly from the composable body during composition.

## Events and Navigation

- Forward user actions through the existing event or callback mechanisms.
- Preserve the established MVVM or MVI event flow.
- Do not introduce alternative navigation mechanisms without approval.
- Keep navigation side effects consistent with the existing architecture.

## Lists and Paging 3

- Use LazyColumn and LazyRow for lazily rendered collections.
- Provide stable item keys when reliable unique identifiers are available.
- Preserve the existing Paging 3 integration.
- Use collectAsLazyPagingItems for PagingData collected in Compose.
- Handle Paging load states when required by the screen.
- Do not convert PagingData into a separate full in-memory list.
- Do not introduce duplicate item state or manual paging logic without explicit justification.
- Consider item updates, refresh behavior, and the actual source of truth when modifying paged UI.

## UI Components

- Extract reusable composables when there is actual reuse or a clear responsibility boundary.
- Avoid excessive decomposition into trivial composables.
- Pass only the state and callbacks required by a component.
- Prefer immutable UI models where appropriate.
- Keep business logic outside presentation-only composables.

## Verification

- Consider recomposition and state restoration when changing UI behavior.
- Check loading, empty, error, and content states where applicable.
- Do not claim that a UI change works without verification.
