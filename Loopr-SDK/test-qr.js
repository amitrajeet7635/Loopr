// Test QR code generation to identify any color issues
const QRCode = require('qrcode');

// Test basic QR code generation with our brand colors
const BRAND_COLORS = {
  primary: '#516BFE',
  secondary: '#7C44FE', 
  accent: '#8b5cf6',
  accent2: '#7F48F4',
  accent3: '#329CFA',
  dark: '#1e1b4b',
  light: '#f8fafc',
  finder: '#4c1d95'
};

async function testQRGeneration() {
  console.log('Testing QR code generation...');
  
  try {
    // Test 1: Basic QR code with brand colors
    console.log('Test 1: Basic QR code with hex colors');
    const dataUrl1 = await QRCode.toDataURL('https://loopr.example.com/payment', {
      width: 280,
      margin: 2,
      color: {
        dark: BRAND_COLORS.dark,
        light: BRAND_COLORS.light
      },
      errorCorrectionLevel: 'H'
    });
    console.log('✓ Success: Basic QR code generated');

    // Test 2: QR code with primary colors
    console.log('Test 2: QR code with primary colors');
    const dataUrl2 = await QRCode.toDataURL('https://loopr.example.com/payment', {
      width: 280,
      margin: 1,
      color: {
        dark: BRAND_COLORS.primary,
        light: '#ffffff'
      },
      errorCorrectionLevel: 'H'
    });
    console.log('✓ Success: Primary color QR code generated');

    // Test 3: Try problematic color formats
    console.log('Test 3: Testing with problematic colors...');
    try {
      const dataUrl3 = await QRCode.toDataURL('https://loopr.example.com/payment', {
        width: 280,
        margin: 1,
        color: {
          dark: 'rgba(255, 255, 255, 0)', // This should fail
          light: '#ffffff'
        },
        errorCorrectionLevel: 'H'
      });
      console.log('⚠️  Warning: RGBA with 0 alpha worked unexpectedly');
    } catch (error) {
      console.log('✓ Expected error with RGBA(255,255,255,0):', error.message);
    }

    // Test 4: All valid hex colors
    console.log('Test 4: All brand colors validation');
    Object.entries(BRAND_COLORS).forEach(([name, color]) => {
      if (color.startsWith('#') && color.length === 7) {
        console.log(`✓ ${name}: ${color} - Valid hex`);
      } else {
        console.log(`✗ ${name}: ${color} - Invalid hex format`);
      }
    });

  } catch (error) {
    console.error('Error during QR generation:', error);
  }
}

testQRGeneration();
