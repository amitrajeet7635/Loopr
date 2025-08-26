/// <reference types="vite/client" />

declare global {
  interface Window {
    Buffer: typeof import('buffer').Buffer;
    process: {
      env: Record<string, string | undefined>;
    };
  }
  var Buffer: typeof import('buffer').Buffer;
}
