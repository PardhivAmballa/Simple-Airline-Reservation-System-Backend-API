// src/pages/FlightsList.jsx
import React, { useEffect, useState } from "react";
import { getAllFlights, searchFlights } from "../api/flightApi";
import { Link } from "react-router-dom";

export default function FlightsList() {
  const [flights, setFlights] = useState([]);
  const [source, setSource] = useState("");
  const [destination, setDestination] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const res = await getAllFlights();
        setFlights(res.data || []);
      } catch (err) {
        console.error("Failed to load flights:", err);
        setFlights([]);
      }
    })();
  }, []);

  const onSearch = async (e) => {
    e?.preventDefault();
    try {
      const res = await searchFlights(source, destination);
      setFlights(res.data || []);
    } catch (err) {
      console.error("Search failed:", err);
      alert("Search failed");
    }
  };

  const onReset = async () => {
    setSource("");
    setDestination("");
    try {
      const res = await getAllFlights();
      setFlights(res.data || []);
    } catch (err) {
      console.error("Failed to reload flights:", err);
    }
  };

  return (
    <div style={{ padding: 12 }}>
      <h2>Flights</h2>

      <form onSubmit={onSearch} style={{ marginBottom: 12 }}>
        <input
          placeholder="source"
          value={source}
          onChange={(e) => setSource(e.target.value)}
          style={{ marginRight: 8 }}
        />
        <input
          placeholder="destination"
          value={destination}
          onChange={(e) => setDestination(e.target.value)}
          style={{ marginRight: 8 }}
        />
        <button type="submit">Search</button>
        <button type="button" onClick={onReset} style={{ marginLeft: 8 }}>
          Reset
        </button>
      </form>

      <div style={{ marginTop: 12 }}>
        {flights.length === 0 ? (
          <p>No flights found</p>
        ) : (
          flights.map((f) => {
            // backend flight shape uses `flightId`, `airline`, `route` (array)
            const id = f.flightId ?? f.id;
            const airline = f.airline ?? f.name;
            const route = Array.isArray(f.route) ? f.route : [];
            const sourceCity = route?.[0] ?? "N/A";
            const destCity = route?.[route.length - 1] ?? "N/A";

            return (
              <div key={id} style={{ border: "1px solid #ddd", padding: 12, marginBottom: 12 }}>
                <h3 style={{ margin: 0 }}>
                  {airline} {id ? <small>({id})</small> : null}
                </h3>
                <p style={{ margin: "8px 0" }}>
                  <strong>
                    {sourceCity} → {destCity}
                  </strong>
                </p>
                <p style={{ margin: "8px 0" }}>Status: {f.status ?? "Unknown"}</p>
                <Link to={`/flights/${encodeURIComponent(id)}`}>View</Link>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}
