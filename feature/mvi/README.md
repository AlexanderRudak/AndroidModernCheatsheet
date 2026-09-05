# MVI

## Overview

This module demonstrates the MVI (Model-View-Intent) architecture using:

- Event
- State
- Reducer
- Command
- Actor
- News

## Architecture

```text
UI
    ↓
Event
    ↓
BaseMviViewModel
    ↓
Reducer
    ↓
State + Command
    ↓
Actor
    ↓
UseCase
    ↓
Event
    ↓
Reducer
    ↓
Updated State
    ↓
Compose UI
```

## Main components

### Event

Represents user actions and results of asynchronous operations.

### State

Represents the current UI state.

### Reducer

Receives State + Event and returns State + optional Command.

### Command

Represents a side effect that should be executed.

### Actor

Executes Commands and returns Events.

### News

Represents one-time events such as navigation or snackbar messages.

## Data flow example

```text
FavoriteClicked
    ↓
Reducer
    ↓
ToggleFavorite Command
    ↓
Actor
    ↓
FavoriteUpdated
    ↓
Reducer
    ↓
Updated State
```