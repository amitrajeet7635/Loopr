export async function convertUsdToSol(usd: number): Promise<number> {
  // In production, fetch real-time price from CoinGecko, Jupiter Aggregator, etc.
  const SOL_PRICE_IN_USD = 25.0; // Replace this with actual fetch later
  return usd / SOL_PRICE_IN_USD;
}