
# Android Architecture Rules

## General Principles

- Treat the existing project architecture as the source of truth.
- Preserve the established separation of responsibilities.
- Do not introduce new architectural layers or patterns without approval.
- Prefer solutions that fit existing components and data flow.
- Do not refactor unrelated features.
- Do not create abstractions solely for theoretical architectural purity.
- Evaluate proposed changes in the context of the complete feature.

## MVVM

- Follow the existing MVVM implementation.
- Keep screen state management consistent with the current ViewModel.
- Preserve the established UI event and state update mechanisms.
- Keep business operations in the appropriate existing layer.
- Do not introduce MVI concepts into MVVM without explicit approval.
- Reuse existing UseCases and repositories when applicable.

## MVI

- Follow the project's existing custom MVI implementation.
- Inspect the actual MVI data flow before proposing changes.
- Respect the responsibilities of ViewModel, Reducer, Actor, Event, Command, State, and News as implemented in the project.
- Keep state transitions predictable.
- Preserve the existing command and side-effect execution mechanisms.
- Do not introduce additional event types or state containers without justification.
- Do not move business operations into the Reducer without examining the existing design.
- Do not assume that all UI data must be stored directly in MVI State.
- Do not introduce a second source of truth for paginated data without explicit approval.
- Explain how asynchronous results affect UI state.

## Domain and Data Layers

- Reuse existing UseCases and repositories.
- Respect established dependency direction.
- Keep data-source implementation details out of presentation logic.
- Do not create a new UseCase for every trivial operation.
- Do not bypass existing business logic without justification.
- Preserve existing domain models and mapping conventions.

## Paging 3

- Preserve the existing Paging 3 data flow.
- Identify the actual source of truth before proposing item updates.
- Consider PagingSource invalidation, refresh behavior, and database observation where applicable.
- Do not duplicate PagingData into a manually maintained list.
- Do not introduce item overrides, caches, or synchronization layers without explicit approval.
- Explain how proposed changes affect pagination, loading states, and item consistency.

## Dependency Injection

- Follow the existing Koin module structure.
- Prefer constructor injection where appropriate.
- Reuse existing dependency definitions.
- Do not introduce a new dependency injection framework.
- Do not create unnecessary dependency wrappers.

## Analytics

- Reuse the existing analytics infrastructure.
- Preserve established event names, parameter keys, and event models.
- Use existing constants for event names and parameter keys.
- Do not scatter analytics string literals throughout the code.
- Keep analytics integration consistent with the architecture of each feature.
- Before adding analytics to MVI, inspect the existing MVVM analytics implementation and MVI command processing.
- Do not create a separate analytics module or parallel analytics infrastructure without explicit approval.

## Architecture Analysis Workflow

Before proposing an architectural change:

1. Identify the relevant feature and its existing components.
2. Trace the actual data and event flow through the code.
3. Identify the source of truth for the affected state.
4. Describe the problem and its cause based on inspected code.
5. Propose the smallest viable change.
6. Identify affected files and potential side effects.
7. Explain meaningful trade-offs and alternatives.
8. Wait for explicit approval before implementation.

Do not present assumptions about the codebase as verified facts.
