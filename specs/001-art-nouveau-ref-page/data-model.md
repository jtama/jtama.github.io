# Data Model: Art Nouveau Reference Page

**Feature**: Art Nouveau Reference Page
**Date**: 2026-02-09

## Entities

### Reference Page

This page is a static HTML artifact. It has no dynamic data model or backend persistence.
However, it represents the following conceptual entities for display:

#### Site Metadata
- **Title**: `jtama.github.io`
- **Description**: "Coding with Passion | Sharing the Journey"
- **Author**: "J.Tama"
- **Theme**: Light (Mucha Palette) / Dark (Midnight Absinthe) - CSS variables.

#### Article (Reference Content)
- **Title**: String
- **Excerpt**: String
- **Date**: Date (ISO 8601)
- **Tags**: List<String>
- **Link**: URL (Relative)

## Relationships

- `Reference Page` displays 1 `Site Metadata`.
- `Reference Page` displays N `Articles`.

## Constraints

- Static HTML only.
- No database.
- No JavaScript state management (pure CSS/HTML).
