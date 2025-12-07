import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const Landing = () => {
  const navigate = useNavigate();
  const [fade, setFade] = useState(false);

  useEffect(() => {
    setFade(true);
  }, []);

  return (
    <div className="relative w-full h-screen overflow-hidden">
      {/* Silk background */}
      <Silk
        speed={5}
        scale={1}
        color="#7B7481"
        noiseIntensity={1.5}
        rotation={0}
      />

     

      {/* Navbar */}
      <Navbar />

      {/* Overlay content */}
      <main className="absolute inset-0 flex flex-col items-center justify-center text-center px-4 z-10">
        <h1
          className={`text-5xl md:text-6xl font-extrabold text-white mb-6 transition-opacity duration-1000 ${
            fade ? 'opacity-100' : 'opacity-0'
          }`}
        >
          Welcome to Jobsy
        </h1>
        <p
          className={`text-lg md:text-xl text-gray-200 mb-10 max-w-xl transition-opacity duration-1000 delay-200 ${
            fade ? 'opacity-100' : 'opacity-0'
          }`}
        >
          Your future starts with the right connection. Find jobs, connect with employers, and grow your career.
        </p>
        <div
          className={`flex flex-col sm:flex-row gap-6 transition-opacity duration-1000 delay-400 ${
            fade ? 'opacity-100' : 'opacity-0'
          }`}
        >
          <button
            onClick={() => navigate('/signup')}
            className="px-8 py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg transition duration-300"
          >
            Sign Up
          </button>
          <button
            onClick={() => navigate('/jobs')}
            className="px-8 py-3 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold rounded-lg transition duration-300"
          >
            Browse Jobs
          </button>
        </div>
      </main>
    </div>
  );
};

export default Landing;
