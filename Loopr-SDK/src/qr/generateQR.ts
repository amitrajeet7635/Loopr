import * as QRCode from 'qrcode';

export async function generateQRCode(uri: string): Promise<HTMLCanvasElement | string> {
  // Check if we're in a browser environment
  if (typeof document !== 'undefined') {
    // Browser environment - return canvas element
    const canvas = document.createElement('canvas');
    await QRCode.toCanvas(canvas, uri, {
      width: 256,
      margin: 2,
      color: {
        dark: '#000000',
        light: '#ffffff'
      }
    });
    return canvas;
  } else {
    // Node.js environment - return data URL string
    try {
      const dataUrl = await QRCode.toDataURL(uri, {
        width: 256,
        margin: 2,
        color: {
          dark: '#000000',
          light: '#ffffff'
        }
      });
      return dataUrl;
    } catch (error) {
      // Fallback to string representation if dataURL is not available
      const qrString = await QRCode.toString(uri, { 
        type: 'terminal',
        small: true 
      });
      return qrString;
    }
  }
}
