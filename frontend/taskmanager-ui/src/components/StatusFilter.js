import React from 'react';

const StatusFilter = ({ activeFilter, onFilterChange, counts }) => {
  const filters = [
    { key: 'ALL', label: 'All Tasks', emoji: '📋' },
    { key: 'TODO', label: 'To Do', emoji: '⏳' },
    { key: 'IN_PROGRESS', label: 'In Progress', emoji: '🔄' },
    { key: 'DONE', label: 'Done', emoji: '✅' },
  ];

  return (
    <div className="status-filter">
      {filters.map(filter => (
        <button
          key={filter.key}
          className={`filter-btn ${activeFilter === filter.key ? 'active' : ''}`}
          onClick={() => onFilterChange(filter.key)}
        >
          {filter.emoji} {filter.label}
          <span className="filter-count">{counts[filter.key]}</span>
        </button>
      ))}
    </div>
  );
};

export default StatusFilter;