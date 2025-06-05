import React, { useState, useCallback } from 'react';
import { Button } from './ui/button';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';
import { Badge } from './ui/badge';
import { ShoppingCart, Package } from 'lucide-react';
import { Product } from '../types/VendingMachine';
import { toast } from './ui/use-toast';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./ui/alert-dialog";

interface ProductCatalogProps {
  products: Product[];
  balance: number;
  onProductSelect: (product: Product) => Promise<void>;
  loading?: boolean;
  onUpdateProducts?: (products: Product[]) => void;
}

const ProductCatalog: React.FC<ProductCatalogProps> = ({ 
  products, 
  balance, 
  onProductSelect,
  loading = false,
  onUpdateProducts
}) => {
  const [isProcessing, setIsProcessing] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const canAfford = useCallback((price: number) => balance >= price, [balance]);
  
  // Images réalistes pour chaque produit
  const getProductImage = (productId: string) => {
    const images: { [key: string]: string } = {
      '1': 'https://images.unsplash.com/photo-1554866585-cd94860890b7?w=200&h=200&fit=crop&crop=center', // Coca Cola
      '2': 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=200&h=200&fit=crop&crop=center', // Chips
      '3': 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=200&h=200&fit=crop&crop=center', // Eau minérale
      '4': 'https://images.unsplash.com/photo-1511381939415-e44015466834?w=200&h=200&fit=crop&crop=center', // Sandwich
  
      '5': 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=200&h=200&fit=crop&crop=center', // Biscuits
      '6': 'https://images.unsplash.com/photo-1539252554453-80ab65ce3586?w=200&h=200&fit=crop&crop=center',
      '7': 'https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?w=200&h=200&fit=crop&crop=center', // Chocolat
      '8': 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=200&h=200&fit=crop&crop=center', // Cacahuètes
    };
    return images[productId] || 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90?w=200&h=200&fit=crop&crop=center';
  };
  
  const resetPurchaseState = () => {
    setIsProcessing(false);
    setSelectedProduct(null);
  };

  const handleProductClick = async (product: Product) => {
    if (loading || isProcessing) {
      toast({
        title: 'Transaction en cours',
        description: 'Veuillez attendre la fin de la transaction en cours',
        variant: 'destructive',
      });
      return;
    }
    
    const affordable = canAfford(product.price);
    const inStock = product.stock > 0;
    
    if (!inStock || !affordable) {
      if (!inStock) {
        toast({ 
          title: 'Produit indisponible', 
          description: 'Ce produit est en rupture de stock' 
        });
      } else {
        toast({ 
          title: 'Solde insuffisant', 
          description: `Il vous manque ${(product.price - balance).toFixed(2)} MAD` 
        });
      }
      return;
    }

    setSelectedProduct(product);
  };

  const handleConfirmPurchase = async () => {
    if (!selectedProduct || isProcessing) return;
    
    try {
      setIsProcessing(true);
      const affordable = canAfford(selectedProduct.price);
      const inStock = selectedProduct.stock > 0;
      
      // Vérification supplémentaire avant l'achat
      if (!inStock || !affordable) {
        toast({
          title: 'Erreur',
          description: !inStock 
            ? 'Le produit n\'est plus en stock' 
            : 'Solde insuffisant pour cet achat',
          variant: 'destructive',
        });
        resetPurchaseState();
        return;
      }

      // Effectuer l'achat
      await onProductSelect(selectedProduct);
      
      // Mise à jour des produits si la fonction est disponible
      if (onUpdateProducts) {
        const updatedProducts = products.map(p => 
          p.id === selectedProduct.id 
            ? { ...p, stock: p.stock - 1 }
            : p
        );
        onUpdateProducts(updatedProducts);
      }

      // Notification de succès
      toast({
        title: 'Achat réussi',
        description: `Vous avez acheté ${selectedProduct.name}`,
      });

    } catch (error) {
      console.error('Erreur lors de l\'achat:', error);
      toast({
        title: 'Erreur',
        description: error instanceof Error ? error.message : 'Une erreur est survenue lors de l\'achat.',
        variant: 'destructive',
      });
    } finally {
      // Réinitialiser l'état après la fin de la transaction
      resetPurchaseState();
    }
  };

  const handleCancelPurchase = () => {
    if (isProcessing) return;
    resetPurchaseState();
  };

  return (
    <>
      <AlertDialog 
        open={selectedProduct !== null} 
        onOpenChange={(open) => {
          if (!open && !isProcessing) {
            handleCancelPurchase();
          }
        }}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>
              {isProcessing ? 'Transaction en cours...' : 'Confirmer l\'achat'}
            </AlertDialogTitle>
            <AlertDialogDescription>
              <div>
                Voulez-vous acheter {selectedProduct?.name} pour {selectedProduct?.price.toFixed(2)} MAD ?
              </div>
              {isProcessing && (
                <div className="mt-2 text-yellow-600 animate-pulse">
                  Transaction en cours, veuillez ne pas fermer cette fenêtre...
                </div>
              )}
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel 
              onClick={handleCancelPurchase}
              disabled={isProcessing}
            >
              Annuler
            </AlertDialogCancel>
            <AlertDialogAction 
              onClick={handleConfirmPurchase}
              disabled={isProcessing || !selectedProduct || (selectedProduct && (!canAfford(selectedProduct.price) || selectedProduct.stock <= 0))}
              className={`${isProcessing ? 'opacity-50 cursor-not-allowed' : ''} bg-green-600 hover:bg-green-700`}
            >
              {isProcessing ? 'Traitement en cours...' : 'Confirmer l\'achat'}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <Card className="w-full">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-gray-800">
            <Package className="h-6 w-6" />
            Catalogue des produits
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            {products.map((product) => {
              const affordable = canAfford(product.price);
              const inStock = product.stock > 0;
              const isAvailable = inStock && affordable;
              
              return (
                <Card 
                  key={product.id}
                  onClick={() => isAvailable && handleProductClick(product)}
                  className={`transition-all duration-200 hover:shadow-lg ${
                    isAvailable
                      ? 'hover:scale-105 cursor-pointer border-green-200 hover:border-green-400' 
                      : 'opacity-75 cursor-not-allowed border-gray-200'
                  }`}
                >
                  <CardContent className="p-3">
                    <div className="aspect-square bg-gradient-to-br from-gray-100 to-gray-200 rounded-lg mb-2 overflow-hidden">
                      <img 
                        src={getProductImage(product.id)}
                        alt={product.name}
                        className="w-full h-full object-cover"
                        onError={(e) => {
                          e.currentTarget.style.display = 'none';
                          e.currentTarget.nextElementSibling?.classList.remove('hidden');
                        }}
                      />                    <div className="w-full h-full items-center justify-center" style={{ display: 'none' }}>
                      <Package className="h-8 w-8 text-gray-400" />
                    </div>
                    </div>
                    
                    <h3 className="font-semibold text-sm mb-2 text-center">
                      {product.name}
                    </h3>
                    
                    <div className="text-center mb-2">
                      <div className="text-lg font-bold text-green-600">
                        {product.price.toFixed(2)} MAD
                      </div>
                      <Badge 
                        variant={inStock ? (affordable ? "default" : "outline") : "destructive"}
                        className={`text-xs ${!inStock && 'bg-red-100'} ${!affordable && 'bg-yellow-50'}`}
                      >
                        {!inStock ? "Rupture" : `Stock: ${product.stock}`}
                      </Badge>
                    </div>
                    
                    <div className="mt-2 text-center">
                      <span className={`text-xs ${
                        !inStock 
                          ? 'text-red-500' 
                          : !affordable 
                            ? 'text-yellow-600' 
                            : 'text-green-600'
                      }`}>
                        {!inStock ? (
                          "Produit en rupture de stock"
                        ) : !affordable ? (
                          `Solde insuffisant (${balance.toFixed(2)} MAD)`
                        ) : (
                          "Cliquez pour sélectionner"
                        )}
                      </span>
                    </div>
                  </CardContent>
                </Card>
              );
            })}
          </div>
        </CardContent>
      </Card>
    </>
  );
};

export default ProductCatalog;
