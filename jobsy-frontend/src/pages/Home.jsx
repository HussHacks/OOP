import React from 'react';
import { useNavigate } from 'react-router-dom';
import TextPressure from '../components/TextPressure';

export default function Home() {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center text-center text-white w-full min-h-[calc(100vh-5rem)] px-4">
      <div className="w-full max-w-[800px]">
        <TextPressure
          text="Jobsy"
          fontFamily="Compressa VF"
          fontUrl="https://res.cloudinary.com/dr6lvwubh/raw/upload/v1529908256/CompressaPRO-GX.woff2"
          width={true}
          weight={true}
          italic={true}
          alpha={false}
          flex={true}
          stroke={false}
          scale={true}
          textColor="#FFFFFF"
          strokeColor="#FF0000"
          strokeWidth={2}
          minFontSize={10}
          className="w-full"
        />
      </div>

      {/* Buttons below TextPressure */}
      <div className="flex flex-col sm:flex-row gap-6 mt-8">
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
    </div>
  );
}
