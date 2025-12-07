import React, { useState, useEffect } from "react";
import { Eye, EyeOff } from "lucide-react";
import { signupUser } from "../api";

export default function Signup() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    dob: "",
    email: "",
    password: "",
    confirmPassword: "",
    userType: "student",
    // Student fields
    skills: "",
    education: "",
    major: "",
    gpa: "",
    // Employer fields
    companyName: "",
    companyDescription: "",
    industry: "",
    location: "",
    website: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [msg, setMsg] = useState({ type: "", text: "" });
  const [loading, setLoading] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState("");

  useEffect(() => {
    const password = form.password;
    let strength = 0;
    if (password.length >= 6) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;

    if (strength <= 1) setPasswordStrength("Weak");
    else if (strength === 2 || strength === 3) setPasswordStrength("Medium");
    else if (strength === 4) setPasswordStrength("Strong");
    else setPasswordStrength("");
  }, [form.password]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setMsg({ type: "", text: "" });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!form.firstName || !form.lastName || !form.dob || !form.email || !form.password) {
      return setMsg({ type: "error", text: "Please fill in all required fields." });
    }

    if (form.password.length < 6) {
      return setMsg({ type: "error", text: "Password must be at least 6 characters." });
    }

    if (form.password !== form.confirmPassword) {
      return setMsg({ type: "error", text: "Passwords do not match." });
    }

    // Validate student-specific fields
    if (form.userType === "student") {
      if (!form.skills || !form.education || !form.major) {
        return setMsg({ type: "error", text: "Please fill in all student fields." });
      }
    }

    // Validate employer-specific fields
    if (form.userType === "employer") {
      if (!form.companyName || !form.industry || !form.location) {
        return setMsg({ type: "error", text: "Please fill in all employer fields." });
      }
    }

    setLoading(true);
    try {
      const userData = {
        username: form.firstName + " " + form.lastName,
        age: new Date().getFullYear() - new Date(form.dob).getFullYear(),
        email: form.email,
        password: form.password,
        userType: form.userType,
      };

      // Add student-specific fields
      if (form.userType === "student") {
        userData.skills = form.skills;
        userData.education = form.education;
        userData.major = form.major;
        userData.gpa = form.gpa ? parseFloat(form.gpa) : null;
      }

      // Add employer-specific fields
      if (form.userType === "employer") {
        userData.companyName = form.companyName;
        userData.companyDescription = form.companyDescription;
        userData.industry = form.industry;
        userData.location = form.location;
        userData.website = form.website;
      }

      const res = await signupUser(userData);
      const user = res.user ?? res;
      localStorage.setItem("user", JSON.stringify(user));
      localStorage.setItem("userType", form.userType);

      setMsg({ type: "success", text: "Account created successfully!" });
      setTimeout(() => {
        window.location.href = "/profile";
      }, 1000);
    } catch (err) {
      console.error("Signup error:", err);
      const message = err?.response?.data?.message || err?.message || "Signup failed.";
      setMsg({ type: "error", text: message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen px-4 py-8 text-white">
      <div className="w-full max-w-2xl bg-black/40 p-8 rounded-2xl backdrop-blur-lg border border-white/10 shadow-xl">
        <h1 className="text-3xl mb-6 text-center font-bold tracking-wide">Create Account</h1>

        {msg.text && (
          <p className={`mb-3 text-sm font-semibold text-center ${msg.type === "error" ? "text-red-400" : "text-green-400"}`}>
            {msg.text}
          </p>
        )}

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          {/* User Type Selection */}
          <div className="flex gap-4 mb-2">
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="radio"
                name="userType"
                value="student"
                checked={form.userType === "student"}
                onChange={handleChange}
                className="w-4 h-4"
              />
              <span className="text-white">Student</span>
            </label>
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="radio"
                name="userType"
                value="employer"
                checked={form.userType === "employer"}
                onChange={handleChange}
                className="w-4 h-4"
              />
              <span className="text-white">Employer</span>
            </label>
          </div>

          {/* Common Fields */}
          <div className="grid grid-cols-2 gap-4">
            <input
              type="text"
              name="firstName"
              placeholder="First Name *"
              className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
              value={form.firstName}
              onChange={handleChange}
              required
            />
            <input
              type="text"
              name="lastName"
              placeholder="Last Name *"
              className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
              value={form.lastName}
              onChange={handleChange}
              required
            />
          </div>

          <input
            type="date"
            name="dob"
            placeholder="Date of Birth *"
            className="p-3 rounded-lg bg-white/10 text-white focus:ring-2 focus:ring-white/40"
            value={form.dob}
            onChange={handleChange}
            required
          />

          <input
            type="email"
            name="email"
            placeholder="Email *"
            className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
            value={form.email}
            onChange={handleChange}
            required
          />

          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              placeholder="Password *"
              className="p-3 w-full rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
              value={form.password}
              onChange={handleChange}
              required
            />
            <span
              className="absolute right-3 top-3 cursor-pointer text-purple-400"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </span>
          </div>

          {passwordStrength && (
            <p className={`text-sm ${passwordStrength === "Weak" ? "text-red-400" : passwordStrength === "Medium" ? "text-yellow-400" : "text-green-400"}`}>
              Password Strength: {passwordStrength}
            </p>
          )}

          <div className="relative">
            <input
              type={showConfirm ? "text" : "password"}
              name="confirmPassword"
              placeholder="Confirm Password *"
              className="p-3 w-full rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
              value={form.confirmPassword}
              onChange={handleChange}
              required
            />
            <span
              className="absolute right-3 top-3 cursor-pointer text-purple-400"
              onClick={() => setShowConfirm(!showConfirm)}
            >
              {showConfirm ? <EyeOff size={20} /> : <Eye size={20} />}
            </span>
          </div>

          {/* Student-Specific Fields */}
          {form.userType === "student" && (
            <>
              <div className="border-t border-white/20 pt-4 mt-2">
                <h3 className="text-lg font-semibold mb-3 text-purple-300">Student Information</h3>
              </div>

              <input
                type="text"
                name="skills"
                placeholder="Skills (e.g., JavaScript, Python, React) *"
                className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                value={form.skills}
                onChange={handleChange}
              />

              <input
                type="text"
                name="education"
                placeholder="Education (e.g., BS Computer Science) *"
                className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                value={form.education}
                onChange={handleChange}
              />

              <div className="grid grid-cols-2 gap-4">
                <input
                  type="text"
                  name="major"
                  placeholder="Major *"
                  className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                  value={form.major}
                  onChange={handleChange}
                />
                <input
                  type="number"
                  step="0.01"
                  min="0"
                  max="4"
                  name="gpa"
                  placeholder="GPA (optional)"
                  className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                  value={form.gpa}
                  onChange={handleChange}
                />
              </div>
            </>
          )}

          {/* Employer-Specific Fields */}
          {form.userType === "employer" && (
            <>
              <div className="border-t border-white/20 pt-4 mt-2">
                <h3 className="text-lg font-semibold mb-3 text-purple-300">Company Information</h3>
              </div>

              <input
                type="text"
                name="companyName"
                placeholder="Company Name *"
                className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                value={form.companyName}
                onChange={handleChange}
              />

              <textarea
                name="companyDescription"
                placeholder="Company Description (optional)"
                className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40 min-h-[80px]"
                value={form.companyDescription}
                onChange={handleChange}
              />

              <div className="grid grid-cols-2 gap-4">
                <input
                  type="text"
                  name="industry"
                  placeholder="Industry *"
                  className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                  value={form.industry}
                  onChange={handleChange}
                />
                <input
                  type="text"
                  name="location"
                  placeholder="Location *"
                  className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                  value={form.location}
                  onChange={handleChange}
                />
              </div>

              <input
                type="url"
                name="website"
                placeholder="Website (optional)"
                className="p-3 rounded-lg bg-white/10 placeholder-white/70 focus:ring-2 focus:ring-white/40"
                value={form.website}
                onChange={handleChange}
              />
            </>
          )}

          <button
            type="submit"
            className="bg-white text-black font-bold py-3 rounded-lg hover:bg-white/90 transition mt-4"
            disabled={loading}
          >
            {loading ? "Creating Account..." : "Sign Up"}
          </button>
        </form>

        <p className="text-center text-sm text-gray-300 mt-4">
          Already have an account?{" "}
          <a href="/login" className="text-purple-400 hover:text-purple-300 font-semibold">
            Login
          </a>
        </p>
      </div>
    </div>
  );
}
