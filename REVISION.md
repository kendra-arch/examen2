# Registro de Revisión y Corrección de Defectos (REVISION.md)

Este documento registra los defectos, riesgos y vulnerabilidades detectados en el código generado por IA para las capas de **APIs (Checkpoint 3)** y **Base de Datos / Persistencia (Checkpoint 4)**, explicando por qué representan un problema y la corrección aplicada.

---

## 🌐 Capa de APIs y MVVM (Checkpoint 3)

### Defecto 1: Intento de llamada de red directa dentro de un Composable (Efecto secundario no controlado)
- **Archivo afectado**: `MainScreen.kt`
- **Por qué es un problema**: Si se ejecutan solicitudes HTTP de forma directa o síncrona dentro del cuerpo de un Composable, la petición se disparará repetidamente en cada recomposición de la interfaz, provocando bucles infinitos de red, parpadeos, consumo excesivo de datos o una excepción de red en el hilo principal (`NetworkOnMainThreadException`).
- **Corrección aplicada**: Se removió toda lógica de red de las pantallas. Las llamadas se centralizaron exclusivamente en el `MainViewModel` utilizando corrutinas asíncronas (`viewModelScope.launch`), exponiendo los datos a la UI mediante flujos reactivos (`StateFlow` con `UiState`).

### Defecto 2: Excepciones no atrapadas durante el consumo de servicios REST (Fallo ante caídas de conexión)
- **Archivo afectado**: `DummyProductRepository.kt`, `FrankfurterRepository.kt`, `JSONPlaceholderRepository.kt` (gestionadas en `MainViewModel.kt`)
- **Por qué es un problema**: Ante un fallo de red (sin internet, timeout o error HTTP 500), si la excepción no es capturada, la aplicación sufrirá un cierre forzoso (crash) inmediato.
- **Corrección aplicada**: Se implementó un manejo defensivo de errores mediante bloques `try-catch` estructurados en el ViewModel, capturando las excepciones e inyectando un estado de error (`UiState.Error`) que permite mostrar una interfaz amigable con un botón de reintento (`onRetry`).

### Defecto 3: Mutación directa de la lista de elementos en memoria (Riesgo en reactividad con derivedStateOf)
- **Archivo afectado**: `MainViewModel.kt` y `MainScreen.kt`
- **Por qué es un problema**: Modificar colecciones directamente en los flujos de estado rompe la inmutabilidad requerida por Jetpack Compose, impidiendo que el buscador con `derivedStateOf` actualice correctamente los elementos filtrados.
- **Corrección aplicada**: Se utilizaron colecciones inmutables (`List` de solo lectura) y estados derivados eficientes (`derivedStateOf` junto con `remember`) para recalcular listas filtradas sin alterar la fuente original de datos.

---

## 🗄️ Capa de Base de Datos y Persistencia - Room (Checkpoint 4)

### Defecto 4: Operaciones de base de datos ejecutadas en el hilo principal de la UI
- **Archivo afectado**: `FavoritosRepository.kt` y `FavoritoDao.kt`
- **Por qué es un problema**: Ejecutar inserciones, consultas o borrados en Room directamente en el hilo principal bloquea el dibujado de la interfaz (`ANR - Application Not Responding`).
- **Corrección aplicada**: Se añadieron modificadores de suspensión (`suspend`) en el DAO y `withContext(Dispatchers.IO)` en el repositorio para garantizar que todas las operaciones de escritura y lectura en Room ocurran estrictamente en el hilo de E/S (`IO`).

### Defecto 5: Ausencia de manejo de estado vacío (Lista de favoritos sin retroalimentación)
- **Archivo afectado**: `MainScreen.kt` (FavoritesTab)
- **Por qué es un problema**: Si la base de datos no contenía ningún registro favorito guardado, la pantalla mostraba un contenedor vacío confuso para el usuario sin ninguna explicación.
- **Corrección aplicada**: Se implementó una verificación condicional de la lista de favoritos (`favoritosList.isEmpty()`) para mostrar el mensaje requerido `"Sin elementos guardados"`, además de incorporar un botón de eliminación individual operado mediante la consulta `@Query DELETE` en el DAO.
