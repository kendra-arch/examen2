# Revisión y Corrección de Defectos (REVISION.md)

## Registro de Riesgos y Defectos

### Defecto 1: Ejecución de peticiones HTTP en el hilo principal
- **Archivo**: `MainViewModel.kt`
- **Problema**: Iniciar peticiones sin corrutinas suspendidas lanzaría una excepción en red.
- **Corrección**: Uso estricto de funciones `suspend` y `viewModelScope.launch` con bloques `try-catch`.

### Defecto 2: Ausencia de manejo de estados vacíos en favoritos
- **Archivo**: `MainScreen.kt`
- **Problema**: Pantalla vacía sin retroalimentación visual al usuario.
- **Corrección**: Agregado de mensaje de lista vacía ("Aún no tienes favoritos" / "Sin elementos guardados") y botón de eliminación mediante consulta `@Query DELETE`.
