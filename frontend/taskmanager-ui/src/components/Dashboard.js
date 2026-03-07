import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SUMMARY_URL = '/api/tasks/summary';

function Dashboard({ onStatusTileClick, refreshKey }) {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    axios.get(SUMMARY_URL)
      .then(res => setSummary(res.data))
      .catch(() => setError('Failed to load dashboard data.'))
      .finally(() => setLoading(false));
  }, [refreshKey]);

  if (loading) return <div className="loading">Loading dashboard...</div>;
  if (error)   return <div className="dashboard-error">{error}</div>;

  const statusTiles = [
    { label: 'To Do',       key: 'TODO',        count: summary.byStatus?.TODO        ?? 0, className: 'tile-todo' },
    { label: 'In Progress', key: 'IN_PROGRESS',  count: summary.byStatus?.IN_PROGRESS ?? 0, className: 'tile-in-progress' },
    { label: 'Done',        key: 'DONE',         count: summary.byStatus?.DONE        ?? 0, className: 'tile-done' },
  ];

  const priorityTiles = [
    { label: '🔴 High',   count: summary.byPriority?.HIGH   ?? 0, className: 'tile-high' },
    { label: '🟡 Medium', count: summary.byPriority?.MEDIUM ?? 0, className: 'tile-medium' },
    { label: '🟢 Low',    count: summary.byPriority?.LOW    ?? 0, className: 'tile-low' },
  ];

  return (
    <div className="dashboard">

      <div className="dashboard-section">
        <div className="dashboard-tile tile-total">
          <span className="tile-count">{summary.total}</span>
          <span className="tile-label">Total Tasks</span>
        </div>
        <div className="dashboard-tile tile-overdue">
          <span className="tile-count">{summary.overdue}</span>
          <span className="tile-label">Overdue</span>
        </div>
      </div>

      <h2 className="dashboard-section-title">By Status</h2>
      <div className="dashboard-section">
        {statusTiles.map(tile => (
          <button
            key={tile.key}
            className={`dashboard-tile tile-clickable ${tile.className}`}
            onClick={() => onStatusTileClick(tile.key)}
            title={`View ${tile.label} tasks`}
          >
            <span className="tile-count">{tile.count}</span>
            <span className="tile-label">{tile.label}</span>
          </button>
        ))}
      </div>

      <h2 className="dashboard-section-title">By Priority</h2>
      <div className="dashboard-section">
        {priorityTiles.map(tile => (
          <div key={tile.label} className={`dashboard-tile ${tile.className}`}>
            <span className="tile-count">{tile.count}</span>
            <span className="tile-label">{tile.label}</span>
          </div>
        ))}
      </div>

    </div>
  );
}

export default Dashboard;
