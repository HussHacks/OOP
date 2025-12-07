import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createJob } from "../api";
import { motion } from "framer-motion";
import { ArrowLeft } from "lucide-react";

export default function PostJob() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        title: "",
        description: "",
        salary: "",
        companyName: "",
        jobType: "Full-time",
        isOpen: true
    });
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState({ type: "", text: "" });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setForm({ ...form, [name]: type === "checkbox" ? checked : value });
        setMessage({ type: "", text: "" });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const user = JSON.parse(localStorage.getItem("user") || "{}");
        if (!user.id) {
            setMessage({ type: "error", text: "Please login to post a job" });
            setTimeout(() => navigate("/login"), 2000);
            return;
        }

        // Check if user is an employer (has companyName field)
        const isEmployer = user.companyName || user.userType === "employer" || user.userType === "EMPLOYER";
        if (!isEmployer) {
            setMessage({ type: "error", text: "Only employers can post jobs" });
            return;
        }

        if (!form.title || !form.description || !form.companyName) {
            setMessage({ type: "error", text: "Please fill in all required fields" });
            return;
        }

        setLoading(true);
        try {
            const jobData = {
                ...form,
                employer: { id: user.id }
            };

            await createJob(jobData);
            setMessage({ type: "success", text: "Job posted successfully!" });
            setTimeout(() => navigate("/jobs"), 2000);
        } catch (err) {
            console.error("Post job error:", err);
            const errorMsg = err?.response?.data?.message || err?.message || "Failed to post job";
            setMessage({ type: "error", text: errorMsg });
        } finally {
            setLoading(false);
        }
    };

    return (
        <motion.div
            className="min-h-screen px-4 py-8 md:px-20"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.5 }}
        >
            <div className="max-w-3xl mx-auto">
                <button
                    onClick={() => navigate("/jobs")}
                    className="flex items-center gap-2 text-white/80 hover:text-white mb-6 transition"
                >
                    <ArrowLeft size={20} />
                    <span>Back to Jobs</span>
                </button>

                <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
                    <h1 className="text-4xl font-bold text-white mb-6">Post a New Job</h1>

                    {message.text && (
                        <div className={`p-4 rounded-lg mb-6 ${message.type === "success" ? "bg-green-500/20 text-green-300" : "bg-red-500/20 text-red-300"}`}>
                            {message.text}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                        <div>
                            <label className="block text-white mb-2 font-semibold">Job Title *</label>
                            <input
                                type="text"
                                name="title"
                                placeholder="e.g., Senior Software Engineer"
                                className="w-full p-3 rounded-lg bg-white/10 text-white placeholder-white/50 focus:ring-2 focus:ring-purple-400 border border-white/20"
                                value={form.title}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div>
                            <label className="block text-white mb-2 font-semibold">Company Name *</label>
                            <input
                                type="text"
                                name="companyName"
                                placeholder="e.g., Tech Corp"
                                className="w-full p-3 rounded-lg bg-white/10 text-white placeholder-white/50 focus:ring-2 focus:ring-purple-400 border border-white/20"
                                value={form.companyName}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div>
                            <label className="block text-white mb-2 font-semibold">Job Description *</label>
                            <textarea
                                name="description"
                                placeholder="Describe the role, responsibilities, and requirements..."
                                className="w-full p-3 rounded-lg bg-white/10 text-white placeholder-white/50 focus:ring-2 focus:ring-purple-400 border border-white/20 min-h-[150px]"
                                value={form.description}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-white mb-2 font-semibold">Salary Range</label>
                                <input
                                    type="text"
                                    name="salary"
                                    placeholder="e.g., $80,000 - $120,000"
                                    className="w-full p-3 rounded-lg bg-white/10 text-white placeholder-white/50 focus:ring-2 focus:ring-purple-400 border border-white/20"
                                    value={form.salary}
                                    onChange={handleChange}
                                />
                            </div>

                            <div>
                                <label className="block text-white mb-2 font-semibold">Job Type</label>
                                <select
                                    name="jobType"
                                    className="w-full p-3 rounded-lg bg-white/10 text-white focus:ring-2 focus:ring-purple-400 border border-white/20"
                                    value={form.jobType}
                                    onChange={handleChange}
                                >
                                    <option value="Full-time">Full-time</option>
                                    <option value="Part-time">Part-time</option>
                                    <option value="Contract">Contract</option>
                                    <option value="Internship">Internship</option>
                                </select>
                            </div>
                        </div>

                        <div className="flex items-center gap-2">
                            <input
                                type="checkbox"
                                name="isOpen"
                                id="isOpen"
                                className="w-5 h-5"
                                checked={form.isOpen}
                                onChange={handleChange}
                            />
                            <label htmlFor="isOpen" className="text-white">
                                Job is currently open for applications
                            </label>
                        </div>

                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold py-4 rounded-xl hover:from-purple-700 hover:to-pink-700 transition disabled:opacity-50 disabled:cursor-not-allowed text-lg mt-4"
                        >
                            {loading ? "Posting Job..." : "Post Job"}
                        </button>
                    </form>
                </div>
            </div>
        </motion.div>
    );
}
