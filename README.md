App Distribuidora de Alimentos
📌 Descripción

Aplicación móvil para la gestión de compras con despacho a domicilio, desarrollada en Android Studio y conectada a Firebase (Authentication y Realtime Database).
Incluye autenticación con correo electrónico, cálculo automático de despacho y monitoreo de cadena de frío en camiones de reparto.

🚀 Reglas de negocio

Compras ≥ $50.000 → despacho gratis dentro de 20 km.

Compras entre $25.000 y $49.999 → $150 por km recorrido.

Compras < $25.000 → $300 por km recorrido.

👥 Historias de Usuario

Cliente: Como cliente quiero registrarme con correo para poder acceder a la aplicación.

Cliente: Como cliente quiero calcular el costo de despacho según mi compra para saber cuánto debo pagar.

Administrador: Como administrador quiero visualizar la temperatura del congelador para evitar pérdidas en productos congelados.

Sistema: Como sistema quiero almacenar la ubicación GPS del cliente después del login para la gestión del despacho.

🛠️ Tecnologías

Android Studio (Java)

Firebase Authentication

Firebase Realtime Database

GitHub (gestión del proyecto, issues, tareas, commits)

📱 Requisitos de instalación

Android Lollipop (5.0) en adelante

Configuración en emulador o dispositivo físico

Conexión a internet

Configurar Firebase en el proyecto:

Activar Authentication (Correo/Contraseña).

Configurar Realtime Database.

Descargar google-services.json y colocar en app/.

Añadir dependencias en build.gradle:

implementation platform('com.google.firebase:firebase-bom:33.3.0')
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-database'

🚀 Uso

Abrir la app.

Registrar un usuario nuevo o iniciar sesión.

Acceder al menú y visualizar la ubicación GPS, que se guarda automáticamente en Firebase.

Próximamente, calcular despacho y monitorear temperatura de congelador.

👨‍💻 Equipo

Hugo: Login y Firebase Authentication

Integrante 2: Base de datos y reglas de negocio

Integrante 3: GPS y alarmas de temperatura

📚 Conclusión

El proyecto permitió aprender a integrar Firebase con Android, gestionar datos en tiempo real, obtener ubicación GPS, manejar login seguro y trabajar de manera colaborativa mediante GitHub.
