const API_URL='http://localhost:8080/api/v1/tasks';

console.log("fetching logs");

async function getAllTasks()
{
 console.log("Calling getAllTasks...");
    const res=await fetch(API_URL);
    const tasks=await res.json();
    const container=document.getElementById('task-list');
    container.innerHTML='';
    tasks.forEach(task=>{
    container.innerHTML +=`
    <div class="task-card">
    <h3>${task.title}</h3>
    <p>${task.description}</p>
    <p>${task.status}</p>
    <p>Due: ${task.dueDateTime?.replace('T', ' ')}</p>
        <button onclick="editTask(${task.id})">Edit</button>
        <button onclick="deleteTask(${task.id})">Delete</button>
      </div>
    `;
});
}

function editTask(id) {
  window.location.href = `create.html?id=${id}`;
}

async function deleteTask(id) {
  if (confirm('Are you sure?')) {
    await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    getAllTasks();
  }
}
async function setupForm() {
  const params = new URLSearchParams(window.location.search);
  const id = params.get('id');
    console.log("Setup form");
  if (id) {
    const res = await fetch(`${API_URL}/${id}`);
    const task = await res.json();
    document.getElementById('taskId').value = task.id;
    document.getElementById('title').value = task.title;
    document.getElementById('description').value = task.description;
    document.getElementById('status').value = task.status;
    document.getElementById('dueDateTime').value = task.dueDateTime?.slice(0,16);
    document.getElementById('form-title').innerText = 'Edit Task';
  }

  document.getElementById('task-form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const task = {
      title: document.getElementById('title').value,
      description: document.getElementById('description').value,
      status: document.getElementById('status').value,
      dueDateTime: document.getElementById('dueDateTime').value
    };
    const taskId = document.getElementById('taskId').value;

    if (taskId) {
      await fetch(`${API_URL}/${taskId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task)
      });
    } else {
      await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task)
      });
    }

    window.location.href = 'index.html';
  });
}