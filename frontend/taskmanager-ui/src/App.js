import React, { useState, useEffect } from 'react';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import StatusFilter from './components/StatusFilter';
import axios from 'axios';
import './App.css';

const API_URL = '/api/tasks';

function App() {
  const [tasks, setTasks] = useState([]);
  const [filteredTasks, setFilteredTasks] = useState([]);
  const [activeFilter, setActiveFilter] = useState('ALL');
  const [editingTask, setEditingTask] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTasks();
  }, []);

  useEffect(() => {
    if (activeFilter === 'ALL') {
      setFilteredTasks(tasks);
    } else {
      setFilteredTasks(tasks.filter(t => t.status === activeFilter));
    }
  }, [tasks, activeFilter]);

  const fetchTasks = async () => {
    try {
      const response = await axios.get(API_URL);
      setTasks(response.data);
    } catch (error) {
      console.error('Error fetching tasks:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (task) => {
    try {
      await axios.post(API_URL, task);
      fetchTasks();
      setShowForm(false);
    } catch (error) {
      console.error('Error creating task:', error);
    }
  };

  const handleUpdate = async (id, task) => {
    try {
      await axios.put(`${API_URL}/${id}`, task);
      fetchTasks();
      setEditingTask(null);
      setShowForm(false);
    } catch (error) {
      console.error('Error updating task:', error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await axios.delete(`${API_URL}/${id}`);
        fetchTasks();
      } catch (error) {
        console.error('Error deleting task:', error);
      }
    }
  };

  const handleEdit = (task) => {
    setEditingTask(task);
    setShowForm(true);
  };

  const handleCancel = () => {
    setEditingTask(null);
    setShowForm(false);
  };

  return (
    <div className="app">
      <header className="app-header">
        <div className="header-content">
          <h1>📋 Task Manager</h1>
          <button
            className="btn btn-primary"
            onClick={() => { setEditingTask(null); setShowForm(true); }}
          >
            + New Task
          </button>
        </div>
      </header>

      <main className="app-main">
        {showForm && (
          <TaskForm
            task={editingTask}
            onSubmit={editingTask
              ? (data) => handleUpdate(editingTask.id, data)
              : handleCreate}
            onCancel={handleCancel}
          />
        )}

        <StatusFilter
          activeFilter={activeFilter}
          onFilterChange={setActiveFilter}
          counts={{
            ALL: tasks.length,
            TODO: tasks.filter(t => t.status === 'TODO').length,
            IN_PROGRESS: tasks.filter(t => t.status === 'IN_PROGRESS').length,
            DONE: tasks.filter(t => t.status === 'DONE').length,
          }}
        />

        {loading ? (
          <div className="loading">Loading tasks...</div>
        ) : (
          <TaskList
            tasks={filteredTasks}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        )}
      </main>
    </div>
  );
}

export default App;