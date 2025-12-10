// src/pages/FlightDetail.jsx
import React, { useEffect, useState, useContext } from "react";
import { getFlightById } from "../api/flightApi";
import { bookFlight } from "../api/bookingApi";
import { useParams, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

export default function FlightDetail() {
  const { id } = useParams(); // this is flightId from the list (encoded)
  const [flight, setFlight] = useState(null);
  const { user } = useContext(AuthContext);
  const nav = useNavigate();

  useEffect(() => {
    if (!id) return;
    (async () => {
      try {
        // backend expects flightId in the path
        const res = await getFlightById(decodeURIComponent(id));
        setFlight(res.data);
      } catch (err) {
        console.error("Failed to load flight:", err);
        alert("Failed to load flight details");
      }
    })();
  }, [id]);

  const onBook = async () => {
    if (!user) {
      nav("/login");
      return;
    }
    try {
      await bookFlight(flight.flightId ?? flight.id);
      nav("/my-bookings");
    } catch (err) {
      console.error("Booking failed:", err);
      alert("Booking failed: " + (err.response?.data?.message || err.message));
    }
  };

  if (!flight) return <div style={{ padding: 12 }}>Loading...</div>;

  const route = Array.isArray(flight.route) ? flight.route : [];
  const sourceCity = route?.[0] ?? "N/A";
  const destCity = route?.[route.length - 1] ?? "N/A";

  return (
    <div style={{ padding: 12 }}>
      <h2>
        {flight.airline} {flight.flightId ? <small>({flight.flightId})</small> : null}
      </h2>

      <p>
        <strong>
          {sourceCity} → {destCity}
        </strong>
      </p>

      <p>Status: {flight.status ?? "Unknown"}</p>
      <p>Current Location: {flight.currentLocation ?? "Unknown"}</p>
      <p>
        Seats: {flight.availableSeats ?? "N/A"} / {flight.totalSeats ?? "N/A"}
      </p>
      <p>Price: {flight.price != null ? `₹${flight.price}` : "N/A"}</p>

      <div style={{ marginTop: 12 }}>
        <button onClick={onBook}>Book this flight</button>
      </div>
    </div>
  );
}
