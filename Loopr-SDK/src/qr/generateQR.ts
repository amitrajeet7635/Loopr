import QRCode from 'qrcode';

export async function generateQRCode(uri: string): Promise<HTMLCanvasElement> {
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
}
