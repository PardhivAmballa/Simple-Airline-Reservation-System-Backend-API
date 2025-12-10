// src/api/flightApi.js
import api from "./api";

export const getAllFlights = () => api.get("/api/flights");
export const searchFlights = (source, destination) =>
  api.get(`/api/flights/search?source=${encodeURIComponent(source)}&destination=${encodeURIComponent(destination)}`);
export const getFlightById = (id) => api.get(`/api/flights/${id}`);
export const addFlight = (flightObj) => api.post("/api/flights", flightObj);
export const updateFlightStatus = (flightId, payload) => api.put(`/api/flights/${flightId}/status`, payload);
export const deleteFlight = (flightId) => api.delete(`/api/flights/${flightId}`);
