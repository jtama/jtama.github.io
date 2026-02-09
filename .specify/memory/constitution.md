<!--
Sync Impact Report:
- Version change: 1.0.0 -> 1.1.0
- Modified principles:
  - Added "Accessibility First" (Strict WCAG 2.2 AA compliance).
  - Added "Progressive Enhancement" (HTML > CSS > JS).
  - Updated "Technology Stack" (Java 23, Tailwind CSS).
- Added sections: None (merged into existing).
- Templates requiring updates:
  - .specify/templates/plan-template.md: ✅ compatible
  - .specify/templates/spec-template.md: ✅ compatible
  - .specify/templates/tasks-template.md: ✅ compatible
-->

# jtama.github.io Constitution

## Core Principles

### I. Content-First
The primary value of this repository is the content (AsciiDoc in `content/`). Code (`src/`) exists solely to render, organize, and enhance the presentation of this content. Content structure drives technical decisions, not vice-versa.

### II. Static Generation (Roq)
The site is statically generated using Quarkus Roq. We deploy static HTML/CSS/JS assets. Dynamic behavior must be client-side only (Search, Comments) or build-time generated. We avoid server-side runtime dependencies.

### III. Automated Publishing
Production deployments occur exclusively via GitHub Actions on the `main` branch. Manual uploads or commits to `gh-pages` are strictly forbidden. The CI pipeline is the only path to production.

### IV. Local Reproducibility
The build process (`./mvnw package`) must be deterministic. Any developer must be able to clone the repo and run `quarkus dev` to see an accurate preview of the site. Infrastructure-as-Code principles apply to build configuration.

### V. Simplicity & Standards
We prefer standard Quarkus Roq plugins and configurations over custom implementations. Custom code (Java/Qute) is introduced only when standard configuration cannot meet a requirement. Keep dependencies minimal.

### VI. Accessibility First (Non-Negotiable)
All UI code must be fully accessible by default (WCAG 2.2 Level AA). Use semantic HTML landmarks, logical focus order, and high contrast. Accessibility is not an afterthought; it is a core requirement validated via dedicated tools.

### VII. Progressive Enhancement
Render content primarily via HTML. Enhance styling with CSS (Tailwind). Use JavaScript only as a last resort for interactivity that cannot be achieved with HTML/CSS alone. The site must remain functional without JavaScript where possible.

## Technology Stack

### Core
- **Framework**: Quarkus (Roq extension)
- **Language**: Java 23 (LTS)
- **Build Tool**: Maven (via `mvnw`)
- **Content**: AsciiDoc
- **Styling**: Tailwind CSS (via PostCSS/Roq integration)

### Environment
- **Development**: `quarkus dev` (Live Reload)
- **CI/CD**: GitHub Actions
- **Hosting**: GitHub Pages

## Development Workflow

### Feature Development
1.  **Plan**: Identify if a change is Content (new post) or Code (new feature/theme).
2.  **Code**: For features, follow the Specification -> Plan -> Task flow.
3.  **Test**: Verify locally with `quarkus dev`. Ensure `mvnw package` succeeds. Validate accessibility.
4.  **Review**: PRs required for code changes. Content changes may self-merge if minor, but PR recommended.

## Governance

This constitution defines the non-negotiable rules for the project.
- **Amendments**: Changes to this document require a Pull Request and explicit ratification rationale.
- **Versioning**: Semantic Versioning (MAJOR.MINOR.PATCH) applies to this constitution.
- **Compliance**: All feature specifications must explicitly state compliance with these principles.

**Version**: 1.1.0 | **Ratified**: 2026-02-09 | **Last Amended**: 2026-02-09
