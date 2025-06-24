# Car2Go Profile Service - Microservice Migration Guide ✅ COMPLETADO

## 🎉 Estado de la Migración: EXITOSA

✅ **Compilación**: Sin errores  
✅ **Tests**: Todos los tests pasan  
✅ **Dependencias IAM**: Completamente eliminadas  
✅ **Autenticación JWT**: Funcionando correctamente  

## Resumen de Cambios

Este proyecto ha sido **migrado exitosamente** de una arquitectura monolítica a microservicios. Los principales cambios realizados fueron:

### 1. ✅ Eliminación de Dependencias IAM Monolíticas

- **Antes**: El código dependía directamente de `UserDetailsImpl` del paquete IAM monolítico
- **Después**: Se creó una abstracción propia para manejar usuarios autenticados

### 2. ✅ Nueva Arquitectura de Autenticación

#### AuthenticatedUser
Clase que representa un usuario autenticado en el contexto del microservicio:
```java
com.pe.platform.shared.infrastructure.security.model.AuthenticatedUser
```

#### JwtAuthenticationFilter Mejorado
- Extrae información del token JWT incluyendo roles/authorities
- Crea un `AuthenticatedUser` en lugar de depender de clases externas
- Maneja diferentes formatos de claims de autoridades
- Marcado como `@Component` para inyección de dependencias

#### AuthenticationUtils
Clase utilitaria para simplificar el acceso a información del usuario autenticado:
```java
com.pe.platform.shared.infrastructure.security.utils.AuthenticationUtils
```

### 3. ✅ Cambios en Servicios y Controladores

#### ProfileCommandServiceImpl
- **Completamente migrado**: Reemplazado `UserDetailsImpl` con `AuthenticatedUser`
- Uso de `AuthenticationUtils.getCurrentUserId()` para simplificar el código
- Eliminadas todas las referencias al IAM monolítico

#### ProfilesController
- **Completamente migrado**: Mismo cambio de `UserDetailsImpl` a `AuthenticatedUser`
- Código más limpio usando `AuthenticationUtils`
- Imports actualizados

### 4. ✅ Configuración de Auditoría JPA

Se agregó configuración para habilitar auditoría automática:
```java
com.pe.platform.shared.infrastructure.persistence.jpa.configuration.JpaAuditingConfiguration
```

## ✅ Compatibilidad con Microservicio IAM

### Formato de Token JWT Esperado

El servicio ahora espera tokens JWT con la siguiente estructura:

```json
{
  "userId": 12345,
  "sub": "username", // Opcional
  "roles": ["ROLE_SELLER", "ROLE_BUYER"], // O authorities/scope
  "iat": 1640995200,
  "exp": 1641081600
}
```

### Claims de Autoridades

El filtro JWT busca authorities en los siguientes claims (en orden):
1. `roles`
2. `authorities` 
3. `scope`

Puede ser un array de strings o un string separado por comas.

## Migración desde IAM Monolítico

### Pasos para Integrar con Microservicio IAM

1. **Configurar Gateway/Proxy**: El microservicio IAM debe generar tokens JWT con los claims apropiados
2. **Sincronizar Secret JWT**: Usar el mismo secret entre IAM y Profile services
3. **Formato de Claims**: Asegurar que el IAM incluya `userId` y roles en el token

### Ejemplo de Generación de Token en IAM Service

```java
// En el microservicio IAM
Claims claims = Jwts.claims().subject(user.getUsername());
claims.put("userId", user.getId());
claims.put("roles", user.getRoles().stream()
    .map(Role::getName)
    .collect(Collectors.toList()));

String token = Jwts.builder()
    .setClaims(claims)
    .setIssuedAt(new Date())
    .setExpiration(new Date(System.currentTimeMillis() + expiration))
    .signWith(key)
    .compact();
```

## Configuración

### application.properties

```properties
# JWT Configuration (debe coincidir con el IAM service)
authorization.jwt.secret=WriteHereYourSecretStringForTokenSigningCredentials
authorization.jwt.expiration.days=7

# Eureka Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.prefer-ip-address=true
```

## Beneficios de la Migración

1. **Desacoplamiento**: El service profile ya no depende directamente del IAM
2. **Escalabilidad**: Cada microservicio puede escalarse independientemente
3. **Mantenimiento**: Código más limpio y fácil de mantener
4. **Flexibilidad**: Fácil integración con diferentes proveedores de autenticación

## Testing

Para probar el servicio, asegúrate de incluir un token JWT válido en el header:

```bash
curl -H "Authorization: Bearer <JWT_TOKEN>" \
     http://localhost:8080/api/v1/profiles/me
```

## Notas Importantes

- Los tokens JWT deben incluir el claim `userId` para identificar al usuario
- Las autoridades/roles se extraen automáticamente del token
- El servicio es compatible con tokens generados por cualquier microservicio IAM que siga el formato establecido
