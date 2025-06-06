#  Distributeur Automatique - Interface Web & API REST

Une application complète de distributeur automatique avec une interface web moderne et une API REST modulaire. Elle permet la gestion des pièces, des produits, des transactions, et le rendu de monnaie optimisé.

---

##  Fonctionnalités principales

###  Côté Utilisateur
- Insertion de pièces valides : `0.5`, `1`, `2`, `5`, `10` MAD
- Affichage dynamique des produits (prix, disponibilité, stock)
- Achat d’un produit si solde suffisant
- Rendu de monnaie optimisé (greedy algorithm)
- Annulation de la transaction avec remboursement

### Côté Backend
- Une seule transaction active à la fois
- Statuts gérés : `IN_PROGRESS`, `COMPLETED`, `CANCELLED`
- Persistance de l'état de la transaction, du solde et des pièces insérées
- Gestion d'erreurs robuste

---

##  Interface Web

###  État de l'interface

| État Produit         | Couleur | Action possible                  |
|----------------------|--------|----------------------------------|
| Disponible           | 🟩 Vert  | Achat possible                   |
| Solde insuffisant    | 🟧 Orange | Non cliquable, aide affichée     |
| Rupture de stock     | 🟥 Rouge  | Non cliquable, opacité réduite   |

###  Flux utilisateur
1. **État initial** : Solde à `0.00 MAD`, produits inaccessibles
2. **Insertion de pièces** : mise à jour du solde et interface
3. **Sélection de produit** : spinner + modal de confirmation
4. **Rendu monnaie** : modal affichant :
   - Produit acheté
   - Montant payé
   - Monnaie rendue (pièces)
   - Solde réinitialisé

###  En-tête et Composants
- Titre avec icône de distributeur
- Affichage du **solde en temps réel**
- Grille responsive de **cartes produits**
- **Boutons de pièces** interactifs
- **Actions globales** : Annuler / Actualiser
- Historique des **pièces insérées**
- Notifications (Succès / Info / Erreur / Alerte)

---

##  Technologies Utilisées

###  Backend
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Java 17**
- **Maven**

###  Frontend
- **React + TypeScript**
- **Vite**
- **Tailwind CSS**
- **shadcn-ui**

---


## Installation & Démarrage

###  Prérequis
- Java 17+
- Maven 3.6+
- Node.js 18+ (pour le frontend)

##  Démarrer le Backend

### Cloner le projet
git clone https://github.com/KarnasZiad/Distributeur-Automatique.git
cd Distributeur-Automatique

### Compiler et lancer
mvn spring-boot:run


##  Démarrer le Frontend

cd frontend
npm install
npm run dev


##  API Endpoints
Base URL : http://localhost:8080/api/vending

Action	Méthode	Endpoint
 Insérer une pièce	POST	/insert-coin?coinValue=2.0
 Lister les produits	GET	/products
 Consulter le solde	GET	/balance
 Acheter un produit	POST	/select-product/{productId}
 Annuler la transaction	POST	/cancel
 Statut de la transaction	GET	/transaction-status
 Liste des pièces valides	GET	/valid-coins

##  Produits pré-configurés

| ID | Produit        | Prix (MAD) | Stock |
|----|----------------|------------|-------|
| 1  | Coca-Cola      | 2.5        | 10    |
| 2  | Eau Minérale   | 1.0        | 15    |
| 3  | Chips          | 3.0        | 8     |
| 4  | Chocolat       | 4.5        | 12    |
| 5  | Café           | 2.0        | 20    |
| 6  | Sandwich       | 6.0        | 5     |
| 7  | Jus d'Orange   | 3.5        | 7     |
| 8  | Biscuits       | 2.5        | 9     |

---

##  Algorithme de rendu de monnaie

- Approche **gloutonne**
- Utilise d'abord les pièces les plus grosses
- Vérifie la disponibilité des pièces dans l'inventaire
- Retourne une erreur si le rendu exact est impossible

---

##  Gestion des transactions

- Une seule transaction active à la fois
- **Statuts** :
  - `IN_PROGRESS`
  - `COMPLETED`
  - `CANCELLED`
- Solde, pièces et produit persistés temporairement
- Rendu de monnaie calculé automatiquement

---

###  Tests unitaires

mvn test


##  Interface Web

###  Structure de l'interface

#### En-tête
- Icône du distributeur
- Solde affiché en temps réel
- Statut dynamique (ex : `"Prêt"`, `"Achat en cours"`, `"Transaction annulée"`)

#### Produits
- Grille responsive de cartes produits
- 3 États visuels :
  - ✅ **Disponible** (bordure verte)
  - ⚠️ **Solde insuffisant** (bordure orange)
  - ❌ **Rupture de stock** (bordure rouge)

#### Panneau de contrôle
- Boutons de pièces : `0.5`, `1`, `2`, `5`, `10 MAD`
- Boutons d’action : `Annuler`, `Actualiser`
- Historique des pièces insérées

---

###  Flux utilisateur

#### 🟢 État initial
- Solde = `0.00 MAD`
- Statut : `"Prêt"`
- Produits tous non accessibles

#### ➕ Insertion
- Animation sur le bouton de pièce
- Mise à jour du solde
- Produits accessibles deviennent cliquables

#### 🛒 Achat
- Clic sur un produit
- Modal de confirmation avec animation (spinner)

#### ✅ Résultat
- Détail de l’achat :
  - Produit acheté
  - Montant payé
  - Monnaie rendue (ex: `2.0 MAD + 0.5 MAD`)
- Réinitialisation automatique du solde


![imgDA](https://github.com/user-attachments/assets/31a1c183-ed14-4e33-82fc-3a0ff0b4ed1e)






