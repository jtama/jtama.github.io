/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./content/**/*.html",
    "./templates/**/*.html",
    "./src/main/resources/web/**/*.js"
  ],
  darkMode: 'class', // Enable class-based dark mode
  theme: {
    extend: {
      colors: {
        mucha: {
          bg: 'var(--mucha-bg)',
          'card-bg': 'var(--mucha-card-bg)',
          text: 'var(--mucha-text)',
          gold: 'var(--mucha-gold)',
          sage: 'var(--mucha-sage)',
          rose: 'var(--mucha-rose)',
          mauve: 'var(--mucha-mauve)',
          blue: 'var(--mucha-blue)',
          border: 'var(--mucha-border)',
          shadow: 'var(--mucha-shadow)',
        }
      },
      fontFamily: {
        heading: ['"Glass Antiqua"', 'cursive'],
        body: ['"Cormorant Garamond"', 'serif'],
      },
    },
  },
  plugins: [
    require('@tailwindcss/typography'),
  ],
}
