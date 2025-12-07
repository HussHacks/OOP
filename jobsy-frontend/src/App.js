import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import { AnimatePresence, motion } from 'framer-motion';
import PillNav from './components/Navbar';
import Silk from './components/Backgrounds/Silk';
import Home from './pages/Home';
import Jobs from './pages/Jobs';
import JobDetails from './pages/JobDetails';
import PostJob from './pages/PostJob';
import About from './pages/About';
import Signup from './pages/Signup';
import Login from './pages/Login';
import Profile from './pages/Profile';
import logo from './assets/JobsyJ.png';

function AppWrapper() {
  const location = useLocation();

  const pageVariants = {
    initial: { opacity: 0, y: 20 },
    animate: { opacity: 1, y: 0, transition: { duration: 0.5, ease: 'easeOut' } },
    exit: { opacity: 0, y: -20, transition: { duration: 0.3, ease: 'easeIn' } }
  };

  // Get logged-in user from localStorage
  const storedUser = JSON.parse(localStorage.getItem('user'));

  // Navbar items depending on login state
  const navItems = storedUser
    ? [
      { label: 'Home', href: '/' },
      { label: 'Jobs', href: '/jobs' },
      { label: 'Profile', href: '/profile' },
      {
        label: 'Logout',
        href: '/login',
        onClick: () => {
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
      },
      { label: 'About', href: '/about' }
    ]
    : [
      { label: 'Home', href: '/' },
      { label: 'Jobs', href: '/jobs' },
      { label: 'Sign Up', href: '/signup' },
      { label: 'Log In', href: '/login' },
      { label: 'About', href: '/about' }
    ];

  return (
    <div className="relative w-full min-h-screen overflow-hidden">
      {/* Silk background */}
      <div className="absolute top-0 left-0 w-full h-full -z-10">
        <Silk />
      </div>

      {/* Navbar */}
      <PillNav logo={logo} items={navItems} activeHref={location.pathname} />

      <div className="pt-[5rem] w-full min-h-screen">
        <AnimatePresence mode="wait">
          <Routes location={location} key={location.pathname}>
            <Route path="/" element={<PageWrapper><Home /></PageWrapper>} />
            <Route path="/jobs" element={<PageWrapper><Jobs /></PageWrapper>} />
            <Route path="/jobs/:id" element={<PageWrapper><JobDetails /></PageWrapper>} />
            <Route path="/post-job" element={<PageWrapper><PostJob /></PageWrapper>} />
            <Route path="/signup" element={<PageWrapper><Signup /></PageWrapper>} />
            <Route path="/login" element={<PageWrapper><Login /></PageWrapper>} />
            <Route path="/about" element={<PageWrapper><About /></PageWrapper>} />
            <Route path="/profile" element={<PageWrapper><Profile /></PageWrapper>} />
          </Routes>
        </AnimatePresence>
      </div>
    </div>
  );
}

// Helper wrapper for Framer Motion
const PageWrapper = ({ children }) => {
  const pageVariants = {
    initial: { opacity: 0, y: 20 },
    animate: { opacity: 1, y: 0, transition: { duration: 0.5, ease: 'easeOut' } },
    exit: { opacity: 0, y: -20, transition: { duration: 0.3, ease: 'easeIn' } }
  };
  return (
    <motion.div variants={pageVariants} initial="initial" animate="animate" exit="exit">
      {children}
    </motion.div>
  );
};

export default function App() {
  return (
    <Router>
      <AppWrapper />
    </Router>
  );
}
