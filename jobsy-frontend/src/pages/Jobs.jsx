import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { useInView } from "react-intersection-observer";
import { getJobs } from "../api";

export default function Jobs() {
  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let mounted = true;
    setLoading(true);
    getJobs()
      .then((data) => {
        if (!mounted) return;
        const jobArray = Array.isArray(data) ? data : data?.jobs ?? [];
        setJobs(jobArray);
      })
      .catch((err) => {
        console.error("Error fetching jobs:", err);
        setError("Failed to load jobs.");
        setJobs([]);
      })
      .finally(() => mounted && setLoading(false));
    return () => (mounted = false);
  }, []);

  const categories = [
    "All",
    ...(Array.isArray(jobs) ? [...new Set(jobs.map((j) => j.jobType || "Other"))] : []),
  ];

  const filteredJobs =
    selectedCategory === "All" ? jobs : jobs.filter((j) => (j.jobType || "Other") === selectedCategory);

  if (loading) return <div className="text-center text-white text-2xl py-20">Loading jobs...</div>;
  if (error) return <div className="text-center text-red-400 text-xl py-20">{error}</div>;
  if (!jobs.length) return <div className="text-center text-white text-2xl py-20">No jobs available at the moment.</div>;

  return (
    <div className="relative w-full min-h-screen py-20 px-4 md:px-20">
      <h1 className="text-4xl md:text-5xl font-extrabold text-center text-white mb-8">Available Jobs</h1>

      <div className="flex justify-center gap-4 mb-12 flex-wrap">
        {categories.map((cat, idx) => (
          <button
            key={idx}
            onClick={() => setSelectedCategory(cat)}
            className={`px-6 py-2 rounded-full font-semibold transition ${selectedCategory === cat
                ? "bg-purple-600 text-white"
                : "bg-gray-800 text-gray-200 hover:bg-purple-500 hover:text-white"
              }`}
          >
            {cat}
          </button>
        ))}
      </div>

      <div className="flex flex-col gap-8">
        {filteredJobs.length > 0 ? (
          filteredJobs.map((job) => (
            <JobCard
              key={job.id ?? job.jobId}
              job={job}
              onViewDetails={() => navigate(`/jobs/${job.id}`)}
            />
          ))
        ) : (
          <p className="text-center text-gray-300 text-xl">No jobs in this category.</p>
        )}
      </div>
    </div>
  );
}

const JobCard = ({ job, onViewDetails }) => {
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <motion.div
      ref={ref}
      initial={{ opacity: 0, y: 50 }}
      animate={inView ? { opacity: 1, y: 0 } : {}}
      transition={{ duration: 0.3 }}
      whileHover={{ y: -5, scale: 1.02 }}
      className="rounded-3xl shadow-lg p-8 md:p-12 bg-purple-700/90 text-white flex flex-col gap-4"
    >
      <div className="flex justify-between items-start">
        <div>
          <h2 className="text-2xl font-bold">{job.title || "Untitled Job"}</h2>
          <p className="text-purple-200 text-lg">{job.companyName || "Unknown Company"}</p>
        </div>
        <div className={`px-3 py-1 rounded-full text-sm ${job.isOpen ? "bg-green-500/30 text-green-200" : "bg-red-500/30 text-red-200"}`}>
          {job.isOpen ? "Open" : "Closed"}
        </div>
      </div>

      <p className="text-gray-100 line-clamp-2">{job.description || "No description available."}</p>

      <div className="flex gap-4 flex-wrap text-sm">
        {job.salary && <span className="bg-white/10 px-3 py-1 rounded-full">💰 {job.salary}</span>}
        {job.jobType && <span className="bg-white/10 px-3 py-1 rounded-full">📋 {job.jobType}</span>}
      </div>

      <div className="flex gap-4 mt-4">
        <motion.button
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          onClick={onViewDetails}
          className="flex-1 bg-white text-purple-700 font-bold py-3 rounded-lg hover:bg-gray-100 transition"
        >
          View Job
        </motion.button>
      </div>
    </motion.div>
  );
};
