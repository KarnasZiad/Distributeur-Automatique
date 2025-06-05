import React from 'react';
import { Product } from '@/types/VendingMachine';

interface ProductCardProps {
  product: Product;
  onSelect: (product: Product) => void;
  disabled: boolean;
}

const ProductCard = ({ product, onSelect, disabled }: ProductCardProps) => {
  // Images statiques existantes
  const getProductImage = (id: string) => {
    return `/images/products/${id}.jpg`;
  };

  return (
    <div 
      className={`bg-white rounded-lg shadow p-4 cursor-pointer transition-all
        ${disabled ? 'opacity-50 cursor-not-allowed' : 'hover:shadow-lg'}
      `}
      onClick={() => !disabled && onSelect(product)}
    >
      <div className="relative w-full h-48 mb-4">
        <img
          src={getProductImage(product.id)}
          alt={product.name}
          className="w-full h-full object-cover rounded-md"
        />
      </div>
      <h3 className="text-lg font-semibold mb-2">{product.name}</h3>
      <p className="text-gray-600 mb-2">{product.description}</p>
      <div className="flex justify-between items-center">
        <span className="text-xl font-bold">{product.price.toFixed(2)} MAD</span>
        <span className="text-sm text-gray-500">Stock: {product.stock}</span>
      </div>
    </div>
  );
};

export default ProductCard;
