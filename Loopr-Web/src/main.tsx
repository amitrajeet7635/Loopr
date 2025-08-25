import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

// Polyfills for Node.js globals in browser environment
import { Buffer } from 'buffer'

window.Buffer = Buffer
// @ts-ignore
window.process = window.process || { env: {} }

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
