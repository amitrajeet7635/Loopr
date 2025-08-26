// Polyfills for Node.js modules in browser environment
import { Buffer } from 'buffer';

// Make Buffer available globally
window.Buffer = Buffer;
globalThis.Buffer = Buffer;

// Add process env if needed
window.process = window.process || {};
window.process.env = window.process.env || {};

export {};
