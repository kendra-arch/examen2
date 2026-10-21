# Diseño de Clases y Base de Datos - Explorador de Productos y Mercado (Grupo 1)

## 1. Diseño de Clases (DTOs, Modelos y Entidades)

### DTOs (Data Transfer Objects)
- **DummyProductDto & DummyProductsResponseDto**: Mapean la respuesta de `https://dummyjson.com/products` (`id`, `title`, `price`, `category`, `images`).
- **FrankfurterResponseDto**: Mapean la respuesta de `https://api.frankfurter.dev/v2/rates` (`base`, `date`, `rates`).
- **PostDto**: Mapean la respuesta de `https://jsonplaceholder.typicode.com/posts` (`userId`, `id`, `title`, `body`).

### Modelos de Dominio
- **DummyProductResumen**: Representa un producto en la UI con su id, título, precio, categoría e imagen.
- **FrankfurterResumen**: Representa los tipos de cambio con base, fecha y mapa de tasas.
- **JSONPlaceholderResumen**: Representa una publicación con id, título y contenido (`body`).

### Entidad de Base de Datos (Room)
- **FavoritoEntity**:
  - `@PrimaryKey val id: Int`
  - `val quote: String` (Almacena el nombre e identidad del producto, precio o publicación)
  - `val agregadoEn: Long`

---

## 2. Diseño de Base de Datos (Room)
- **Base de Datos**: `KanyeWestDatabase`
- **Tabla**: `favoritos`
- **Columnas**:
  - `id` (INTEGER, Primary Key)
  - `quote` (TEXT, Identidad / Título / Precio del ítem)
  - `agregadoEn` (INTEGER, Timestamp)
- **DAO**: `FavoritoDao` con soporte `Flow` y `suspend`.

---

## 3. Arquitectura MVVM y Mapeadores Compartidos
- **Capa Data**: Retrofit Services, Repositorios y Room DAO.
- **Capa Mapeadores**:
  - **Mapeador 1 (DummyJSON & Frankfurter)**: Comparte lógica de mapeo para transformar precios y productos a identidad de BD.
  - **Mapeador 2 (Frankfurter & JSONPlaceholder)**: Comparte lógica de mapeo para estructurar metadatos y tasas hacia la BD.
- **Capa ViewModel**: Gestiona estados reactivos (`StateFlow`, `MutableStateFlow`) para las 3 APIs y favoritos.
- **Capa UI**: Pantallas Jetpack Compose con LazyColumn, Buscador con `derivedStateOf`, detalle, favoritos con botón de eliminar e indicador de lista vacía.
