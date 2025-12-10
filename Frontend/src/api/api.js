// src/api/api.js
import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE,
  headers: {
    "Content-Type": "application/json"
  }
});

// Attach Basic Auth header from localStorage if present
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("basicAuth");
  if (token) {
    config.headers.Authorization = `Basic ${token}`;
  }
  return config;
});

export default api;
