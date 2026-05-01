/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,jsx,ts,tsx}",
    "./components/**/*.{js,jsx,ts,tsx}",
  ],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors: {
        primary: "#6C63FF",
        secondary: "#FF6584",
        background: "#F8F9FE",
        card: "#FFFFFF",
        text: "#1A1A2E",
        muted: "#9E9E9E",
      },
    },
  },
  plugins: [],
};
