import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getProfile } from "../api";
import { Briefcase, GraduationCap, Building, MapPin, Globe, Mail, User } from "lucide-react";

export default function Profile() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    // use `id` from hook above
    let mounted = true;
    const load = async () => {
      setLoading(true);
      try {
        if (id) {
          // Viewing someone else's profile by id
          const data = await getProfile(id);
          if (!mounted) return;
          const profile = data.user ?? data;
          setUser(profile);
        } else {
          // Viewing own profile
          const stored = localStorage.getItem("user");
          if (!stored) {
            window.location.href = "/login";
            return;
          }
          const parsed = JSON.parse(stored);
          const userId = parsed.id ?? parsed.userId ?? null;
          if (!userId) {
            setUser(parsed);
            return;
          }
          const data = await getProfile(userId);
          if (!mounted) return;
          const profile = data.user ?? data;
          setUser(profile);
          // Update localStorage with fresh data
          localStorage.setItem("user", JSON.stringify(profile));
        }
      } catch (err) {
        console.error("Get profile error:", err);
        setError("Failed to load profile.");
      } finally {
        if (mounted) setLoading(false);
      }
    };

    load();
    return () => (mounted = false);
  }, [id]);

  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("userType");
    window.location.href = "/login";
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-white text-2xl">Loading profile...</div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-red-400 text-2xl">Please login to view profile</div>
      </div>
    );
  }

  const isEmployer = user.companyName || user.userType === "EMPLOYER" || user.userType === "employer";
  const isStudent = user.skills || user.userType === "STUDENT" || user.userType === "student";

  return (
    <motion.div
      className="min-h-screen px-4 py-8 md:px-20"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.5 }}
    >
      <div className="max-w-4xl mx-auto">
        {/* Header */}
        <div className="bg-gradient-to-r from-purple-600 to-pink-600 rounded-2xl p-8 mb-6 text-white">
          <div className="flex justify-between items-start">
            <div>
              <h1 className="text-4xl font-bold mb-2">{user.username || "User"}</h1>
              <div className="flex items-center gap-2 text-purple-100">
                <Mail size={18} />
                <span>{user.email}</span>
              </div>
              <div className="mt-2 px-3 py-1 bg-white/20 rounded-full inline-block text-sm">
                {isEmployer ? "Employer" : "Student"}
              </div>
            </div>
            {!id && (
              <button
                onClick={handleLogout}
                className="bg-white/20 hover:bg-white/30 px-4 py-2 rounded-lg transition"
              >
                Logout
              </button>
            )}
          </div>
        </div>

        {/* Employer Profile */}
        {isEmployer && (
          <div className="space-y-6">
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
              <h2 className="text-2xl font-bold text-white mb-6">Company Information</h2>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {user.companyName && (
                  <div className="flex items-start gap-3">
                    <Building className="text-purple-400 mt-1" size={20} />
                    <div>
                      <p className="text-white/60 text-sm">Company Name</p>
                      <p className="text-white text-lg">{user.companyName}</p>
                    </div>
                  </div>
                )}

                {user.industry && (
                  <div className="flex items-start gap-3">
                    <Briefcase className="text-purple-400 mt-1" size={20} />
                    <div>
                      <p className="text-white/60 text-sm">Industry</p>
                      <p className="text-white text-lg">{user.industry}</p>
                    </div>
                  </div>
                )}

                {user.location && (
                  <div className="flex items-start gap-3">
                    <MapPin className="text-purple-400 mt-1" size={20} />
                    <div>
                      <p className="text-white/60 text-sm">Location</p>
                      <p className="text-white text-lg">{user.location}</p>
                    </div>
                  </div>
                )}

                {user.website && (
                  <div className="flex items-start gap-3">
                    <Globe className="text-purple-400 mt-1" size={20} />
                    <div>
                      <p className="text-white/60 text-sm">Website</p>
                      <a href={user.website} target="_blank" rel="noopener noreferrer" className="text-purple-300 hover:text-purple-200">
                        {user.website}
                      </a>
                    </div>
                  </div>
                )}
              </div>

              {user.companyDescription && (
                <div className="mt-6">
                  <p className="text-white/60 text-sm mb-2">About Company</p>
                  <p className="text-white">{user.companyDescription}</p>
                </div>
              )}
            </div>

            {/* Post Job Button */}
            <button
              onClick={() => navigate("/post-job")}
              className="w-full bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold py-4 rounded-xl hover:from-purple-700 hover:to-pink-700 transition text-lg"
            >
              + Post a New Job
            </button>
          </div>
        )}

        {/* Student Profile */}
        {isStudent && (
          <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
            <h2 className="text-2xl font-bold text-white mb-6">Student Information</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {user.age && (
                <div className="flex items-start gap-3">
                  <User className="text-purple-400 mt-1" size={20} />
                  <div>
                    <p className="text-white/60 text-sm">Age</p>
                    <p className="text-white text-lg">{user.age} years</p>
                  </div>
                </div>
              )}

              {user.education && (
                <div className="flex items-start gap-3">
                  <GraduationCap className="text-purple-400 mt-1" size={20} />
                  <div>
                    <p className="text-white/60 text-sm">Education</p>
                    <p className="text-white text-lg">{user.education}</p>
                  </div>
                </div>
              )}

              {user.major && (
                <div className="flex items-start gap-3">
                  <Briefcase className="text-purple-400 mt-1" size={20} />
                  <div>
                    <p className="text-white/60 text-sm">Major</p>
                    <p className="text-white text-lg">{user.major}</p>
                  </div>
                </div>
              )}

              {user.gpa && (
                <div className="flex items-start gap-3">
                  <GraduationCap className="text-purple-400 mt-1" size={20} />
                  <div>
                    <p className="text-white/60 text-sm">GPA</p>
                    <p className="text-white text-lg">{user.gpa.toFixed(2)}</p>
                  </div>
                </div>
              )}
            </div>

            {user.skills && (
              <div className="mt-6">
                <p className="text-white/60 text-sm mb-3">Skills</p>
                <div className="flex flex-wrap gap-2">
                  {user.skills.split(',').map((skill, idx) => (
                    <span key={idx} className="bg-purple-600/30 text-purple-200 px-3 py-1 rounded-full text-sm">
                      {skill.trim()}
                    </span>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}

        {error && (
          <div className="mt-6 p-4 bg-red-500/20 text-red-300 rounded-lg">
            {error}
          </div>
        )}
      </div>
    </motion.div>
  );
}
