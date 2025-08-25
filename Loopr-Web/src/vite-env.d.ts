/// <reference types="vite/client" />

declare global {
  interface Window {
    Buffer: typeof Buffer;
    process: any;
  }
  
  var Buffer: typeof Buffer;
  var process: any;
}

export {};
