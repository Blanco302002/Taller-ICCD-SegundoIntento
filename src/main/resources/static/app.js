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

    tasks.forEach(task => list.appendChild(renderTask(task)));
}

// Dibuja una tarea en modo NORMAL: checkbox + título a la izquierda,
// botones "Modificar" y "Eliminar" a la derecha.
function renderTask(task) {
    const li = document.createElement("li");
    li.className = "list-group-item d-flex justify-content-between align-items-center";

    // ----- Izquierda: checkbox de completada + título -----
    const left = document.createElement("div");
    left.className = "d-flex align-items-center gap-2";

    // Checkbox: al tildarlo/destildarlo hace un PUT cambiando 'completed'.
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.className = "form-check-input mt-0";
    checkbox.checked = task.completed;
    checkbox.addEventListener("change", () => updateTask(task, { completed: checkbox.checked }));

    // Título (tachado y gris si la tarea está completada).
    const titleSpan = document.createElement("span");
    titleSpan.textContent = task.title;
    if (task.completed) {
        titleSpan.classList.add("text-decoration-line-through", "text-muted");
    }

    left.appendChild(checkbox);
    left.appendChild(titleSpan);

    // ----- Derecha: botones Modificar y Eliminar -----
    const buttons = document.createElement("div");
    buttons.className = "d-flex gap-2";

    const editButton = document.createElement("button");
    editButton.className = "btn btn-outline-secondary btn-sm";
    editButton.textContent = "Modificar";
    editButton.addEventListener("click", () => enterEditMode(li, task));

    const deleteButton = document.createElement("button");
    deleteButton.className = "btn btn-danger btn-sm";
    deleteButton.textContent = "Eliminar";
    deleteButton.addEventListener("click", () => deleteTask(task.id));

    buttons.appendChild(editButton);
    buttons.appendChild(deleteButton);

    li.appendChild(left);
    li.appendChild(buttons);
    return li;
}

// Convierte el item a modo EDICIÓN: un input con el título + Guardar/Cancelar.
function enterEditMode(li, task) {
    li.innerHTML = "";

    // Input con el título actual (ocupa el espacio disponible).
    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control form-control-sm flex-grow-1 me-2";
    input.value = task.title;

    const buttons = document.createElement("div");
    buttons.className = "d-flex gap-2";

    // Guardar: PUT con el nuevo título.
    const saveButton = document.createElement("button");
    saveButton.className = "btn btn-success btn-sm";
    saveButton.textContent = "Guardar";
    saveButton.addEventListener("click", () => updateTask(task, { title: input.value }));

    // Cancelar: vuelve a dibujar la lista normal, descartando el cambio.
    const cancelButton = document.createElement("button");
    cancelButton.className = "btn btn-secondary btn-sm";
    cancelButton.textContent = "Cancelar";
    cancelButton.addEventListener("click", () => loadTasks());

    buttons.appendChild(saveButton);
    buttons.appendChild(cancelButton);

    li.appendChild(input);
    li.appendChild(buttons);
    input.focus();
}

// Modifica una tarea (PUT /tasks/{id}). 'changes' son los campos a cambiar;
// el resto se mantiene igual al de la tarea actual.
async function updateTask(task, changes) {
    const updated = { title: task.title, completed: task.completed, ...changes };

    await fetch(`${API}/${task.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updated)
    });

    loadTasks();
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
