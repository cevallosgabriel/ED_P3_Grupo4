# 🎮 Tres en Raya contra la Computadora (IA Minimax)

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-blue?style=for-the-badge&logo=openjdk" alt="Java 17" />
  <img src="https://img.shields.io/badge/JavaFX-19-orange?style=for-the-badge&logo=java" alt="JavaFX 19" />
  <img src="https://img.shields.io/badge/Android-SDK-brightgreen?style=for-the-badge&logo=android" alt="Android SDK" />
  <img src="https://img.shields.io/badge/ESPOL-Estructuras_de_Datos-navy?style=for-the-badge" alt="ESPOL" />
</p>

> **Proyecto Universitario de Estructuras de Datos**  
> Implementación completa del juego interactivo "Tres en Raya" potenciado por Inteligencia Artificial basada en **Árboles N-arios** y el algoritmo **Minimax**, disponible tanto para **Desktop (JavaFX)** como para **Dispositivos Móviles (Android Nativo)**.

---

## 👥 Equipo de Desarrollo (Grupo 4)

| Integrante | Rol / Contribución |
| :--- | :--- |
| 🧑‍💻 **Gabriel Cevallos** | Lógica Principal, Estructura del Árbol N-ario y Controlador Minimax |
| 🧑‍💻 **Dylan Pincay** | Diseño e Integración de Interfaz Gráfica (JavaFX & Android UI) |
| 👩‍💻 **Helen Cruz** | Modelos de Datos, Persistencia (Serialización) e Iteradores |

---

## 📸 Capturas del Proyecto

<div align="center">
  <img width="280" alt="Vista Android" src="https://github.com/user-attachments/assets/34157f17-298a-40dc-91f7-35494c2224c9" />
</div>

---

## 🚀 Características Principales

- 🧠 **Toma de Decisiones por IA**: Computadora imbatible mediante evaluación en Árbol N-ario y estrategia Minimax.
- 💡 **Sistema de Sugerencias en Tiempo Real**: Recomienda dinámicamente al usuario la casilla con mayor ventaja táctica.
- 👁️ **Visualización del Recorrido (BFS)**: Modo interactivo que demuestra visualmente la evaluación y descartes de alternativas por parte de la IA.
- ↺ **Deshacer Movimientos**: Posibilidad de revertir el último turno (jugada del humano y respuesta del bot).
- 💾 **Persistencia de Partidas**: Guardado manual (`guardado.ser`) y guardado automático transparente (`autoGuardado.ser`) usando serialización Java.
- 🛡️ **Manejo de Reanudación Segura**: Inicio garantizado y limpio de partida vacía si el usuario solicita reanudar sin una partida previa guardada.
- 🎨 **Interfaz Multiplataforma Premium**:
  - **JavaFX Desktop**: Estilizado moderno CSS con tarjetas elevadas, gradientes y esquema de color Índigo/Slate.
  - **Android Nativo**: Aceleración por hardware (60 FPS), efectos táctiles *Material Ripple* y animaciones de rebote.

---

## 🌲 Estructura de Datos y Lógica del Algoritmo Minimax

### 1. Árbol N-ario Genérico (`Tree<E>` y `TreeNode<E>`)
Implementación desde cero sin dependencias de librerías no lineales nativas. Cada nodo del árbol (`TreeNode<Board>`) contiene:
- `data`: Copia inmutable del estado del tablero (`Board`).
- `children`: Lista de nodos hijos (`List<TreeNode<Board>>`).
- `movement`: Índice de la casilla del movimiento efectuado (0 al 8).
- `utility`: Valor de utilidad calculado.
- `maximizing`: Booleano que define si el nivel representa el turno de **MAX (Bot)** o **MIN (Humano)**.

### 2. Algoritmo Minimax en 4 Pasos Estrictos
1. **Generación del Árbol de Decisiones (Profundidad 2)**:
   - **Nivel 0 (Raíz)**: Estado actual del tablero.
   - **Nivel 1**: Movimientos posibles de la computadora (`maximizing = false`).
   - **Nivel 2 (Hojas)**: Respuestas del oponente a cada tablero del Nivel 1 (`maximizing = true`).
2. **Cálculo de la Función de Utilidad (Heurística)**:
   En cada nodo hoja $t$, se evalúa:
   $$U_{jugador}(t) = P_{jugador} - P_{oponente}$$
   Donde $P$ representa el número de líneas (filas, columnas o diagonales) aún disponibles y no bloqueadas por el oponente.
3. **Propagación del Mínimo (Nivel 1)**:
   Para cada familia de nodos hermanos en Nivel 2 (respuestas del rival), se encuentra el valor de utilidad **MÍNIMO** y se asocia a su nodo padre en el Nivel 1.
4. **Elección de la Utilidad Máxima**:
   La computadora selecciona el nodo del Nivel 1 que contenga la utilidad **MÁXIMA** de entre todos los mínimos calculados y ejecuta dicho movimiento.

---

## 🛠️ Arquitectura del Proyecto (Patrón MVC)

```text
com.espol.proyectoestruturadatos
│
├── dstructure/                  # Estructuras de Datos Propias
│   ├── Tree.java                # Árbol N-ario genérico y constructor de árbol de decisión
│   ├── TreeNode.java            # Nodo del Árbol N-ario
│   └── Minimax.java             # Motor de Inteligencia Artificial (Minimax)
│
├── model/                       # Capa del Modelo de Datos
│   ├── board/
│   │   ├── Board.java           # Tablero 3x3 y evaluación heurística de líneas
│   │   ├── Box.java             # Casilla del tablero
│   │   ├── Symbol.java          # Ficha del juego ('X' u 'O')
│   │   └── iterators/           # Iteradores personalizados por Fila, Columna y Diagonal
│   │       ├── IteratorByRow.java
│   │       ├── IteratorByColumn.java
│   │       └── IteratorByDiagonal.java
│   └── player/
│       ├── Player.java          # Clase base abstracta de jugador
│       ├── Human.java           # Jugador Humano
│       └── Bot.java             # Jugador Computadora
│
├── controller/                  # Capa de Controladores (MVC)
│   ├── MainController.java      # Coordinador global del flujo de juego
│   ├── BoardController.java     # Controlador de turnos, estado y reglas
│   └── ChooseController.java    # Configuración de símbolos y turnos iniciales
│
├── ProyectoEstruturaDatos.java  # Clase principal de entrada
└── ProyectoFXUnificado.java     # Aplicación Principal JavaFX (Interfaz Gráfica Desktop)
```

---

## 💻 Requisitos e Instalación

### Prerrequisitos
- **Java JDK 17** o superior.
- **Apache Maven 3.8+**.
- **Android Studio** (Opcional, solo para recompilar el APK móvil).

### Compilación y Ejecución (Desktop JavaFX)

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/Proyecto_Estructura_Datos.git

# Acceder a la carpeta del proyecto
cd Proyecto_Estructura_Datos/Proyecto_Unificado

# Compilar y ejecutar la aplicación JavaFX
mvn clean javafx:run
```

### Generación del APK Móvil (Android)

```bash
# Compilar binario APK para Android
cd ProyectoTresEnRayaAndroid
.\gradlew.bat assembleDebug
```
*El archivo `.apk` generado se ubicará en `app/build/outputs/apk/debug/app-debug.apk`.*

---

## 📜 Licencia y Universidad

Proyecto desarrollado para la materia de **Estructuras de Datos** en la **Escuela Superior Politécnica del Litoral (ESPOL)**. Todos los derechos reservados.
