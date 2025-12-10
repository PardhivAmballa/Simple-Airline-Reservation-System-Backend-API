// src/api/bookingApi.js
import api from "./api";

export const bookFlight = (flightId) => api.post(`/api/bookings/${flightId}`);
export const getMyBookings = () => api.get("/api/bookings/my-bookings");
export const getAllBookingsAdmin = () => api.get("/api/bookings/all");
export const deleteBookingAdmin = (bookingId) => api.delete(`/api/bookings/${bookingId}`);
export const requestCancel = (bookingId) => api.post(`/api/bookings/cancel-request/${bookingId}`);
export const getCancelRequestsAdmin = () => api.get("/api/bookings/admin/cancel-requests");
export const cancelAllRequestsAdmin = () => api.post("/api/bookings/admin/cancel-all-requests");
export const cancelOneRequestedAdmin = (bookingId) => api.post(`/api/bookings/admin/cancel-request/${bookingId}`);
