export interface Product {
  id: string;
  name: string;
  price: number;
  stock: number;
  description?: string;
}

export interface CartItem {
  product: Product;
  quantity: number;
}

export interface Transaction {
  items: CartItem[];
  totalAmount: number;
  amountPaid: number;
  change: number;
}

export interface Coin {
  value: number;
  label: string;
}

// Interfaces pour les réponses de l'API
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: {
    message: string;
    code?: string;
  };
}

export interface PurchaseResult {
  purchasedProduct: Product;
  totalPaid: number;
  changeAmount: number;
  changeCoins: Record<string, number>;
  changeDisplay: string[];
}

export interface TransactionStatus {
  transactionId: string;
  totalInserted: number;
  status: 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  createdAt: string;
  selectedProduct?: Product;
  insertedCoins: number[];
  changeAmount: number;
}
