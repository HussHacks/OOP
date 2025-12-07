import { useEffect, useState } from 'react';
import api from './api'; // import the Axios instance

function JobsList() {
  const [jobs, setJobs] = useState([]); // state to store jobs

  useEffect(() => {
    // Fetch jobs from backend when the component mounts
    api.get('/jobs') // calls http://localhost:8080/jobs
      .then(response => setJobs(response.data)) // save response to state
      .catch(error => console.error(error));
  }, []);

  return (
    <div>
      <h1>Jobs</h1>
      <ul>
        {jobs.map(job => (
          <li key={job.id}>
            <h3>{job.title}</h3>
            <p>{job.description}</p>
            <p>Company: {job.companyName}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default JobsList;
