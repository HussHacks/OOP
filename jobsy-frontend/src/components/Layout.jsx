import PillNav from './Navbar';
import Silk from './Backgrounds/Silk';

export default function Layout({ children }) {
  const navItems = [
    { label: 'Home', href: '/' },
    { label: 'Jobs', href: '/jobs' },
    { label: 'About', href: '/about' }
  ];

  return (
    <div className="relative w-full min-h-screen overflow-hidden">
      {/* Silk background */}
      <div className="absolute top-0 left-0 w-full h-full -z-10">
        <Silk />
      </div>

      {/* Navbar */}
      <PillNav logo="/assets/JobsyJ.png" items={navItems} activeHref="/" />

      {/* Page content */}
      <div className="pt-[5rem]">{children}</div>
    </div>
  );
}
