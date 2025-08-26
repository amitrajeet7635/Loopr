import typescript from 'rollup-plugin-typescript2';
import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import { terser } from 'rollup-plugin-terser';

export default {
  input: 'src/index.ts',
  output: [
    {
      file: 'dist/loopr-sdk.umd.js',
      format: 'umd',
      name: 'LooprSDK',
      sourcemap: true,
      globals: {
        react: 'React',
        '@solana/web3.js': 'solanaWeb3',
        '@solana/pay': 'solanaPay',
        'qrcode': 'QRCode',
        'bignumber.js': 'BigNumber'
      }
    },
    {
      file: 'dist/loopr-sdk.esm.js',
      format: 'esm',
      sourcemap: true
    }
  ],
  external: ['react', '@solana/web3.js', '@solana/pay', 'qrcode', 'bignumber.js'],
  plugins: [
    resolve({
      browser: true,
      preferBuiltins: false,
      // Add buffer polyfill resolution
      alias: {
        buffer: 'buffer'
      }
    }),
    commonjs({
      // Include buffer in commonjs transformation
      include: ['node_modules/**'],
    }),
    typescript({
      typescript: require('typescript'),
      tsconfig: './tsconfig.json',
      declaration: true,
      declarationDir: 'dist',
      useTsconfigDeclarationDir: false
    }),
    terser()
  ]
};