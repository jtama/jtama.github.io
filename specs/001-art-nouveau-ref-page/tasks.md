---
description: "Task list template for feature implementation"
---

# Tasks: Art Nouveau Reference Page

**Input**: Design documents from `/specs/001-art-nouveau-ref-page/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md

**Tests**: Tests are manual visual inspections and accessibility audits as per plan. No automated test files required.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [X] T001 Create reference file `inspiration/art_nouveau_reference.html` with HTML5 boilerplate
- [X] T002 Add Tailwind CSS via CDN and basic configuration in `inspiration/art_nouveau_reference.html`
- [X] T003 Add Google Fonts (Glass Antiqua, Cormorant Garamond) links in `inspiration/art_nouveau_reference.html`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core layout structure that MUST be complete before specific design elements

- [X] T004 Define "Mucha" color palette in Tailwind config within `inspiration/art_nouveau_reference.html`
- [X] T005 Implement base layout structure (`header`, `main`, `footer`) with semantic tags in `inspiration/art_nouveau_reference.html`
- [X] T006 Add "Skip to main content" link for accessibility in `inspiration/art_nouveau_reference.html`

**Checkpoint**: Foundation ready - semantic structure and base styles available.

---

## Phase 3: User Story 1 - Semantic Structure & Mobile Layout (Priority: P1)

**Goal**: Establish a responsive, semantic HTML structure that works on mobile devices.

**Independent Test**: View at 320px width; verify stacking and no overflow.

### Implementation for User Story 1

- [X] T007 [US1] Implement Header with responsive navigation placeholder in `inspiration/art_nouveau_reference.html`
- [X] T008 [US1] Implement Hero section with semantic markup in `inspiration/art_nouveau_reference.html`
- [X] T009 [US1] Implement Article List section with placeholder content from data-model in `inspiration/art_nouveau_reference.html`
- [X] T010 [US1] Implement Footer with semantic markup in `inspiration/art_nouveau_reference.html`
- [X] T011 [US1] Verify mobile-first stacking (no horizontal scroll at 320px)

**Checkpoint**: Page is structurally sound and mobile-responsive.

---

## Phase 4: User Story 2 - Art Nouveau Visual Design (Priority: P1)

**Goal**: Apply the Art Nouveau aesthetic (Mucha palette, organic lines, fonts).

**Independent Test**: Visual inspection against reference image and palette.

### Implementation for User Story 2

- [X] T012 [US2] Apply "Mucha" background and text colors to body and containers in `inspiration/art_nouveau_reference.html`
- [X] T013 [US2] Apply correct font families (Headings: Glass Antiqua, Body: Cormorant Garamond) in `inspiration/art_nouveau_reference.html`
- [X] T014 [US2] Implement organic borders (irregular border-radius) for cards and sections in `inspiration/art_nouveau_reference.html`
- [X] T015 [US2] Add decorative border styles (Gold colors) to containers in `inspiration/art_nouveau_reference.html`

**Checkpoint**: Page visually matches the Art Nouveau aesthetic.

---

## Phase 5: User Story 3 - Accessibility Compliance (Priority: P2)

**Goal**: Ensure the page is fully accessible to all users.

**Independent Test**: Lighthouse/Axe audit score of 100%.

### Implementation for User Story 3

- [X] T016 [US3] Verify and fix contrast ratios for all text elements in `inspiration/art_nouveau_reference.html`
- [X] T017 [US3] Ensure all interactive elements have visible focus states in `inspiration/art_nouveau_reference.html`
- [X] T018 [US3] Add `aria-label` and `alt` attributes to images/icons in `inspiration/art_nouveau_reference.html`
- [X] T019 [US3] Run final Lighthouse audit and fix any reported issues

**Checkpoint**: Page passes all accessibility checks.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final cleanups

- [X] T020 Remove any unused CSS classes or comments
- [X] T021 Validate HTML5 syntax

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Starts immediately.
- **Foundational (Phase 2)**: Depends on Phase 1. Blocks User Stories.
- **User Story 1 (P1)**: Semantic structure. Depends on Phase 2.
- **User Story 2 (P1)**: Visual design. Can run in parallel with US1 content population, but relies on US1 structure for final application. Best executed after US1.
- **User Story 3 (P2)**: Accessibility. Should be continuous, but final verification depends on US1 & US2 completion.

### User Story Dependencies

- **US1 (Structure)**: Independent after foundation.
- **US2 (Design)**: Dependent on US1 elements existing.
- **US3 (A11y)**: Dependent on US1/US2 implementation.

### Implementation Strategy

1. **MVP**: Complete Phases 1, 2, and 3 (Semantic Mobile Page).
2. **Design Increment**: Complete Phase 4 (Art Nouveau Style).
3. **Compliance Increment**: Complete Phase 5 (Accessibility Hardening).
