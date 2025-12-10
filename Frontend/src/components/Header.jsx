// src/components/Header.jsx
import React, { useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

export default function Header() {
  const { user, logout } = useContext(AuthContext);
  const nav = useNavigate();

  const onLogout = () => {
    logout();
    nav("/login");
  };

  return (
    <header style={{ padding: 12, borderBottom: "1px solid #ccc", marginBottom: 12 }}>
      <Link to="/">Home</Link> {" | "}
      <Link to="/flights">Flights</Link> {" | "}
      {user && <Link to="/my-bookings">My Bookings</Link>} {" | "}
      {user && user.role === "ADMIN" && <Link to="/admin">Admin</Link>}
      <span style={{ float: "right" }}>
        {user ? (
          <>
            <strong>{user.username}</strong> {" "}
            <button onClick={onLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link> {" / "}
            <Link to="/register">Register</Link>
          </>
        )}
      </span>
    </header>
  );
}
