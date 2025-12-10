// src/context/AuthContext.jsx
import React, { createContext, useState, useEffect } from "react";
import { validateCredentials } from "../api/authApi";

export const AuthContext = createContext();

/*
  NOTE:
  - We store base64(username:password) in localStorage as "basicAuth" for prototype convenience.
  - We also store a simple { username, role } in localStorage as "user" so UI can read role.
  - For role: because backend doesn't return role during Basic auth, we can attempt to infer role
    by calling an admin-only endpoint (like GET /api/bookings/all) after login and check 200 vs 403.
    Here we'll attempt a simple design: after validating credentials we attempt to GET /api/bookings/all.
*/
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem("user");
    return raw ? JSON.parse(raw) : null;
  });

  useEffect(() => {
    // keep user in sync with localStorage across tabs (optional)
    const onStorage = () => {
      const raw = localStorage.getItem("user");
      setUser(raw ? JSON.parse(raw) : null);
    };
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
  }, []);

  const login = async (username, password) => {
    const token = btoa(`${username}:${password}`);
    // validate by calling protected endpoint
    try {
      await validateCredentials(username, password); // returns flights if ok
      // Try to infer role: try admin-only endpoint
      let role = "USER";
      try {
        // Try to call admin-only endpoint (will succeed only if admin)
        await fetch(`${(import.meta.env.VITE_API_BASE || "http://localhost:8080")}/api/bookings/all`, {
          method: "GET",
          headers: { Authorization: `Basic ${token}`, "Content-Type": "application/json" }
        }).then(res => {
          if (res.status === 200) role = "ADMIN";
        }).catch(() => {});
      } catch (err) {}
      localStorage.setItem("basicAuth", token);
      const userObj = { username, role };
      localStorage.setItem("user", JSON.stringify(userObj));
      setUser(userObj);
      return userObj;
    } catch (err) {
      throw new Error("Invalid credentials");
    }
  };

  const logout = () => {
    localStorage.removeItem("basicAuth");
    localStorage.removeItem("user");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
