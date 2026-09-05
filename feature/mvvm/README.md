# MVVM

## Overview

This module demonstrates the MVVM architecture using:

- ViewModel
- StateFlow
- UseCase
- Repository

## Architecture

UI
↓
ViewModel
↓
UseCase
↓
Repository
↓
Data

## Main components

### ViewModel

Holds the UI state and exposes StateFlow.

### State

Represents the current UI state.

### UseCase

Contains business logic.

### Repository

Provides data from local or remote sources.