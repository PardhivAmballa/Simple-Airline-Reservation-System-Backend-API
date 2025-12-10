// src/pages/MyBookings.jsx
import React, { useEffect, useState } from "react";
import { getMyBookings, requestCancel } from "../api/bookingApi";
import { Link } from "react-router-dom";

export default function MyBookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadBookings = async () => {
    setLoading(true);
    try {
      const res = await getMyBookings();
      // backend returns: [{ bookingId, flightId, username, status }]
      setBookings(res.data || []);
    } catch (err) {
      console.error("Failed to load bookings:", err);
      alert("Failed to load bookings");
    }
    setLoading(false);
  };

  useEffect(() => {
    loadBookings();
  }, []);

  const onRequestCancel = async (bookingId) => {
    if (!confirm("Request cancellation for this booking?")) return;

    try {
      await requestCancel(bookingId);
      alert("Cancellation request sent.");
      loadBookings();
    } catch (err) {
      console.error("Request cancel failed:", err);
      alert("Request cancel failed");
    }
  };

  return (
    <div style={{ padding: 12 }}>
      <h2>My Bookings</h2>

      {loading ? (
        <p>Loading...</p>
      ) : bookings.length === 0 ? (
        <p>No bookings found.</p>
      ) : (
        bookings.map((b) => (
          <div key={b.bookingId} style={{ border: "1px solid #ddd", padding: 10, marginBottom: 12 }}>
            <p><strong>Booking ID:</strong> {b.bookingId}</p>
            <p>
              <strong>Flight:</strong>{" "}
              <Link to={`/flights/${encodeURIComponent(b.flightId)}`}>{b.flightId}</Link>
            </p>
            <p><strong>Status:</strong> {b.status}</p>

            <button onClick={() => onRequestCancel(b.bookingId)}>
              Request Cancellation
            </button>
          </div>
        ))
      )}
    </div>
  );
}
