// URL de la API. Es RELATIVA (sin http://localhost): la página y la API
// se sirven desde el mismo lugar, así funciona tanto en tu compu
// (http://localhost:8080) como en Render (https://...onrender.com).
const API = "/tasks";

// Referencias a los elementos del HTML que vamos a usar.
const list = document.getElementById("task-list");
const loading = document.getElementById("loading");
const emptyMessage = document.getElementById("empty-message");

// Trae todas las tareas (GET /tasks) y las dibuja en la lista.
async function loadTasks() {
    loading.classList.remove("d-none");   // mostrar el spinner
    list.innerHTML = "";
    emptyMessage.classList.add("d-none");

    const response = await fetch(API);
    const tasks = await response.json();

    loading.classList.add("d-none");      // ocultar el spinner

    // Si no hay tareas, mostramos el mensajito y salimos.
    if (tasks.length === 0) {
        emptyMessage.classList.remove("d-none");
        return;
    }

    tasks.forEach(task => {
        // Cada tarea es un item de la lista, con el texto a la izquierda
        // y el botón de eliminar a la derecha (justify-content-between).
        const li = document.createElement("li");
        li.className = "list-group-item d-flex justify-content-between align-items-center";

        // Título (tachado y gris si la tarea está completada).
        const titleSpan = document.createElement("span");
        titleSpan.textContent = task.title;
        if (task.completed) {
            titleSpan.classList.add("text-decoration-line-through", "text-muted");
        }

        // Botón rojo y chico para eliminar esta tarea.
        const deleteButton = document.createElement("button");
        deleteButton.className = "btn btn-danger btn-sm";
        deleteButton.textContent = "Eliminar";
        deleteButton.addEventListener("click", () => deleteTask(task.id));

        li.appendChild(titleSpan);
        li.appendChild(deleteButton);
        list.appendChild(li);
    });
}

// Elimina una tarea (DELETE /tasks/{id}) y refresca la lista.
async function deleteTask(id) {
    await fetch(`${API}/${id}`, { method: "DELETE" });
    loadTasks();
}

// Maneja el formulario "Agregar": crea la tarea (POST) y refresca la lista.
document.getElementById("task-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const titleInput = document.getElementById("task-title");

    await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title: titleInput.value, completed: false })
    });

    titleInput.value = "";
    loadTasks();
});

// Carga las tareas cuando se abre la página.
loadTasks();
