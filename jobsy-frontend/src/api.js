import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080",
});

// -------------------- USERS --------------------
export const signupUser = async (userData) => {
  const response = await API.post("/api/users/signup", userData);
  return response.data;
};

export const loginUser = async (email, password) => {
  const response = await API.post("/api/users/login", { email, password });
  return response.data;
};

// -------------------- JOBS --------------------
export const getJobs = async () => {
  const response = await API.get("/jobs");
  return response.data;
};

export const getJobById = async (id) => {
  const response = await API.get(`/jobs/${id}`);
  return response.data;
};

export const createJob = async (jobData) => {
  const response = await API.post("/jobs", jobData);
  return response.data;
};

export const updateJob = async (id, jobData) => {
  const response = await API.put(`/jobs/${id}`, jobData);
  return response.data;
};

export const deleteJob = async (id) => {
  const response = await API.delete(`/jobs/${id}`);
  return response.data;
};

export const searchJobs = async (keyword) => {
  const response = await API.get(`/jobs/search?keyword=${keyword}`);
  return response.data;
};

// -------------------- APPLICATIONS --------------------
export const applyJob = async (applicationData) => {
  const response = await API.post("/applications", applicationData);
  return response.data;
};

export const getApplicationsByStudent = async (studentId) => {
  const response = await API.get(`/applications/student/${studentId}`);
  return response.data;
};

export const getApplicationsByJob = async (jobId) => {
  const response = await API.get(`/applications/job/${jobId}`);
  return response.data;
};

export const getAllApplications = async () => {
  const response = await API.get("/applications");
  return response.data;
};

export const acceptApplication = async (id) => {
  const response = await API.put(`/applications/${id}/accept`);
  return response.data;
};

export const rejectApplication = async (id) => {
  const response = await API.put(`/applications/${id}/reject`);
  return response.data;
};

// -------------------- USERS --------------------
export const getProfile = async (userId) => {
  const response = await API.get(`/api/users/${userId}`);
  return response.data;
};

export default API;
