// src/pages/AdminDashboard.jsx
import React, { useEffect, useState } from "react";
import {
  getAllBookingsAdmin,
  deleteBookingAdmin,
  getCancelRequestsAdmin,
  cancelAllRequestsAdmin,
  cancelOneRequestedAdmin
} from "../api/bookingApi";

import {
  getAllFlights,
  addFlight,
  updateFlightStatus,
  deleteFlight
} from "../api/flightApi";

export default function AdminDashboard() {
  const [bookings, setBookings] = useState([]);
  const [cancelRequests, setCancelRequests] = useState([]);
  const [flights, setFlights] = useState([]);
  const [newFlight, setNewFlight] = useState({
    flightId: "",
    airline: "",
    routeText: "",
    totalSeats: 100,
    availableSeats: 100,
    price: 0,
    status: "Scheduled",
    currentLocation: ""
  });

  const loadAll = async () => {
    try {
      const [flRes, bkRes, crRes] = await Promise.all([
        getAllFlights(),
        getAllBookingsAdmin(),
        getCancelRequestsAdmin()
      ]);

      setFlights(flRes.data || []);
      setBookings(bkRes.data || []);
      setCancelRequests(crRes.data || []);
    } catch (err) {
      console.error("Admin load failed:", err);
      alert("Failed to load admin data");
    }
  };

  useEffect(() => {
    loadAll();
  }, []);

  // add flight
  const onAddFlight = async () => {
    const route = newFlight.routeText.split(",").map((s) => s.trim()).filter(Boolean);
    const payload = {
      flightId: newFlight.flightId,
      airline: newFlight.airline,
      route,
      totalSeats: Number(newFlight.totalSeats),
      availableSeats: Number(newFlight.availableSeats),
      price: Number(newFlight.price),
      status: newFlight.status,
      currentLocation: newFlight.currentLocation || route[0] || ""
    };

    try {
      await addFlight(payload);
      alert("Flight added");
      setNewFlight({
        flightId: "",
        airline: "",
        routeText: "",
        totalSeats: 100,
        availableSeats: 100,
        price: 0,
        status: "Scheduled",
        currentLocation: ""
      });
      loadAll();
    } catch (err) {
      console.error("Add flight failed:", err);
      alert("Add flight failed");
    }
  };

  const onDeleteFlight = async (flightId) => {
    if (!confirm("Delete this flight?")) return;
    try {
      await deleteFlight(flightId);
      alert("Deleted");
      loadAll();
    } catch (err) {
      console.error("Delete flight failed:", err);
      alert("Delete failed");
    }
  };

  // delete booking
  const onDeleteBooking = async (bookingId) => {
    if (!confirm("Cancel this booking?")) return;
    try {
      await deleteBookingAdmin(bookingId);
      alert("Booking cancelled");
      loadAll();
    } catch (err) {
      console.error("Booking cancel failed:", err);
      alert("Booking cancel failed");
    }
  };

  // admin cancel all requests
  const onCancelAll = async () => {
    if (!confirm("Cancel ALL requested?")) return;
    try {
      await cancelAllRequestsAdmin();
      alert("All requests cancelled");
      loadAll();
    } catch (err) {
      console.error("Cancel all failed:", err);
      alert("Cancel all failed");
    }
  };

  // admin cancel one request
  const onCancelOne = async (bookingId) => {
    if (!confirm("Cancel this request?")) return;
    try {
      await cancelOneRequestedAdmin(bookingId);
      alert("Cancelled");
      loadAll();
    } catch (err) {
      console.error("Cancel one failed:", err);
      alert("Cancel one failed");
    }
  };

  return (
    <div style={{ padding: 12 }}>
      <h2>Admin Dashboard</h2>

      {/* Add Flight */}
      <h3>Add Flight</h3>
      <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
        <input
          placeholder="flightId"
          value={newFlight.flightId}
          onChange={(e) => setNewFlight({ ...newFlight, flightId: e.target.value })}
        />
        <input
          placeholder="airline"
          value={newFlight.airline}
          onChange={(e) => setNewFlight({ ...newFlight, airline: e.target.value })}
        />
        <input
          placeholder="route (e.g. BLR,HYD,BOM)"
          value={newFlight.routeText}
          onChange={(e) => setNewFlight({ ...newFlight, routeText: e.target.value })}
          style={{ minWidth: 250 }}
        />
        <input
          type="number"
          placeholder="totalSeats"
          value={newFlight.totalSeats}
          onChange={(e) => setNewFlight({ ...newFlight, totalSeats: e.target.value })}
        />
        <input
          type="number"
          placeholder="availableSeats"
          value={newFlight.availableSeats}
          onChange={(e) => setNewFlight({ ...newFlight, availableSeats: e.target.value })}
        />
        <input
          type="number"
          placeholder="price"
          value={newFlight.price}
          onChange={(e) => setNewFlight({ ...newFlight, price: e.target.value })}
        />
        <select
          value={newFlight.status}
          onChange={(e) => setNewFlight({ ...newFlight, status: e.target.value })}
        >
          <option>Scheduled</option>
          <option>In Air</option>
          <option>Cancelled</option>
          <option>Delayed</option>
        </select>
        <input
          placeholder="currentLocation"
          value={newFlight.currentLocation}
          onChange={(e) => setNewFlight({ ...newFlight, currentLocation: e.target.value })}
        />
        <button onClick={onAddFlight}>Add Flight</button>
      </div>

      <hr />

      {/* Flights */}
      <h3>Flights</h3>
      {flights.length === 0 ? (
        <p>No flights</p>
      ) : (
        flights.map((f) => (
          <div
            key={f.flightId}
            style={{ border: "1px solid #ccc", padding: 10, marginBottom: 10 }}
          >
            <p><strong>{f.airline}</strong> ({f.flightId})</p>
            <p>
              Route:{" "}
              {Array.isArray(f.route)
                ? `${f.route[0]} → ${f.route[f.route.length - 1]}`
                : "Unknown route"}
            </p>
            <p>Status: {f.status}</p>

            <button onClick={() => onDeleteFlight(f.flightId)}>Delete</button>
          </div>
        ))
      )}

      <hr />

      {/* Bookings */}
      <h3>All Bookings</h3>
      {bookings.length === 0 ? (
        <p>No bookings</p>
      ) : (
        bookings.map((b) => (
          <div
            key={b.bookingId}
            style={{ border: "1px solid #ccc", padding: 10, marginBottom: 10 }}
          >
            <p><strong>Booking:</strong> {b.bookingId}</p>
            <p><strong>Flight:</strong> {b.flightId}</p>
            <p><strong>User:</strong> {b.username}</p>
            <p><strong>Status:</strong> {b.status}</p>

            <button onClick={() => onDeleteBooking(b.bookingId)}>Cancel Booking</button>
          </div>
        ))
      )}

      <hr />

      {/* Cancel Requests */}
      <h3>Cancel Requests</h3>
      <button onClick={onCancelAll} style={{ marginBottom: 8 }}>
        Cancel All Requests
      </button>

      {cancelRequests.length === 0 ? (
        <p>No cancel requests</p>
      ) : (
        cancelRequests.map((r) => (
          <div
            key={r.bookingId}
            style={{ border: "1px solid #ccc", padding: 10, marginBottom: 10 }}
          >
            <p><strong>Booking ID:</strong> {r.bookingId}</p>

            <button onClick={() => onCancelOne(r.bookingId)}>Cancel This Booking</button>
          </div>
        ))
      )}
    </div>
  );
}
