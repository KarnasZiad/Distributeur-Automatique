import { Product, ApiResponse, PurchaseResult, TransactionStatus } from '../types/VendingMachine';

// Use relative path for API calls (will be proxied by Vite)
const API_BASE = '/api/vending';

const defaultHeaders = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
};

export async function fetchProducts(): Promise<ApiResponse<Product[]>> {
  console.log('Fetching products...');
  try {
    const res = await fetch(`${API_BASE}/products`, {
      headers: defaultHeaders
    });
    console.log('Response status:', res.status);
    
    const response = await res.json();
    
    if (!res.ok) {
      throw new Error(response.error?.message || response.message || 'Une erreur est survenue lors du chargement des produits');
    }
    
    console.log('Products loaded:', response);
    return response;
  } catch (error) {
    console.error('Failed to fetch products:', error);
    throw error instanceof Error 
      ? error 
      : new Error('Une erreur inattendue est survenue');
  }
}

export async function fetchBalance(): Promise<ApiResponse<number>> {
  console.log('Fetching balance');
  try {
    const res = await fetch(`${API_BASE}/balance`, {
      headers: defaultHeaders
    });
    
    const data = await res.json();
    
    if (!res.ok) {
      throw new Error(data.error?.message || data.message || 'Une erreur est survenue lors du chargement du solde');
    }
    
    console.log('Balance loaded:', data);
    return data;
  } catch (error) {
    console.error('Failed to fetch balance:', error);
    throw error instanceof Error 
      ? error 
      : new Error('Une erreur inattendue est survenue');
  }
}

export async function insertCoin(coinValue: number): Promise<ApiResponse<TransactionStatus>> {
  const formattedValue = coinValue.toFixed(1); // Ensure one decimal place
  console.log('Inserting coin:', formattedValue);
  try {
    const res = await fetch(`${API_BASE}/insert-coin?coinValue=${formattedValue}`, {
      method: 'POST',
      headers: defaultHeaders
    });
    
    const data = await res.json();
    
    if (!res.ok) {
      throw new Error(data.error?.message || data.message || 'Une erreur est survenue lors de l\'insertion de la pièce');
    }
    
    console.log('Coin inserted:', data);
    return data;
  } catch (error) {
    console.error('Failed to insert coin:', error);
    throw error instanceof Error 
      ? error 
      : new Error('Une erreur inattendue est survenue');
  }
}

export async function selectProduct(productId: string | number): Promise<ApiResponse<PurchaseResult>> {
  const id = typeof productId === 'string' ? parseInt(productId, 10) : productId;
  console.log('Selecting product:', id);
  try {
    const res = await fetch(`${API_BASE}/select-product/${id}`, {
      method: 'POST',
      headers: defaultHeaders,
      credentials: 'include'
    });

    const data: ApiResponse<PurchaseResult> = await res.json();
    console.log('Server response:', data);
    
    if (!res.ok) {
      throw new Error(data.error?.message || data.message || 'Erreur lors de la sélection du produit');
    }
    
    if (!data.success) {
      throw new Error(data.error?.message || 'La transaction a échoué');
    }
    
    if (!data.data?.purchasedProduct) {
      throw new Error('Données de transaction invalides');
    }

    console.log('Product selected successfully:', data);
    return data;
  } catch (error) {
    console.error('Failed to select product:', error);
    throw error instanceof Error 
      ? error 
      : new Error('Une erreur inattendue est survenue');
  }
}

export async function cancelTransaction(): Promise<ApiResponse<Record<string, number>>> {
  console.log('Canceling transaction');
  try {
    const res = await fetch(`${API_BASE}/cancel`, {
      method: 'POST',
      headers: defaultHeaders
    });
    
    const data = await res.json();
    
    if (!res.ok) {
      throw new Error(data.error?.message || data.message || 'Une erreur est survenue lors de l\'annulation');
    }
    
    console.log('Transaction canceled:', data);
    return data;
  } catch (error) {
    console.error('Failed to cancel transaction:', error);
    throw error instanceof Error 
      ? error 
      : new Error('Une erreur inattendue est survenue');
  }
}

export async function fetchTransactionStatus() {
  console.log('Fetching transaction status');
  try {
    const res = await fetch(`${API_BASE}/transaction-status`, {
      headers: defaultHeaders
    });
    if (!res.ok) {
      const error = await res.json();
      console.error('Error fetching transaction status:', error);
      throw new Error(error.message || 'Une erreur est survenue lors du chargement du statut');
    }
    const data = await res.json();
    console.log('Transaction status loaded:', data);
    return data;
  } catch (error) {
    console.error('Failed to fetch transaction status:', error);
    throw error;
  }
}

export async function fetchValidCoins() {
  console.log('Fetching valid coins');
  try {
    const res = await fetch(`${API_BASE}/valid-coins`, {
      headers: defaultHeaders
    });
    if (!res.ok) {
      const error = await res.json();
      console.error('Error fetching valid coins:', error);
      throw new Error(error.message || 'Une erreur est survenue lors du chargement des pièces valides');
    }
    const data = await res.json();
    console.log('Valid coins loaded:', data);
    return data;
  } catch (error) {
    console.error('Failed to fetch valid coins:', error);
    throw error;
  }
}
