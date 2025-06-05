# 🛒 Distributeur Automatique - Interface Web & API REST

Une application complète avec **interface graphique web moderne** et **API REST modulaire** pour un distributeur automatique qui gère les pièces, les produits, les transactions et le rendu de monnaie optimisé.

## 🎯 Fonctionnalités

- ✅ **Interface web interactive** et responsive
- ✅ Accepte des pièces valides (0.5, 1, 2, 5, 10 MAD)
- ✅ Affiche les produits disponibles avec prix et disponibilité
- ✅ Permet la sélection de produits si le solde est suffisant
- ✅ Distribue les produits et rend la monnaie optimisée
- ✅ Gère l'annulation de la transaction
- ✅ Animations et feedback visuel en temps réel
- ✅ Messages de notification dynamiques

## 🚀 Accès rapide

### Interface Web
- **URL principale** : `http://localhost:8080`
- **Interface moderne** avec design responsive
- **Interactions en temps réel** avec l'API

### API REST
- **Base URL** : `http://localhost:8080/api/vending`
- **Console H2** : `http://localhost:8080/h2-console`

## 🛠️ Technologies utilisées

### Backend
- **Spring Boot 3.2.0** - Framework principal
- **Spring Data JPA** - Persistance des données
- **H2 Database** - Base de données en mémoire
- **Maven** - Gestion des dépendances
- **Java 17** - Version Java

### Frontend
- **HTML5/CSS3** - Structure et style
- **JavaScript ES6+** - Logique interactive
- **Font Awesome** - Icônes
- **CSS Grid/Flexbox** - Layout responsive

## Structure du projet

```
src/
├── main/
│   ├── java/com/distributeur/
│   │   ├── controller/          # Contrôleurs REST
│   │   ├── service/             # Logique métier
│   │   ├── model/               # Entités JPA
│   │   ├── repository/          # Repositories JPA
│   │   ├── dto/                 # Data Transfer Objects
│   │   └── config/              # Configuration
│   └── resources/
│       └── application.properties
└── test/                        # Tests unitaires
```

## Installation et démarrage

### Prérequis
- Java 17 ou supérieur
- Maven 3.6 ou supérieur

### Démarrage
```bash
# Cloner le projet
cd Distributeur-Automatique

# Compiler et démarrer l'application
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## API Endpoints

### Base URL: `http://localhost:8080/api/vending`

### 1. Insérer une pièce
```http
POST /insert-coin?coinValue=2.0
```
**Pièces valides:** 0.5, 1.0, 2.0, 5.0, 10.0 MAD

### 2. Consulter les produits disponibles
```http
GET /products
```

### 3. Consulter le solde actuel
```http
GET /balance
```

### 4. Sélectionner un produit
```http
POST /select-product/{productId}
```

### 5. Annuler la transaction
```http
POST /cancel
```

### 6. Consulter le statut de la transaction
```http
GET /transaction-status
```

### 7. Consulter les pièces valides
```http
GET /valid-coins
```

## 🎮 Guide d'utilisation

### Interface Web (Recommandé)

1. **Accéder à l'interface** : `http://localhost:8080`
2. **Insérer des pièces** : Cliquer sur les boutons de pièces (0.5, 1, 2, 5, 10 MAD)
3. **Consulter le solde** : Affiché en temps réel dans l'en-tête
4. **Sélectionner un produit** : Cliquer sur une carte produit disponible
5. **Voir le résultat** : Modal avec détails de l'achat et monnaie rendue
6. **Annuler si nécessaire** : Bouton "Annuler Transaction"

### API REST (Pour développeurs)

#### Scénario complet d'achat

1. **Consulter les produits disponibles**
```bash
curl -X GET http://localhost:8080/api/vending/products
```

2. **Insérer des pièces**
```bash
curl -X POST "http://localhost:8080/api/vending/insert-coin?coinValue=5.0"
curl -X POST "http://localhost:8080/api/vending/insert-coin?coinValue=2.0"
```

3. **Vérifier le solde**
```bash
curl -X GET http://localhost:8080/api/vending/balance
```

4. **Acheter un produit (ID 1 - Coca-Cola 2.5 MAD)**
```bash
curl -X POST http://localhost:8080/api/vending/select-product/1
```

5. **Ou annuler la transaction**
```bash
curl -X POST http://localhost:8080/api/vending/cancel
```

## Produits pré-configurés

| ID | Produit | Prix (MAD) | Stock |
|----|---------|------------|-------|
| 1  | Coca-Cola | 2.5 | 10 |
| 2  | Eau Minérale | 1.0 | 15 |
| 3  | Chips | 3.0 | 8 |
| 4  | Chocolat | 4.5 | 12 |
| 5  | Café | 2.0 | 20 |
| 6  | Sandwich | 6.0 | 5 |
| 7  | Jus d'Orange | 3.5 | 7 |
| 8  | Biscuits | 2.5 | 9 |

## Tests

Exécuter les tests unitaires :
```bash
mvn test
```

## Console H2 (Développement)

Accéder à la console H2 : `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:vendingdb`
- **Username:** `sa`
- **Password:** (vide)

## Architecture

### Algorithme de rendu de monnaie
L'application utilise un algorithme glouton pour optimiser le rendu de monnaie :
1. Utilise les plus grosses pièces en premier
2. Vérifie la disponibilité dans l'inventaire
3. Calcule le nombre optimal de pièces à rendre
4. Retourne une erreur si la monnaie exacte ne peut pas être rendue

### Gestion des transactions
- Une seule transaction active à la fois
- Statuts : `IN_PROGRESS`, `COMPLETED`, `CANCELLED`
- Persistance des pièces insérées et du produit sélectionné
- Calcul automatique de la monnaie à rendre

## 🎨 Fonctionnalités de l'Interface Web

### Design et UX
- **Design moderne** avec dégradés et animations
- **Interface responsive** adaptée mobile/desktop
- **Icônes intuitives** pour chaque produit
- **Feedback visuel** pour toutes les actions

### Interactions dynamiques
- **Animations de pièces** lors de l'insertion
- **Mise à jour en temps réel** du solde et statut
- **Messages de notification** avec auto-disparition
- **Modal d'achat** avec détails complets
- **États visuels** des produits (disponible/indisponible/solde insuffisant)

### Fonctionnalités avancées
- **Historique des pièces** insérées avec compteurs
- **Calcul automatique** de la monnaie optimisée
- **Gestion d'erreurs** avec messages explicites
- **Loading states** pour les opérations asynchrones
- **Actualisation automatique** des données

## 💡 Points forts de l'implémentation

### Backend
1. **Modularité** : Séparation claire des responsabilités
2. **Robustesse** : Gestion d'erreurs complète
3. **Extensibilité** : Facile d'ajouter de nouveaux produits/fonctionnalités
4. **Performance** : Algorithme optimisé pour le rendu de monnaie
5. **Maintenabilité** : Code bien structuré et testé

### Frontend
1. **Expérience utilisateur** : Interface intuitive et moderne
2. **Réactivité** : Mise à jour en temps réel
3. **Accessibilité** : Design responsive et icônes claires
4. **Performance** : Chargement rapide et animations fluides
5. **Documentation** : README complet avec exemples

## 🎯 Exemple d'utilisation complète

### Via l'interface web
1. Ouvrir `http://localhost:8080`
2. Insérer 5.0 MAD + 2.0 MAD (solde: 7.0 MAD)
3. Cliquer sur "Coca-Cola" (2.5 MAD)
4. Voir la modal : Produit acheté + Monnaie rendue (4.5 MAD optimisée)

### Via l'API REST
```bash
# 1. Consulter les produits
GET /api/vending/products

# 2. Insérer des pièces
POST /api/vending/insert-coin?coinValue=5.0
POST /api/vending/insert-coin?coinValue=2.0

# 3. Vérifier le solde (7.0 MAD)
GET /api/vending/balance

# 4. Acheter un produit (Coca-Cola 2.5 MAD)
POST /api/vending/select-product/1
# Retourne : Produit + Monnaie rendue (4.5 MAD optimisée)
```

Le projet est **prêt pour la production** avec une architecture solide, une interface moderne, des tests complets et une documentation détaillée ! 🎉
