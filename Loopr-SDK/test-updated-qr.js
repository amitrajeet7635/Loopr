// Test the updated generateQR function with color validation
const { generateQRCode } = require('./dist/loopr-sdk.umd.js');

async function testUpdatedQR() {
  console.log('Testing updated generateQR function...');
  
  try {
    // Test in Node.js environment (should return data URL)
    console.log('Testing Node.js environment...');
    const result = await generateQRCode('https://loopr.example.com/payment');
    
    if (typeof result === 'string') {
      if (result.startsWith('data:')) {
        console.log('✓ Success: Generated data URL in Node.js environment');
        console.log('  Data URL length:', result.length);
      } else {
        console.log('✓ Success: Generated terminal string in Node.js environment');
        console.log('  String preview:', result.substring(0, 100) + '...');
      }
    } else {
      console.log('⚠️  Warning: Expected string in Node.js environment, got:', typeof result);
    }
    
  } catch (error) {
    console.error('✗ Error:', error.message);
    
    // Check if it's the specific RGBA error we're trying to fix
    if (error.message.includes('Invalid hex color: rgba(255, 255, 255, 0)')) {
      console.log('❌ The RGBA(255,255,255,0) error still exists!');
    }
  }
}

testUpdatedQR();
