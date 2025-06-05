import React from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Coins, Plus } from 'lucide-react';
import { Coin } from '@/types/VendingMachine';

interface CoinSlotProps {
  balance: number;
  onCoinInsert: (amount: number) => void;
  onCancel: () => void;
  validCoins: number[];
}

const CoinSlot: React.FC<CoinSlotProps> = ({ balance, onCoinInsert, onCancel, validCoins }) => {
  const coins: Coin[] = validCoins.map((v) => ({ value: v, label: `${v.toLocaleString('fr-FR', { minimumFractionDigits: 1 })} MAD` }));

  return (
    <Card className="w-full bg-gradient-to-br from-blue-50 to-indigo-100 border-2 border-blue-200">
      <CardHeader className="text-center">
        <CardTitle className="flex items-center justify-center gap-2 text-blue-800">
          <Coins className="h-6 w-6" />
          Insertion des pièces
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="text-center">
          <div className="text-2xl font-bold text-green-600 mb-2">
            {balance.toFixed(2)} MAD
          </div>
          <div className="text-sm text-gray-600">Solde disponible</div>
        </div>
        
        <div className="grid grid-cols-3 gap-2">
          {coins.map((coin) => (
            <Button
              key={coin.value}
              onClick={() => onCoinInsert(coin.value)}
              className="h-16 bg-gradient-to-br from-yellow-400 to-yellow-600 hover:from-yellow-500 hover:to-yellow-700 text-yellow-900 font-bold shadow-lg transition-all duration-200 hover:scale-105"
              variant="secondary"
            >
              <div className="flex flex-col items-center">
                <Plus className="h-4 w-4 mb-1" />
                <span className="text-xs">{coin.label}</span>
              </div>
            </Button>
          ))}
        </div>
        
        <Button 
          onClick={onCancel}
          className="w-full bg-red-500 hover:bg-red-600 text-white font-semibold py-3"
          disabled={balance === 0}
        >
          Annuler & Récupérer la monnaie
        </Button>
      </CardContent>
    </Card>
  );
};

export default CoinSlot;
