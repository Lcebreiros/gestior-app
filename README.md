# Gestior - App Android para Rellenito Alfajores

Aplicación Android nativa desarrollada con Jetpack Compose que consume la API de Rellenito Alfajores.

## Tecnologías Utilizadas

- **Jetpack Compose**: UI moderna y declarativa
- **Hilt**: Inyección de dependencias
- **Retrofit**: Cliente HTTP para consumir la API
- **Coil**: Carga de imágenes
- **Navigation Compose**: Navegación entre pantallas
- **DataStore**: Almacenamiento seguro del token de autenticación
- **Kotlin Coroutines & Flow**: Programación asíncrona
- **Material Design 3**: Diseño moderno

## Estructura del Proyecto

```
app/
├── data/
│   ├── local/
│   │   └── TokenManager.kt           # Gestión del token con DataStore
│   ├── model/                         # Modelos de datos (User, Product, Order, etc.)
│   ├── remote/                        # Servicios API con Retrofit
│   └── repository/                    # Repositorios para lógica de negocio
├── di/
│   └── NetworkModule.kt              # Configuración de Hilt y Retrofit
├── navigation/
│   ├── Screen.kt                     # Definición de rutas
│   └── NavGraph.kt                   # Grafo de navegación
└── ui/
    ├── screens/
    │   ├── auth/                     # Login y Register
    │   ├── dashboard/                # Pantalla principal
    │   ├── products/                 # Listado y gestión de productos
    │   ├── orders/                   # Pedidos
    │   ├── clients/                  # Clientes
    │   └── profile/                  # Perfil de usuario
    └── theme/                        # Tema y colores

```

## Configuración

### 1. Configurar la URL de la API

Edita el archivo [NetworkModule.kt](app/src/main/java/com/example/gestior/di/NetworkModule.kt):

```kotlin
// Para emulador Android (apunta a localhost del host)
private const val BASE_URL = "http://10.0.2.2:8000/api/"

// Para dispositivo físico en la misma red
// private const val BASE_URL = "http://TU_IP_LOCAL:8000/api/"
// Ejemplo: "http://192.168.1.100:8000/api/"

// Para producción
// private const val BASE_URL = "https://tu-dominio.com/api/"
```

### 2. Iniciar el servidor Laravel

Asegúrate de tener el backend corriendo:

```bash
cd /home/leandro/rellenito-alfajores
php artisan serve
```

### 3. Sincronizar el proyecto

En Android Studio:
1. Abre el proyecto
2. Haz clic en "Sync Now" cuando aparezca el banner
3. Espera a que Gradle termine de sincronizar

### 4. Compilar y ejecutar

```bash
./gradlew assembleDebug
```

O ejecuta directamente desde Android Studio (Shift + F10).

## Funcionalidades Implementadas

### ✅ Autenticación
- Login con email y contraseña
- Registro de nuevos usuarios
- Almacenamiento seguro del token con DataStore
- Auto-login si el usuario ya tiene sesión activa

### ✅ Dashboard
- Vista principal con acceso rápido a:
  - Productos
  - Pedidos
  - Clientes
  - Stock

### ✅ Productos
- Listado de productos con paginación
- Búsqueda de productos
- Vista de detalles (placeholder)
- Creación de productos (placeholder)
- Indicador visual para stock bajo

### 🚧 Por Implementar

#### Productos
- Formulario completo de creación/edición
- Vista detallada con toda la información
- Actualización de stock
- Eliminación de productos
- Carga y visualización de imágenes

#### Pedidos
- Listado de pedidos
- Creación de pedidos
- Vista de detalle
- Agregar/quitar productos del pedido
- Finalizar pedido
- Cancelar pedido

#### Clientes
- Listado de clientes
- Búsqueda de clientes
- Creación y edición de clientes
- Vista de detalle con historial de pedidos

#### Perfil
- Mostrar información del usuario
- Editar perfil
- Cerrar sesión
- Cerrar todas las sesiones

#### Stock
- Vista de inventario
- Productos con stock bajo
- Productos sin stock
- Historial de ajustes de stock
- Resumen del inventario

## Endpoints de la API

La aplicación consume la API REST de Rellenito Alfajores. Ver documentación completa en:
- [API_DOCUMENTATION.md](/home/leandro/rellenito-alfajores/API_DOCUMENTATION.md)

### Principales endpoints:

**Autenticación**
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/logout`
- `GET /api/v1/auth/me`

**Productos**
- `GET /api/v1/products`
- `GET /api/v1/products/{id}`
- `POST /api/v1/products`
- `PUT /api/v1/products/{id}`
- `DELETE /api/v1/products/{id}`

**Pedidos**
- `GET /api/v1/orders`
- `POST /api/v1/orders`
- `POST /api/v1/orders/{id}/finalize`

**Clientes**
- `GET /api/v1/clients`
- `POST /api/v1/clients`

## Problemas Comunes

### Error de conexión
- Verifica que el servidor Laravel esté corriendo
- Si usas emulador, usa `10.0.2.2` en lugar de `localhost`
- Si usas dispositivo físico, usa la IP local de tu computadora
- Verifica que el firewall no esté bloqueando el puerto 8000

### Error 401 Unauthorized
- El token ha expirado o es inválido
- Cierra sesión y vuelve a iniciar sesión

### Error de compilación
- Limpia el proyecto: `./gradlew clean`
- Sincroniza Gradle: File > Sync Project with Gradle Files
- Invalida caché: File > Invalidate Caches / Restart

## Próximos Pasos

1. **Completar funcionalidades pendientes**: Implementar formularios completos, vistas de detalle y operaciones CRUD
2. **Mejoras de UX**:
   - Pull-to-refresh en listas
   - Animaciones de transición
   - Estados de carga más informativos
3. **Manejo de errores mejorado**: Toast messages, Snackbars
4. **Imágenes**: Implementar carga y visualización de imágenes de productos
5. **Notificaciones**: Integrar notificaciones push con Pusher
6. **Modo offline**: Cache local con Room Database
7. **Tests**: Tests unitarios y de integración

## Licencia

Proyecto privado para Rellenito Alfajores.
