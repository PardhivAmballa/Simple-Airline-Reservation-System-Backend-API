// src/api/authApi.js
import api from "./api";

// Register public user
export const registerUser = (username, password) =>
  api.post("/api/users/register", { username, password });

// Register admin (admin-only; frontend should call only if current user is admin)
export const registerAdmin = (username, password) =>
  api.post("/api/users/admin/register", { username, password });

// Because backend uses HTTP Basic, there is no login endpoint.
// We'll validate credentials by hitting a protected endpoint (e.g. /api/flights).
export const validateCredentials = (username, password) => {
  const token = btoa(`${username}:${password}`);
  return api.get("/api/flights", {
    headers: { Authorization: `Basic ${token}` }
  });
};
