import React, { useState, useEffect } from 'react';
import { toast } from '@/hooks/use-toast';
import CoinSlot from '@/components/CoinSlot';
import ProductCatalog from '@/components/ProductCatalog';
import TransactionSummary from '@/components/TransactionSummary';
import { Product } from '@/types/VendingMachine';
import {
  fetchProducts,
  fetchBalance,
  insertCoin,
  selectProduct,
  fetchValidCoins,
  bulkPurchase
} from '@/api/vending';

const Index = () => {
  const [balance, setBalance] = useState(0);
  const [products, setProducts] = useState<Product[]>([]);
  const [validCoins, setValidCoins] = useState<number[]>([0.5, 1, 2, 5, 10]);
  const [loading, setLoading] = useState(false);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);

  // Fetch products, balance, and valid coins on mount
  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [prodRes, balRes, coinsRes] = await Promise.all([
        fetchProducts(),
        fetchBalance(),
        fetchValidCoins()
      ]);

      if (prodRes && prodRes.data) {
        const mappedProducts = prodRes.data.map((product: any) => ({
          id: product.id.toString(),
          name: product.name,
          price: Number(product.price),
          stock: product.quantity ?? 0,
          description: product.description ?? ''
        }));
        setProducts(mappedProducts);
      }

      if (balRes.success) {
        setBalance(Number(balRes.data));
      }

      if (coinsRes.success && Array.isArray(coinsRes.data)) {
        setValidCoins(coinsRes.data);
      }
    } catch (error) {
      console.error('Error loading data:', error);
      toast({ 
        title: 'Erreur', 
        description: "Erreur de connexion au backend.",
        variant: 'destructive' 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCoinInsert = async (amount: number) => {
    try {
      setLoading(true);
      const value = Number(amount.toFixed(1));
      const res = await insertCoin(value);
      
      if (res.success) {
        setBalance(Number(res.data.totalInserted));
        toast({ 
          title: 'Pièce insérée', 
          description: `${value.toFixed(2)} MAD ajouté.` 
        });
      } else {
        toast({ 
          title: 'Erreur', 
          description: res.message || 'Pièce non acceptée.',
          variant: 'destructive' 
        });
      }
    } catch (error) {
      console.error('Error inserting coin:', error);
      toast({ 
        title: 'Erreur', 
        description: 'Erreur lors de l\'insertion.',
        variant: 'destructive' 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleProductSelect = async (product: Product) => {
    // Ajouter le produit au panier
    const existingItem = cartItems.find(item => item.product.id === product.id);
    
    if (existingItem) {
      // Mettre à jour la quantité si le produit est déjà dans le panier
      setCartItems(prevItems => 
        prevItems.map(item => 
          item.product.id === product.id 
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      );
    } else {
      // Ajouter un nouveau produit au panier
      setCartItems(prevItems => [...prevItems, { product, quantity: 1 }]);
    }

    toast({
      title: 'Produit ajouté',
      description: `${product.name} a été ajouté au panier`
    });
  };

  const handleRemoveItem = (productId: string) => {
    setCartItems(prevItems => prevItems.filter(item => item.product.id !== productId));
  };

  const handleClearCart = () => {
    setCartItems([]);
  };

  const handleConfirmPurchase = async () => {
    if (loading || cartItems.length === 0) return;

    try {
      setLoading(true);

      // Préparer les items pour l'achat en bloc
      const purchaseItems = cartItems.flatMap(item => 
        Array(item.quantity).fill({ productId: item.product.id })
      );

      // Effectuer l'achat en bloc
      const result = await bulkPurchase(purchaseItems);
      
      if (result.success) {
        // Rafraîchir les données
        await loadData();
        
        // Vider le panier après l'achat réussi
        setCartItems([]);

        toast({
          title: 'Achat réussi',
          description: 'Tous vos produits ont été distribués',
          variant: 'success'
        });
      }
    } catch (error: any) {
      console.error('Purchase error:', error);
      toast({ 
        title: 'Erreur lors de l\'achat', 
        description: error?.message || 'Une erreur est survenue',
        variant: 'destructive'
      });
      // Rafraîchir les données en cas d'erreur
      await loadData();
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 to-slate-200 p-4">
      <div className="max-w-7xl mx-auto">
        <header className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">
            Bienvenu !
          </h1>
          <p className="text-gray-600">
            Sélectionnez vos produits préférés en toute simplicité
          </p>
        </header>
        
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-1">
            <CoinSlot 
              balance={balance}
              onCoinInsert={handleCoinInsert}
              validCoins={validCoins}
              loading={loading}
            />
          </div>
          
          <div className="lg:col-span-2">
            <ProductCatalog
              products={products}
              balance={balance}
              onProductSelect={handleProductSelect}
              loading={loading}
              onUpdateProducts={setProducts}
            />
          </div>

          <div className="lg:col-span-3">
            <TransactionSummary
              cartItems={cartItems}
              balance={balance}
              onRemoveItem={handleRemoveItem}
              onConfirmPurchase={handleConfirmPurchase}
              onClearCart={handleClearCart}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Index;
