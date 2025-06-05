
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
    <Card className="w-full bg-gradient-to-br from-green-50 to-emerald-100 border-2 border-green-200">
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-green-800">
          <Receipt className="h-6 w-6" />
          Récapitulatif
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
            <div className="space-y-2 max-h-48 overflow-y-auto">
              {cartItems.map((item) => (
                <div key={item.product.id} className="flex items-center justify-between bg-white p-3 rounded-lg shadow-sm">
                  <div className="flex-1">
                    <div className="font-medium text-sm">{item.product.name}</div>
                    <div className="text-xs text-gray-600">
                      {item.quantity} × {item.product.price.toFixed(2)} MAD
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="font-bold text-green-600">
                      {(item.product.price * item.quantity).toFixed(2)} MAD
                    </span>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => onRemoveItem(item.product.id)}
                      className="h-6 w-6 p-0"
                    >
                      <Trash2 className="h-3 w-3" />
                    </Button>
                  </div>
                </div>
              ))}
            </div>

            <Separator />

            <div className="space-y-2">
              <div className="flex justify-between text-sm">
                <span>Total à payer:</span>
                <span className="font-bold">{totalAmount.toFixed(2)} MAD</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>Solde disponible:</span>
                <span className="font-bold text-blue-600">{balance.toFixed(2)} MAD</span>
              </div>
              {change >= 0 && (
                <div className="flex justify-between text-sm">
                  <span>Monnaie à rendre:</span>
                  <span className="font-bold text-green-600">{change.toFixed(2)} MAD</span>
                </div>
              )}
            </div>

            <div className="flex gap-2">
              <Button
                onClick={onClearCart}
                variant="outline"
                className="flex-1"
              >
                <Trash2 className="h-4 w-4 mr-2" />
                Vider
              </Button>
              <Button
                onClick={onConfirmPurchase}
                disabled={!canPurchase}
                className="flex-2 bg-green-600 hover:bg-green-700"
              >
                <ArrowRight className="h-4 w-4 mr-2" />
                Confirmer l'achat
              </Button>
            </div>
          </>
        )}
      </CardContent>
    </Card>
  );
};

export default TransactionSummary;
