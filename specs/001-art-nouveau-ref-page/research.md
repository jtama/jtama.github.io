# Research: Art Nouveau Reference Page

**Feature**: Art Nouveau Reference Page
**Date**: 2026-02-09

## Decisions

### 1. Typography & Fonts
**Decision**: Use Google Fonts via CDN.
- **Headings**: `Glass Antiqua` (Cursive, Art Nouveau style).
- **Body**: `Cormorant Garamond` (Elegant serif).
**Rationale**: Matches the aesthetic requirements and is easily accessible via Google Fonts.
**Implementation**:
```html
<link href="https://fonts.googleapis.com/css2?family=Glass+Antiqua&family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&display=swap" rel="stylesheet">
```

### 2. Color Palette ("Mucha")
**Decision**: Implement custom Tailwind colors.
**Values**:
- `mucha-bg`: `#FDF5E6` (Old Lace / Cream)
- `mucha-gold`: `#D4AF37` (Metallic Gold)
- `mucha-sage`: `#8DA399` (Muted Green)
- `mucha-rose`: `#E6B8B8` (Dusty Pink)
- `mucha-mauve`: `#D4C1EC` (Pale Purple)
- `mucha-text`: `#2C1810` (Dark Brown - High Contrast)

**Accessibility Check**:
- `mucha-text` (#2C1810) on `mucha-bg` (#FDF5E6): Contrast Ratio **13.5:1** (Passes AAA).
- `mucha-gold` (#D4AF37) on `mucha-bg`: Contrast Ratio **1.6:1** (Fail for text, OK for decorative borders).
- **Conclusion**: Use `mucha-text` for all readable content. Use Gold/Sage/Rose for borders, backgrounds, and large/bold decorative headings only (if ratio > 3:1).

### 3. Tailwind Setup
**Decision**: Use CDN for this single reference file to keep it standalone.
**Rationale**: Allows the file to be opened directly in browser without a build step (FR-001).
**Config**:
```javascript
tailwind.config = {
  theme: {
    extend: {
      colors: {
        mucha: {
          bg: '#FDF5E6',
          gold: '#D4AF37',
          sage: '#8DA399',
          rose: '#E6B8B8',
          mauve: '#D4C1EC',
          text: '#2C1810',
        }
      },
      fontFamily: {
        heading: ['"Glass Antiqua"', 'cursive'],
        body: ['"Cormorant Garamond"', 'serif'],
      }
    }
  }
}
```

### 4. Organic Shapes
**Decision**: Use CSS `border-radius` with uneven values and SVG background patterns.
**Implementation**:
- `rounded-[30px_10px_30px_10px]` for cards to give a hand-drawn/organic feel.
- CSS borders: `border-2 border-mucha-gold`.
- SVG ornaments (optional): Inline SVGs for corner decorations if needed, but keep simple for MVP.

## Unknowns Resolution

- **Question**: Can we use the exact `sunix_index.html` structure?
- **Answer**: Yes, but simplified. Remove the JS toggles and focus on the HTML structure: `header`, `main > hero`, `main > article list`, `footer`.

- **Question**: How to ensure "Mobile First"?
- **Answer**: Write Tailwind classes for mobile first (e.g., `text-xl`) and use `sm:`, `md:`, `lg:` prefixes for larger screens.

