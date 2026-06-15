# 📋 Task Demo — Integración y Entrega Continua

![CI/CD](https://github.com/Blanco302002/Taller-ICCD-SegundoIntento/actions/workflows/ci.yml/badge.svg)

Aplicación web simple de **gestión de tareas** (crear, listar y eliminar) construida
para demostrar un flujo completo de **Integración Continua (CI)** y **Entrega Continua (CD)**.

El proyecto es deliberadamente chico y legible: el foco no está en la complejidad de la
app, sino en el **circuito de automatización** que la rodea (repositorio → servidor de CI →
pruebas automáticas → despliegue → feedback).

🌐 **Demo en vivo:** https://tareas-demo.onrender.com

---

## ✨ Funcionalidades

- Listar todas las tareas.
- Agregar una tarea nueva.
- Eliminar una tarea.
- Interfaz web simple con Bootstrap (formulario, lista y spinner de carga).

---

## 🧱 Stack tecnológico

| Capa | Herramienta |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.3 (Web + Data JPA) |
| Base de datos | H2 (en archivo para runtime, en memoria para tests) |
| Gestor de paquetes / build | Maven |
| Tests | JUnit 5 + Mockito + MockMvc |
| Contenedor | Docker |
| Servidor de CI/CD | GitHub Actions |
| Entorno de entrega | Render |
| Feedback | Slack (webhook) |
| Frontend | HTML + JavaScript vanilla + Bootstrap 5 |

---

## 🗂️ Estructura del proyecto

```
.
├── src/main/java/com/example/taskdemo/
│   ├── TaskDemoApplication.java   # punto de entrada
│   ├── Task.java                  # entidad (id, title, completed)
│   ├── TaskRepository.java        # repositorio JPA (CRUD automático)
│   ├── TaskService.java           # lógica de negocio
│   └── TaskController.java        # endpoints REST /tasks
├── src/main/resources/
│   ├── application.properties     # config de runtime (H2 en archivo)
│   └── static/                    # frontend servido por Spring
│       ├── index.html
│       ├── app.js
│       └── styles.css
├── src/test/                      # tests unitarios y de integración
├── Dockerfile                     # receta para empaquetar la app en una imagen
└── .github/workflows/ci.yml       # pipeline CI/CD
```

---

## 🚀 Cómo correr el proyecto localmente

**Requisitos:** Java 21 y Maven instalados.

```bash
# 1. Levantar la app
mvn spring-boot:run

# 2. Abrir en el navegador
#    Frontend:      http://localhost:8080/
#    API de tareas: http://localhost:8080/tasks
#    Consola H2:    http://localhost:8080/h2-console
```

**Para empaquetar el jar ejecutable:**

```bash
mvn package
java -jar target/task-demo-0.0.1-SNAPSHOT.jar
```

---

## 🔌 Endpoints de la API

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/tasks` | Lista todas las tareas |
| `POST` | `/tasks` | Crea una tarea (body JSON: `{ "title": "...", "completed": false }`) |
| `PUT` | `/tasks/{id}` | Actualiza una tarea existente |
| `DELETE` | `/tasks/{id}` | Elimina una tarea |

---

## 🗄️ Base de datos (H2)

Se usa **H2**, una base de datos SQL liviana que corre dentro de la propia aplicación.

- **Runtime:** modo archivo (`jdbc:h2:file:./data/taskdb`). Los datos **persisten**
  entre reinicios (se guardan en la carpeta `data/`).
- **Tests:** modo memoria (`jdbc:h2:mem:testdb`), aislada y descartable, para que los
  tests nunca toquen ni ensucien los datos reales.

Para inspeccionar la base con la consola web: `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:file:./data/taskdb`, usuario `sa`, sin contraseña).

---

## ✅ Pruebas automatizadas

```bash
mvn test
```

- **Tests unitarios** (`TaskServiceTest`): prueban la lógica del *service* de forma
  aislada, simulando (mock) el repositorio con Mockito. No usan base de datos.
- **Tests de integración** (`TaskControllerIntegrationTest`): levantan el contexto
  completo de Spring y prueban todo el camino *HTTP → controller → service → repositorio
  → base H2*, usando `MockMvc` y `@Transactional` para aislar cada test.

---

## 🔄 Pipeline de CI/CD

Definido en [`.github/workflows/ci.yml`](.github/workflows/ci.yml), se ejecuta en cada
Pull Request y en cada push a `main`:

1. **Build + Tests** *(Integración Continua)* — instala Java 21, corre las pruebas
   automáticas y empaqueta la app. Si algo falla, el pipeline se corta.
2. **Deploy a Render** *(Entrega Continua)* — solo en push a `main` y solo si los tests
   pasaron, dispara el despliegue de la app en Render.
3. **Feedback a Slack** — avisa al canal del equipo si el pipeline salió bien o mal.

```
push a main  →  GitHub Actions (tests)  →  deploy a Render  →  aviso a Slack
```

---

## 🐳 Docker

La app se empaqueta como imagen Docker mediante un [`Dockerfile`](Dockerfile) multi-stage
(una etapa compila con Maven, otra solo ejecuta el jar). Render construye y corre esta
imagen en la nube.

```bash
# Construir y correr localmente (requiere Docker)
docker build -t task-demo .
docker run -p 8080:8080 task-demo
```

---

## 🌳 Flujo de trabajo con Git

Se trabaja en la rama `development` y se integra a `main` mediante Pull Requests, lo que
dispara el pipeline antes de cada despliegue.
