// API calls for statistics endpoints

// Remplacer l'API_BASE absolu par un chemin relatif pour profiter du proxy Vite
const API_BASE = '/api/vending';

export async function fetchStatistics() {
  const res = await fetch(`${API_BASE}/statistics`);
  return res.json();
}

export async function fetchDailyStatistics() {
  const res = await fetch(`${API_BASE}/statistics/daily`);
  return res.json();
}

export async function fetchProductStatistics() {
  const res = await fetch(`${API_BASE}/statistics/products`);
  return res.json();
}

export async function fetchHealth() {
  const res = await fetch(`${API_BASE}/health`);
  return res.json();
}

export async function fetchAlerts() {
  const res = await fetch(`${API_BASE}/alerts`);
  return res.json();
}

export async function runDiagnostic() {
  const res = await fetch(`${API_BASE}/diagnostic`, { method: 'POST' });
  return res.json();
}

export async function fetchCoinInventory() {
  const res = await fetch(`${API_BASE}/coin-inventory`);
  return res.json();
}

export async function fetchTransactionHistory(limit = 10, status?: string) {
  const url = new URL(`${API_BASE}/history`);
  url.searchParams.set('limit', limit.toString());
  if (status) url.searchParams.set('status', status);
  const res = await fetch(url.toString());
  return res.json();
}

export async function resetSystem() {
  const res = await fetch(`${API_BASE}/reset`, { method: 'POST' });
  return res.json();
}

export async function exportData(format: string = 'json') {
  const url = new URL(`${API_BASE}/export`);
  url.searchParams.set('format', format);
  const res = await fetch(url.toString());
  return res.json();
}
