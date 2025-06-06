
import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { Receipt, Trash2, ArrowRight } from 'lucide-react';
import { CartItem } from '@/types/VendingMachine';

interface TransactionSummaryProps {
  cartItems: CartItem[];
  balance: number;
  onRemoveItem: (productId: string) => void;
  onConfirmPurchase: () => void;
  onClearCart: () => void;
}

const TransactionSummary: React.FC<TransactionSummaryProps> = ({
  cartItems,
  balance,
  onRemoveItem,
  onConfirmPurchase,
  onClearCart
}) => {
  const totalAmount = cartItems.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
  const change = balance - totalAmount;
  const canPurchase = cartItems.length > 0 && balance >= totalAmount;

  return (
    <Card className="w-full bg-gradient-to-br from-green-50 to-emerald-100 border-2 border-green-200 relative group">
      {/* Indicateur de nombre d'articles */}
      {cartItems.length > 0 && (
        <div className="absolute -top-2 -right-2 bg-blue-500 text-white w-6 h-6 rounded-full flex items-center justify-center text-sm font-bold shadow-lg">
          {cartItems.reduce((sum, item) => sum + item.quantity, 0)}
        </div>
      )}
      
      <CardHeader>
        <CardTitle className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-green-800">
            <Receipt className="h-6 w-6" />
            Récapitulatif
          </div>
          {cartItems.length > 0 && (
            <span className="text-sm text-gray-600">
              {cartItems.length} article{cartItems.length > 1 ? 's' : ''}
            </span>
          )}
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {cartItems.length === 0 ? (
          <div className="text-center py-8 text-gray-500">
            <Receipt className="h-12 w-12 mx-auto mb-4 opacity-50" />
            <p>Aucun produit sélectionné</p>
          </div>
        ) : (
          <>
            <div className="space-y-2 max-h-48 overflow-y-auto pr-2">
              {cartItems.map((item) => (
                <div 
                  key={item.product.id} 
                  className="flex items-center justify-between bg-white p-3 rounded-lg shadow-sm hover:shadow-md transition-all duration-200 border border-transparent hover:border-green-200"
                >
                  <div className="flex-1">
                    <div className="font-medium text-sm flex items-center gap-2">
                      {item.product.name}
                      {item.quantity > 1 && (
                        <span className="px-1.5 py-0.5 bg-blue-100 text-blue-700 rounded-full text-xs font-bold">
                          x{item.quantity}
                        </span>
                      )}
                    </div>
                    <div className="text-xs text-gray-600 mt-1">
                      Prix unitaire: {item.product.price.toFixed(2)} MAD
                    </div>
                  </div>
                  <div className="flex flex-col items-end gap-1">
                    <span className="font-bold text-green-600 text-sm">
                      {(item.product.price * item.quantity).toFixed(2)} MAD
                    </span>
                    <Button
                      size="sm"
                      variant="ghost"
                      onClick={() => onRemoveItem(item.product.id)}
                      className="h-6 px-2 text-red-600 hover:text-red-700 hover:bg-red-50"
                    >
                      <Trash2 className="h-3 w-3 mr-1" />
                      <span className="text-xs">Retirer</span>
                    </Button>
                  </div>
                </div>
              ))}
            </div>

            <Separator />

            <div className="space-y-3 bg-white p-4 rounded-lg">
              <div className="flex justify-between text-sm items-center">
                <span className="text-gray-600">Total à payer</span>
                <div className="flex items-center gap-2">
                  <span className="text-lg font-bold">{totalAmount.toFixed(2)} MAD</span>
                </div>
              </div>
              
              <Separator className="my-2" />
              
              <div className="flex justify-between text-sm items-center">
                <span className="text-gray-600">Solde disponible</span>
                <div className="flex items-center gap-2">
                  <div className={`h-2 w-2 rounded-full ${balance >= totalAmount ? 'bg-green-500' : 'bg-red-500'}`} />
                  <span className={`text-lg font-bold ${balance >= totalAmount ? 'text-green-600' : 'text-red-600'}`}>
                    {balance.toFixed(2)} MAD
                  </span>
                </div>
              </div>
              
              {change >= 0 && (
                <>
                  <Separator className="my-2" />
                  <div className="flex justify-between text-sm items-center">
                    <span className="text-gray-600">Monnaie à rendre</span>
                    <span className="text-lg font-bold text-blue-600">{change.toFixed(2)} MAD</span>
                  </div>
                </>
              )}
            </div>

            <div className="flex gap-2 mt-6">
              <Button
                onClick={onClearCart}
                variant="outline"
                className="flex-1 border-red-200 text-red-600 hover:bg-red-50 hover:text-red-700"
                disabled={cartItems.length === 0}
              >
                <Trash2 className="h-4 w-4 mr-2" />
                Vider le panier
              </Button>
              <Button
                onClick={onConfirmPurchase}
                disabled={!canPurchase}
                className="flex-[2] min-w-[120px] h-10 bg-green-600 hover:bg-green-700 font-semibold"
              >
                <span className="flex items-center justify-center gap-2 px-2">
                  <ArrowRight className="h-4 w-4 flex-shrink-0" />
                  <span className="whitespace-nowrap">Acheter</span>
                  {canPurchase && (
                    <span className="ml-1 text-xs bg-green-500 px-2 py-0.5 rounded-full whitespace-nowrap">
                      {totalAmount.toFixed(2)} MAD
                    </span>
                  )}
                </span>
              </Button>
            </div>
          </>
        )}
      </CardContent>
    </Card>
  );
};

export default TransactionSummary;
