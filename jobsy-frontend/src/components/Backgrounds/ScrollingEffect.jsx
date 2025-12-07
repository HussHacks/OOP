// src/components/Backgrounds/ScrollingEffect.jsx
import FluidGlass from './FluidGlass'; // assuming you saved your huge FluidGlass code as FluidGlass.jsx

export default function ScrollingEffect() {
  return (
    <div className="absolute top-0 left-0 w-full h-full -z-20">
      <FluidGlass mode="lens" />
    </div>
  );
}
