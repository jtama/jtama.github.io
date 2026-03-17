/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./content/**/*.html",
    "./templates/**/*.html",
    "./src/**/*Extension.java",
    "./src/main/resources/web/**/*.js"
  ],
  darkMode: 'class', // Enable class-based dark mode
  theme: {
    extend: {
      colors: {
        cream: '#FDF8E1',
        primary: {
          DEFAULT: '#F87171',
          hover: '#ef4444'
        },
        body: '#2D2424'
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
        serif: ['Playfair Display', 'serif'],
      }
    }
  },
  safelist: [
    'text-green-600',
    'text-blue-600',
    'text-red-400',
    'text-orange-500',
    'text-violet-400'
  ],
  plugins: [
    require('@tailwindcss/typography'),
  ],
}
