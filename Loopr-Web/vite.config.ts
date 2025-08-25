import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import path from "path";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
  define: {
    global: 'globalThis',
  },
  resolve: {
    alias: {
      buffer: path.resolve(__dirname, 'node_modules/buffer'),
      process: path.resolve(__dirname, 'node_modules/process/browser'),
      stream: path.resolve(__dirname, 'node_modules/stream-browserify'),
      crypto: path.resolve(__dirname, 'node_modules/crypto-browserify'),
    },
  },
  optimizeDeps: {
    include: ['buffer', 'process/browser', 'stream-browserify', 'crypto-browserify'],
  },
});
