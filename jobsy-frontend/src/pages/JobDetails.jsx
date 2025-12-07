import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getJobById, applyJob } from "../api";
import { motion } from "framer-motion";
import { ArrowLeft, Building, DollarSign, Calendar, MapPin, Briefcase } from "lucide-react";

export default function JobDetails() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [job, setJob] = useState(null);
    const [loading, setLoading] = useState(true);
    const [applying, setApplying] = useState(false);
    const [message, setMessage] = useState({ type: "", text: "" });

    useEffect(() => {
        loadJob();
    }, [id]);

    const loadJob = async () => {
        try {
            const data = await getJobById(id);
            setJob(data);
        } catch (err) {
            console.error("Error loading job:", err);
            setMessage({ type: "error", text: "Failed to load job details" });
        } finally {
            setLoading(false);
        }
    };

    const handleApply = async () => {
        const user = JSON.parse(localStorage.getItem("user") || "{}");
        if (!user.id) {
            setMessage({ type: "error", text: "Please login to apply" });
            setTimeout(() => navigate("/login"), 2000);
            return;
        }

        const userType = user.userType || localStorage.getItem("userType");
        if (userType !== "student" && userType !== "STUDENT") {
            setMessage({ type: "error", text: "Only students can apply to jobs" });
            return;
        }

        setApplying(true);
        try {
            await applyJob({
                student: { id: user.id },
                job: { id: job.id },
                status: "PENDING"
            });
            setMessage({ type: "success", text: "Application submitted successfully!" });
        } catch (err) {
            console.error("Apply error:", err);
            const errorMsg = err?.response?.data?.message || err?.message || "Failed to apply";
            setMessage({ type: "error", text: errorMsg });
        } finally {
            setApplying(false);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="text-white text-2xl">Loading job details...</div>
            </div>
        );
    }

    if (!job) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="text-red-400 text-2xl">Job not found</div>
            </div>
        );
    }

    return (
        <motion.div
            className="min-h-screen px-4 py-8 md:px-20"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.5 }}
        >
            <div className="max-w-4xl mx-auto">
                {/* Back Button */}
                <button
                    onClick={() => navigate("/jobs")}
                    className="flex items-center gap-2 text-white/80 hover:text-white mb-6 transition"
                >
                    <ArrowLeft size={20} />
                    <span>Back to Jobs</span>
                </button>

                {/* Job Header */}
                <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 mb-6 border border-white/20">
                    <div className="flex items-start justify-between mb-4">
                        <div>
                            <h1 className="text-4xl font-bold text-white mb-2">{job.title}</h1>
                            <div className="flex items-center gap-2 text-purple-300 text-lg">
                                <Building size={20} />
                                <span>{job.companyName || "Company"}</span>
                            </div>
                        </div>
                        <div className={`px-4 py-2 rounded-full ${job.isOpen ? "bg-green-500/20 text-green-300" : "bg-red-500/20 text-red-300"}`}>
                            {job.isOpen ? "Open" : "Closed"}
                        </div>
                    </div>

                    {/* Job Meta Info */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">
                        {job.salary && (
                            <div className="flex items-center gap-2 text-white/80">
                                <DollarSign size={18} className="text-green-400" />
                                <span>{job.salary}</span>
                            </div>
                        )}
                        {job.jobType && (
                            <div className="flex items-center gap-2 text-white/80">
                                <Briefcase size={18} className="text-blue-400" />
                                <span>{job.jobType}</span>
                            </div>
                        )}
                        {job.datePosted && (
                            <div className="flex items-center gap-2 text-white/80">
                                <Calendar size={18} className="text-yellow-400" />
                                <span>Posted {new Date(job.datePosted).toLocaleDateString()}</span>
                            </div>
                        )}
                    </div>
                </div>

                {/* Job Description */}
                <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 mb-6 border border-white/20">
                    <h2 className="text-2xl font-bold text-white mb-4">Job Description</h2>
                    <p className="text-white/90 text-lg leading-relaxed whitespace-pre-wrap">
                        {job.description || "No description available."}
                    </p>
                </div>

                {/* Message */}
                {message.text && (
                    <div className={`p-4 rounded-lg mb-6 ${message.type === "success" ? "bg-green-500/20 text-green-300" : "bg-red-500/20 text-red-300"}`}>
                        {message.text}
                    </div>
                )}

                {/* Apply Button */}
                {job.isOpen && (
                    <button
                        onClick={handleApply}
                        disabled={applying}
                        className="w-full bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold py-4 rounded-xl hover:from-purple-700 hover:to-pink-700 transition disabled:opacity-50 disabled:cursor-not-allowed text-lg"
                    >
                        {applying ? "Submitting Application..." : "Apply Now"}
                    </button>
                )}
            </div>
        </motion.div>
    );
}
