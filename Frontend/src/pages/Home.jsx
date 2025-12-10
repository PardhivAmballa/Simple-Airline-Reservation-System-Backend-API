// src/pages/Home.jsx
import React, { useEffect, useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getAllFlights } from "../api/flightApi";
import { AuthContext } from "../context/AuthContext";

export default function Home() {
  const [featured, setFeatured] = useState([]);
  const [loading, setLoading] = useState(false);
  const { user } = useContext(AuthContext);
  const nav = useNavigate();

  useEffect(() => {
    (async () => {
      setLoading(true);
      try {
        const res = await getAllFlights();
        // pick first 3 flights as featured (safe handling)
        const list = res.data || [];
        setFeatured(list.slice(0, 3));
      } catch (err) {
        console.error("Failed to load flights:", err);
        setFeatured([]);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const goToFlights = () => nav("/");

  return (
    <div style={{ fontFamily: "system-ui, sans-serif", padding: 24, color: "#222" }}>
      {/* Hero */}
      <div style={{
        display: "flex",
        flexDirection: "column",
        gap: 12,
        padding: 24,
        borderRadius: 8,
        background: "linear-gradient(90deg,#ffffff,#f7f7ff)",
        boxShadow: "0 4px 18px rgba(0,0,0,0.06)",
        marginBottom: 20
      }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <div>
            <h1 style={{ margin: 0, fontSize: 28 }}>Simple Airline Reservation</h1>
            <p style={{ margin: "6px 0 0 0", color: "#555" }}>Search, book and manage flights — lightweight demo app.</p>
          </div>

          <div style={{ display: "flex", gap: 8 }}>
            <button onClick={goToFlights} style={ctaBtnStyle}>Browse Flights</button>
            {!user ? (
              <>
                <Link to="/login"><button style={ghostBtnStyle}>Login</button></Link>
                <Link to="/register"><button style={ghostBtnStyle}>Register</button></Link>
              </>
            ) : (
              <>
                <div style={{ alignSelf: "center", color: "#333" }}>Hello, <strong>{user.username}</strong></div>
                {user.role === "ADMIN" && <Link to="/admin"><button style={ctaSmall}>Admin</button></Link>}
              </>
            )}
          </div>
        </div>

        <div style={{ display: "flex", gap: 12, flexWrap: "wrap", marginTop: 12 }}>
          <div style={infoCardStyle}>
            <strong>Quick Search</strong>
            <div style={{ color: "#666", marginTop: 6 }}>Find flights by source & destination — try BLR, HYD, BOM etc.</div>
          </div>
          <div style={infoCardStyle}>
            <strong>Secure Booking</strong>
            <div style={{ color: "#666", marginTop: 6 }}>Basic-auth protected endpoints for demo — consider sessions/JWT in production.</div>
          </div>
          <div style={infoCardStyle}>
            <strong>Admin Tools</strong>
            <div style={{ color: "#666", marginTop: 6 }}>Add / update flights and manage bookings (admin only).</div>
          </div>
        </div>
      </div>

      {/* Featured Flights */}
      <section>
        <h2 style={{ marginTop: 0 }}>Featured Flights</h2>

        {loading ? <p>Loading featured flights…</p> : (
          featured.length === 0 ? (
            <p>No flights to show right now — <Link to="/">browse all flights</Link>.</p>
          ) : (
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: 12 }}>
              {featured.map((f) => {
                const id = f.flightId ?? f.id;
                const airline = f.airline ?? "Unknown airline";
                const route = Array.isArray(f.route) ? `${f.route[0]} → ${f.route[f.route.length - 1]}` : "Route unknown";
                return (
                  <div key={id} style={{
                    border: "1px solid #e6e6e6",
                    borderRadius: 8,
                    padding: 12,
                    boxShadow: "0 2px 8px rgba(0,0,0,0.04)"
                  }}>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                      <div>
                        <div style={{ fontSize: 14, color: "#666" }}>{airline}</div>
                        <div style={{ fontWeight: 700, fontSize: 16 }}>{route}</div>
                        <div style={{ color: "#777", marginTop: 6 }}>Status: {f.status ?? "Unknown"}</div>
                      </div>
                      <div style={{ textAlign: "right" }}>
                        <div style={{ fontWeight: 700 }}>{f.price != null ? `₹${f.price}` : "N/A"}</div>
                        <div style={{ fontSize: 12, color: "#666" }}>{id}</div>
                      </div>
                    </div>

                    <div style={{ marginTop: 10, display: "flex", gap: 8 }}>
                      <Link to={`/flights/${encodeURIComponent(id)}`}><button style={miniBtn}>View</button></Link>
                      <button style={miniPrimary} onClick={() => {
                        if (!user) return nav("/login");
                        // navigate to flight detail which contains Book action
                        nav(`/flights/${encodeURIComponent(id)}`);
                      }}>Book</button>
                    </div>
                  </div>
                );
              })}
            </div>
          )
        )}
      </section>

      {/* Footer quick links */}
      <footer style={{ marginTop: 28, paddingTop: 18, borderTop: "1px dashed #eee", color: "#666" }}>
        <div style={{ display: "flex", gap: 12, alignItems: "center", flexWrap: "wrap" }}>
          <Link to="/flights">Flights</Link>
          <Link to="/my-bookings">My Bookings</Link>
          {user?.role === "ADMIN" && <Link to="/admin">Admin</Link>}
          <span style={{ marginLeft: "auto" }}>© Simple Airline — Demo</span>
        </div>
      </footer>
    </div>
  );
}

/* inline styles (small helpers) */
const ctaBtnStyle = {
  background: "#3b82f6",
  color: "white",
  border: "none",
  padding: "8px 14px",
  borderRadius: 6,
  cursor: "pointer"
};
const ctaSmall = { ...ctaBtnStyle, padding: "6px 10px", fontSize: 13 };
const ghostBtnStyle = {
  background: "transparent",
  color: "#3b82f6",
  border: "1px solid #dbeafe",
  padding: "8px 12px",
  borderRadius: 6,
  cursor: "pointer"
};
const infoCardStyle = {
  padding: 10,
  borderRadius: 8,
  background: "#fff",
  border: "1px solid #f1f5f9",
  minWidth: 180,
  boxShadow: "0 1px 6px rgba(0,0,0,0.03)"
};
const miniBtn = {
  background: "#fff",
  color: "#3b82f6",
  border: "1px solid #e6eefc",
  padding: "6px 10px",
  borderRadius: 6,
  cursor: "pointer"
};
const miniPrimary = { ...miniBtn, background: "#3b82f6", color: "#fff", border: "none" };
