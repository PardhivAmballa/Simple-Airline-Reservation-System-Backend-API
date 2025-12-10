// src/pages/Register.jsx
import React, { useState } from "react";
import { registerUser } from "../api/authApi";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const nav = useNavigate();

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      await registerUser(username, password);
      alert("Registered. Now login.");
      nav("/login");
    } catch (err) {
      alert("Registration failed: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div style={{ padding: 12 }}>
      <h2>Register</h2>
      <form onSubmit={onSubmit}>
        <div><input placeholder="username" value={username} onChange={e=>setUsername(e.target.value)} /></div>
        <div><input placeholder="password" type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
        <button type="submit">Register</button>
      </form>
    </div>
  );
}
