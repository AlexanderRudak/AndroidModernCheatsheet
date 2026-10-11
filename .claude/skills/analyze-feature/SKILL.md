
---
name: analyze-feature
description: Analyze an existing Android feature, trace its architecture and data flow, identify problems, and propose minimal changes without modifying code.
disable-model-invocation: true
argument-hint: "[feature and analysis goal]"
allowed-tools: Read, Glob, Grep
---

# Android Feature Analysis

## Task

Analyze the following feature or problem:

$ARGUMENTS

This skill is strictly read-only. Do not modify files, run commands, or generate implementation code unless explicitly requested in a separate task.

## Step 1: Locate Relevant Code

- Find the relevant feature and its entry points.
- Inspect only files necessary to understand the task.
- Identify existing ViewModels, UI components, UseCases, repositories, and data sources.
- Determine whether the feature uses MVVM or MVI.
- Do not assume classes or components exist without verifying them.

## Step 2: Trace Architecture

For MVVM:
- Trace UI actions through the ViewModel.
- Identify state management and side effects.
- Trace relevant UseCases and repository operations.

For MVI:
- Trace UI Events through ViewModel, Reducer, Commands, and Actors.
- Identify State updates and News handling.
- Trace asynchronous operations and their results.
- Identify how UI data is actually rendered.

For Paging 3:
- Identify the PagingData source and transformations.
- Determine the source of truth for item data.
- Inspect relevant refresh, invalidation, and update behavior.

## Step 3: Analyze the Problem

- Explain the current implementation.
- Identify the requested behavior.
- Compare actual and expected behavior.
- Identify the root cause when supported by code.
- Clearly separate verified findings from assumptions.
- Identify relevant architectural constraints.

## Step 4: Propose a Solution

- Prefer the smallest change compatible with the existing architecture.
- Reuse existing components and infrastructure.
- Avoid unnecessary abstractions or duplicate state.
- Identify affected files.
- Explain important trade-offs.
- Mention alternatives only when they offer meaningful advantages.

## Step 5: Verification

- Identify existing tests relevant to the change.
- Propose focused tests when appropriate.
- Explain how the proposed behavior can be verified.
- Do not claim that tests were executed.

## Required Output

Respond in Russian using the following structure:

1. Current implementation
2. Data and event flow
3. Findings and root cause
4. Recommended solution
5. Files affected
6. Verification approach

Reference actual file paths and relevant functions or classes.

Do not modify project files.
/exit
