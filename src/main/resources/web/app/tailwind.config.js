/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./content/**/*.html",
    "./templates/**/*.html",
    "./src/**/*Extension.java",
    "./src/main/resources/web/**/*.js"
  ],
  darkMode: 'class', // Enable class-based dark mode
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
