// src/pages/Login.jsx
import React, { useState, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useContext(AuthContext);
  const nav = useNavigate();
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await login(username, password);
      nav("/");
    } catch (err) {
      alert("Login failed: " + err.message);
    } finally { setLoading(false); }
  };

  return (
    <div style={{ padding: 12 }}>
      <h2>Login</h2>
      <form onSubmit={onSubmit}>
        <div>
          <input placeholder="username" value={username} onChange={(e)=>setUsername(e.target.value)} />
        </div>
        <div>
          <input placeholder="password" type="password" value={password} onChange={(e)=>setPassword(e.target.value)} />
        </div>
        <div>
          <button type="submit" disabled={loading}>{loading ? "Signing in..." : "Login"}</button>
        </div>
      </form>

      <p style={{ color: "orange" }}>
        <strong>Dev note:</strong> This prototype stores basic auth (base64 username:password) in localStorage.
        For production, switch to sessions or JWT.
      </p>
    </div>
  );
}
