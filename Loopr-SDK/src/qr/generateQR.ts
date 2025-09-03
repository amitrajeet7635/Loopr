import * as QRCode from 'qrcode';

// Logo will be handled via dynamic loading

// Loopr brand colors - purple/blue gradient theme inspired by the logo
const BRAND_COLORS = {
  primary: '#516BFE', // Blue from logo
  secondary: '#7C44FE', // Purple from logo 
  accent: '#8b5cf6', // Violet-500
  accent2: '#7F48F4', // Purple shade from logo
  accent3: '#329CFA', // Light blue from logo
  dark: '#1e1b4b', // Indigo-900
  light: '#f8fafc', // Slate-50
  finder: '#4c1d95' // Deep purple for finder patterns
};

// Utility function to validate hex colors and convert RGBA to hex if needed
function ensureValidHexColor(color: string): string {
  // If it's already a valid hex color, return it
  if (/^#[0-9A-Fa-f]{6}$/.test(color)) {
    return color;
  }
  
  // If it's a named color from our brand palette, return it
  const brandColor = Object.values(BRAND_COLORS).find(c => c === color);
  if (brandColor) {
    return brandColor;
  }
  
  // If it's an RGBA or invalid format, return a safe fallback
  console.warn(`Invalid color format detected: ${color}, using fallback`);
  return BRAND_COLORS.dark; // Safe fallback
}

function createSimplifiedLogo(): string {
  return `
<svg width="80" height="80" viewBox="0 0 80 80" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <radialGradient id="logoGrad" cx="50%" cy="50%" r="50%">
      <stop offset="0%" style="stop-color:${BRAND_COLORS.secondary};stop-opacity:1" />
      <stop offset="30%" style="stop-color:${BRAND_COLORS.accent2};stop-opacity:1" />
      <stop offset="60%" style="stop-color:${BRAND_COLORS.primary};stop-opacity:1" />
      <stop offset="100%" style="stop-color:${BRAND_COLORS.accent3};stop-opacity:1" />
    </radialGradient>
    <linearGradient id="logoLinear" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" style="stop-color:${BRAND_COLORS.primary};stop-opacity:1" />
      <stop offset="25%" style="stop-color:${BRAND_COLORS.accent3};stop-opacity:1" />
      <stop offset="50%" style="stop-color:${BRAND_COLORS.secondary};stop-opacity:1" />
      <stop offset="75%" style="stop-color:${BRAND_COLORS.accent2};stop-opacity:1" />
      <stop offset="100%" style="stop-color:${BRAND_COLORS.primary};stop-opacity:1" />
    </linearGradient>
    <filter id="shadow">
      <feDropShadow dx="2" dy="2" stdDeviation="2" flood-color="${BRAND_COLORS.dark}" flood-opacity="0.3"/>
    </filter>
  </defs>
  
  <!-- White background circle with shadow -->
  <circle cx="40" cy="40" r="38" fill="white" stroke="url(#logoLinear)" stroke-width="4" filter="url(#shadow)"/>
  
  <!-- Main circular loop inspired by your logo -->
  <path d="M25 40 Q25 25, 40 25 Q55 25, 55 40 Q55 55, 40 55 Q28 55, 28 42" 
        stroke="url(#logoLinear)" stroke-width="6" fill="none" stroke-linecap="round" filter="url(#shadow)"/>
  
  <!-- Inner decorative circles -->
  <circle cx="50" cy="30" r="4" fill="url(#logoGrad)" filter="url(#shadow)"/>
  <circle cx="32" cy="48" r="3" fill="url(#logoLinear)" filter="url(#shadow)"/>
  
  <!-- Loopr text -->
  <text x="40" y="68" text-anchor="middle" font-family="Arial Black, Arial" font-size="11" font-weight="900" fill="url(#logoLinear)" filter="url(#shadow)">LOOPR</text>
</svg>`;
}

// Function to get the actual Loopr infinity logo SVG content
function getLooprLogoSVG(): string {
  // Return an infinity-style logo SVG content that matches the loopr-logo-icon.png
  return `
<svg width="120" height="120" viewBox="0 0 120 120" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <linearGradient id="infinityGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" style="stop-color:#516BFE;stop-opacity:1" />
      <stop offset="25%" style="stop-color:#7C44FE;stop-opacity:1" />
      <stop offset="50%" style="stop-color:#7F48F4;stop-opacity:1" />
      <stop offset="75%" style="stop-color:#329CFA;stop-opacity:1" />
      <stop offset="100%" style="stop-color:#516BFE;stop-opacity:1" />
    </linearGradient>
    <filter id="infinityShadow">
      <feDropShadow dx="2" dy="2" stdDeviation="3" flood-color="#1e1b4b" flood-opacity="0.3"/>
    </filter>
  </defs>
  
  <!-- White background circle -->
  <circle cx="60" cy="60" r="55" fill="white" stroke="url(#infinityGrad)" stroke-width="3" filter="url(#infinityShadow)"/>
  
  <!-- Infinity symbol - two connected loops -->
  <path d="M30 60 C30 45, 40 35, 50 35 C60 35, 70 45, 70 60 C70 75, 80 85, 90 85 C100 85, 110 75, 110 60 C110 45, 100 35, 90 35 C80 35, 70 45, 70 60 C70 75, 60 85, 50 85 C40 85, 30 75, 30 60 Z" 
        fill="url(#infinityGrad)" filter="url(#infinityShadow)"/>
  
  <!-- Inner decorative elements -->
  <circle cx="45" cy="60" r="8" fill="white" filter="url(#infinityShadow)"/>
  <circle cx="75" cy="60" r="8" fill="white" filter="url(#infinityShadow)"/>
  
  <!-- Center connection point -->
  <circle cx="60" cy="60" r="4" fill="url(#infinityGrad)" filter="url(#infinityShadow)"/>
</svg>`;
}

async function loadLogoFromFile(): Promise<string | null> {
  try {
    // In browser environment, try to fetch the loopr-logo-icon.png (infinity logo)
    if (typeof window !== 'undefined') {
      const paths = [
        // Primary paths for the infinity logo
        '/loopr-logo-icon.png', // From public folder (copied there)
        '/assets/loopr-logo-icon.png', // Public assets folder
        // Try to import the asset directly (webpack/vite should handle this)
        new URL('../assets/loopr-logo-icon.png', import.meta.url).href,
        // Fallback paths
        '/src/assets/loopr-logo-icon.png',
        './src/assets/loopr-logo-icon.png',
        '../Loopr-SDK/src/assets/loopr-logo-icon.png'
      ];
      
      for (const path of paths) {
        try {
          console.log('Attempting to load infinity logo from:', path);
          const response = await fetch(path);
          if (response.ok && response.headers.get('content-type')?.includes('image')) {
            console.log('Successfully loaded infinity logo from:', path);
            const blob = await response.blob();
            return URL.createObjectURL(blob);
          }
        } catch (e) {
          console.warn('Failed to load from path:', path, e);
          // Continue to next path
        }
      }
    }
    
  } catch (error) {
    console.warn('Could not load loopr-logo-icon.png (infinity logo), using enhanced logo', error);
  }
  
  console.log('Using fallback SVG logo (infinity-style)');
  // Return the enhanced Loopr logo as fallback
  return null; // Return null to use SVG fallback
}

async function createCustomizedQRCanvas(qrCanvas: HTMLCanvasElement, logoSvg: string): Promise<HTMLCanvasElement> {
  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d')!;
  
  // Use the same size as the original QR code for simplicity
  const size = qrCanvas.width;
  canvas.width = size;
  canvas.height = size;
  
  // Draw the original QR code exactly as generated (standard black and white)
  ctx.drawImage(qrCanvas, 0, 0, size, size);
  
  // Add logo in center with simple white background (await the async operation)
  await addLogoToCenter(ctx, 0, 0, size, logoSvg);
  
  return canvas;
}


function addLogoToCenter(ctx: CanvasRenderingContext2D, qrX: number, qrY: number, qrSize: number, logoSvg: string): Promise<void> {
  return new Promise((resolve) => {
    const logoSize = 60; // Standard size for QR code logo
    const logoX = qrX + (qrSize - logoSize) / 2;
    const logoY = qrY + (qrSize - logoSize) / 2;
    
    // Create a simple white rectangular background for the logo
    ctx.fillStyle = 'white';
    ctx.fillRect(logoX - 4, logoY - 4, logoSize + 8, logoSize + 8);
    
    // Load and draw the actual logo image
    const img = new Image();
    
    // Check if logoSvg is a URL (from loadLogoFromFile) or SVG content
    if (logoSvg.startsWith('blob:') || logoSvg.startsWith('http') || logoSvg.startsWith('/')) {
      // It's a URL, load directly
      img.src = logoSvg;
    } else {
      // It's SVG content, convert to blob URL
      const blob = new Blob([logoSvg], { type: 'image/svg+xml' });
      const url = URL.createObjectURL(blob);
      img.src = url;
    }
    
    img.onload = () => {
      // Draw the logo maintaining aspect ratio
      ctx.drawImage(img, logoX, logoY, logoSize, logoSize);
      
      // Clean up blob URL if created
      if (img.src.startsWith('blob:')) {
        URL.revokeObjectURL(img.src);
      }
      resolve();
    };
    
    img.onerror = () => {
      console.warn('Failed to load logo, using simple fallback');
      // Draw a simple rectangular fallback logo
      ctx.fillStyle = BRAND_COLORS.primary;
      ctx.fillRect(logoX, logoY, logoSize, logoSize);
      
      // Add "L" text
      ctx.fillStyle = 'white';
      ctx.font = `bold ${logoSize * 0.5}px Arial`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText('L', logoX + logoSize / 2, logoY + logoSize / 2);
      
      resolve();
    };
  });
}

async function addSimpleLogo(canvas: HTMLCanvasElement): Promise<void> {
  return new Promise((resolve) => {
    const ctx = canvas.getContext('2d')!;
    const logoSize = 60; // Standard logo size
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const logoX = centerX - logoSize / 2;
    const logoY = centerY - logoSize / 2;

    // Create simple white background for logo
    ctx.fillStyle = 'white';
    ctx.fillRect(logoX - 4, logoY - 4, logoSize + 8, logoSize + 8);

    // Try to load the actual logo from assets
    const img = new Image();
    
    // Try the logo sources
    const logoSources = [
      '/loopr-logo-icon.png', // From public folder
      '/assets/loopr-logo-icon.png',
      '/src/assets/loopr-logo-icon.png',
    ];

    let currentIndex = 0;
    
    const tryLoadLogo = () => {
      if (currentIndex >= logoSources.length) {
        // If all sources fail, draw a simple branded logo
        drawFallbackLogo(ctx, logoX, logoY, logoSize);
        resolve();
        return;
      }

      img.src = logoSources[currentIndex];
      currentIndex++;
    };

    img.onload = () => {
      try {
        // Draw the logo
        ctx.drawImage(img, logoX, logoY, logoSize, logoSize);
        resolve();
      } catch (error) {
        console.warn('Error drawing logo image:', error);
        tryLoadLogo();
      }
    };

    img.onerror = () => {
      console.warn(`Failed to load logo from: ${logoSources[currentIndex - 1]}`);
      tryLoadLogo();
    };

    // Start loading
    tryLoadLogo();
  });
}

function drawFallbackLogo(ctx: CanvasRenderingContext2D, x: number, y: number, size: number): void {
  // Simple branded background
  ctx.fillStyle = BRAND_COLORS.primary;
  ctx.fillRect(x, y, size, size);
  
  // Draw "L" for Loopr
  ctx.fillStyle = 'white';
  ctx.font = `bold ${size * 0.5}px Arial`;
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';
  ctx.fillText('L', x + size / 2, y + size / 2);
}

export async function generateQRCode(uri: string): Promise<HTMLCanvasElement | string> {
  console.log('Generating QR code for URI:', uri);
  
  // Check if we're in a browser environment
  if (typeof document !== 'undefined') {
    console.log('Browser environment detected, generating canvas QR code');
    
    // Browser environment - return canvas element
    const canvas = document.createElement('canvas');
    const size = 400;
    canvas.width = size;
    canvas.height = size;
    
    try {
      // Generate base QR code with application theme colors
      await QRCode.toCanvas(canvas, uri, {
        width: size,
        margin: 4,
        color: {
          dark: BRAND_COLORS.primary, // Theme blue color
          light: '#ffffff' // White background
        },
        errorCorrectionLevel: 'H' // High error correction for logo integration
      });
      
      console.log('Base QR code generated successfully');
      
      // Load the actual Loopr infinity logo
      const logoSource = await loadLogoFromFile();
      console.log('Logo loaded:', logoSource ? 'success' : 'using fallback');
      
      // Create customized QR code with the infinity logo
      const customizedCanvas = await createCustomizedQRCanvas(canvas, logoSource || getLooprLogoSVG());
      
      console.log('QR code with infinity logo created successfully');
      
      return customizedCanvas;
    } catch (error) {
      console.error('Error generating canvas QR code:', error);
      // Fallback to simple QR code with basic logo
      try {
        await QRCode.toCanvas(canvas, uri, {
          width: size,
          margin: 4,
          color: {
            dark: '#000000', // Classic black
            light: '#ffffff'
          },
          errorCorrectionLevel: 'M'
        });
        console.log('Fallback QR code generated');
        await addSimpleLogo(canvas);
        return canvas;
      } catch (fallbackError) {
        console.error('Fallback canvas generation also failed:', fallbackError);
        // Return data URL as final fallback
        return await QRCode.toDataURL(uri, {
          width: size,
          margin: 4,
          color: {
            dark: BRAND_COLORS.primary,
            light: '#ffffff'
          },
          errorCorrectionLevel: 'M'
        });
      }
    }
  } else {
    console.log('Node.js environment detected, generating data URL QR code');
    
    // Node.js environment - return data URL string with branded styling
    try {
      const dataUrl = await QRCode.toDataURL(uri, {
        width: 400,
        margin: 4,
        color: {
          dark: '#000000', // Classic black
          light: '#ffffff'
        },
        errorCorrectionLevel: 'H'
      });
      console.log('Data URL QR code generated successfully');
      return dataUrl;
    } catch (error) {
      console.error('Error generating data URL QR code:', error);
      // Fallback to string representation if dataURL is not available
      const qrString = await QRCode.toString(uri, { 
        type: 'terminal',
        small: true 
      });
      console.log('Fallback string QR code generated');
      return qrString;
    }
  }
}
