import React, { useState } from "react";
import { Eye, EyeOff } from "lucide-react";
import { loginUser } from "../api";

export default function Login() {
  const [form, setForm] = useState({ email: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [msg, setMsg] = useState({ type: "", text: "" });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setMsg({ type: "", text: "" });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!form.email || !form.password) {
      return setMsg({ type: "error", text: "Please fill in all fields." });
    }

    setLoading(true);
    try {
      const res = await loginUser(form.email, form.password);
      const user = res.user ?? res;
      localStorage.setItem("user", JSON.stringify(user));
      localStorage.setItem("userType", user.userType || "student");

      setMsg({ type: "success", text: "Login successful!" });
      window.location.href = "/profile";
    } catch (err) {
      console.error("Login error:", err);

      // Handle 401 Unauthorized specifically
      if (err.response && err.response.status === 401) {
        setMsg({ type: "error", text: "Invalid credentials. Please check your email or password." });
      } else {
        const message = err?.response?.data?.message || err?.message || "Login failed. Please try again.";
        setMsg({ type: "error", text: message });
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-5rem)] px-4 text-white">
      <div className="w-full max-w-md bg-black/40 p-8 rounded-2xl backdrop-blur-lg border border-white/10 shadow-xl">
        <h1 className="text-3xl mb-6 text-center font-bold tracking-wide">Login</h1>

        {msg.text && <p className={`mb-3 text-sm font-semibold text-center ${msg.type === "error" ? "text-red-400" : "text-green-400"}`}>{msg.text}</p>}

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input type="email" name="email" placeholder="Email" className="p-3 rounded-lg bg-white/10 placeholder-white focus:ring-2 focus:ring-white/40" value={form.email} onChange={handleChange} required />

          <div className="relative">
            <input type={showPassword ? "text" : "password"} name="password" placeholder="Password" className="p-3 w-full rounded-lg bg-white/10 placeholder-white focus:ring-2 focus:ring-white/40" value={form.password} onChange={handleChange} required />
            <span className="absolute right-3 top-3 cursor-pointer text-purple-500" onClick={() => setShowPassword(!showPassword)}>
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </span>
          </div>

          <button type="submit" className="bg-white text-black font-bold py-3 rounded-lg hover:bg-white/80 transition" disabled={loading}>
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        <p className="text-center text-sm text-gray-300 mt-4">
          Don't have an account?{" "}
          <a href="/signup" className="text-purple-400 hover:text-purple-300 font-semibold">
            Sign up
          </a>
        </p>
      </div>
    </div>
  );
}
