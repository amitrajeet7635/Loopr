export interface Plan{
    id: string,
    name: string,
    priceUSD: number,
    frequency: 'monthly' | 'quarterly' | 'yearly',
    features: string[];
    merchantWallet: string;
}