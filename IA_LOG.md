# Registro de uso de IA (IA_LOG.md)

* **Herramienta de IA usada:** Gemini (Google)
* **Prompts principales:** 
  1. "tengo que hacer esto para tres apis que son estas... DummyJSON, Frankfurter, JSONPlaceholder... dime el paso a paso de que tengo que hacer para lograr una buena implementacion segun la pizarra"
  2. "no tenemos 3 apis la primera y segund usan un mapeador y la 2 y tercera otro comparten la base de dtaos, ahora ayudame ahacer esta parte..."
  
* **Decisiones del diseño propuesto por la IA que cambié y por qué:** 
  
  1. **Uso de Mermaid vs Imágenes estáticas:** La IA propuso inicialmente generar imágenes externas para los diagramas. Decidí utilizar diagramas generados por código `Mermaid` directamente dentro del Markdown. Esto permite cumplir con la entrega visual (se renderiza en GitHub) manteniendo el código versionable y editable.
  
  2. **Corrección lógica en la convergencia de APIs:** La IA sugirió un flujo independiente para cada API. Usando mi razonamiento sobre las reglas de negocio del examen, intervine para forzar que los datos de la API 1 (DummyJSON) y la API 2 (Frankfurter) pasaran obligatoriamente por un "Mapeador 1" conjunto. Así me aseguro de que la Base de Datos guarde el precio ya calculado en BOB/EUR, actuando como la verdadera "fuente única de verdad" para la interfaz.
  
  3. **Separación estricta de DTOs y Entities (MVVM):** La IA tendía a usar las mismas clases de datos para todo. Exigí separar estrictamente los `DTOs` (respuestas volátiles de Retrofit) de las `@Entity` (tablas de Room). Aplicando los conceptos de la clase, sé que si la estructura del JSON de la API cambia mañana, mi esquema de base de datos local no debe romperse.
  
  4. **Uso de Flow en lugar de Suspend para consultas:** En el diseño de la base de datos y la comunicación hacia el ViewModel, la IA sugirió usar funciones `suspend` para leer datos de Room. Corregí este diseño para usar `Flow<List<T>>` en las consultas `SELECT` de lectura. Esto garantiza que la vista en Compose (View) sea reactiva y se actualice automáticamente usando `collectAsState()` cada vez que la tabla cambie.