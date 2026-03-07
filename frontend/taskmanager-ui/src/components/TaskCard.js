import React from 'react';

const priorityConfig = {
  HIGH:   { label: 'High',   className: 'priority-high' },
  MEDIUM: { label: 'Medium', className: 'priority-medium' },
  LOW:    { label: 'Low',    className: 'priority-low' },
};

const statusConfig = {
  TODO: { label: 'To Do', emoji: '⏳', className: 'status-todo' },
  IN_PROGRESS: { label: 'In Progress', emoji: '🔄', className: 'status-inprogress' },
  DONE: { label: 'Done', emoji: '✅', className: 'status-done' },
};

const TaskCard = ({ task, onEdit, onDelete }) => {
  const status = statusConfig[task.status] || statusConfig.TODO;
  const priority = priorityConfig[task.priority] || priorityConfig.MEDIUM;

  const formatDate = (dateStr) => {
    if (!dateStr) return null;
    return new Date(dateStr).toLocaleDateString('en-US', {
      year: 'numeric', month: 'short', day: 'numeric'
    });
  };

  const isOverdue = () => {
    if (!task.dueDate || task.status === 'DONE') return false;
    return new Date(task.dueDate) < new Date();
  };

  return (
    <div className={`task-card ${isOverdue() ? 'overdue' : ''}`}>
      <div className="task-card-header">
        <span className={`status-badge ${status.className}`}>
          {status.emoji} {status.label}
        </span>
        <span className={`priority-badge ${priority.className}`}>
          {priority.label}
        </span>
        <div className="task-actions">
          <button className="btn btn-edit" onClick={() => onEdit(task)}>✏️ Edit</button>
          <button className="btn btn-delete" onClick={() => onDelete(task.id)}>🗑️ Delete</button>
        </div>
      </div>
      <h3 className="task-title">{task.title}</h3>
      {task.description && (
        <p className="task-description">{task.description}</p>
      )}
      <div className="task-footer">
        {task.dueDate && (
          <span className={`due-date ${isOverdue() ? 'overdue-text' : ''}`}>
            📅 Due: {formatDate(task.dueDate)}
            {isOverdue() && ' — Overdue!'}
          </span>
        )}
        <span className="created-date">
          🕐 Created: {formatDate(task.createdAt)}
        </span>
      </div>
    </div>
  );
};

export default TaskCard;