# Sistema de Contenido Audiovisual

Proyecto de Programación Orientada a Objetos (Universidad Politécnica Salesiana).
Catálogo en consola de películas, series de TV, documentales, podcasts y programas de radio,
con persistencia en CSV, código limpio, principios SOLID, patrón MVC y pruebas JUnit.

## Requisitos

- Java 17 o superior (JDK).
- Las librerías de pruebas (JUnit 4.13.2 y Hamcrest 1.3) ya están en la carpeta `lib/`.
  No se necesita Maven ni Gradle.

## Cómo clonar y ejecutar

```bash
git clone <URL-DEL-REPOSITORIO>
cd ContenidoAudiovisual
./ejecutar.sh
```

`ejecutar.sh` compila `src/` en la carpeta `out/` y arranca el programa. Sin el script:

```bash
mkdir -p out/main
javac -encoding UTF-8 -d out/main $(find src -name '*.java')
java -cp out/main edu.ups.poo.audiovisual.MainAudiovisual
```

En Windows (PowerShell):

```powershell
mkdir out\main -Force
javac -encoding UTF-8 -d out\main (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out\main edu.ups.poo.audiovisual.MainAudiovisual
```

El programa lee `data/contenidos.csv` al iniciar. Se puede indicar otro archivo como argumento:
`java -cp out/main edu.ups.poo.audiovisual.MainAudiovisual otra/ruta.csv`.

**NetBeans / Eclipse / IntelliJ:** abrir la carpeta como proyecto con `src` como carpeta de fuentes,
`test` como carpeta de pruebas, y agregar los dos `.jar` de `lib/` a las librerías.
Clase principal: `edu.ups.poo.audiovisual.MainAudiovisual`.

## Cómo ejecutar las pruebas

```bash
./compilar_y_probar.sh
```

Compila el código y las pruebas, y ejecuta las 110 pruebas con JUnit. Resultado esperado: `OK (110 tests)`.

Desde un IDE: clic derecho sobre la carpeta `test` y "Run tests".

## Estructura

```
ContenidoAudiovisual/
├── src/edu/ups/poo/audiovisual/
│   ├── MainAudiovisual.java        Punto de entrada (arma las piezas)
│   ├── modelo/                     MODELO: clases de negocio
│   ├── vista/                      VISTA: interfaz de consola
│   ├── controlador/                CONTROLADOR: lógica de la aplicación
│   └── persistencia/               Lectura y escritura de CSV
├── test/                           Pruebas JUnit (misma estructura de paquetes)
├── data/contenidos.csv             Datos iniciales
├── docs/                           Diagramas de clases (.png, .svg y fuente .dot)
└── lib/                            JUnit y Hamcrest
```

### Menú de la aplicación

Listar, agregar contenido (los cinco tipos), buscar por título, eliminar por ID, agregar actor a una
película, agregar temporada a una serie, agregar investigador a un documental, guardar en archivo y cargar
desde archivo. La opción `0` sale o cancela.

Los ID se asignan al crear cada objeto durante la ejecución; no se guardan en el CSV.

## Formato del archivo CSV

Una línea por contenido, campos separados por `;`. Las líneas vacías y las que empiezan con `#` se ignoran.

```
TIPO;titulo;duracionEnMinutos;genero;campos específicos
PELICULA;El Origen;148;Ciencia Ficción;Christopher Nolan;Leonardo DiCaprio:Estadounidense:1974|Joseph Gordon-Levitt:Estadounidense:1981
SERIE;Stranger Things;50;Ciencia Ficción;Duffer Bros;1:8|2:9|3:8
DOCUMENTAL;Cosmos;45;Divulgación;Astronomía;Carl Sagan:Astronomía:Universidad de Cornell
PODCAST;Mentes Curiosas;35;Educativo;Los secretos del universo;47;Spotify
RADIO;La Hora Cultural;60;Cultura;Ana Vásquez;92.5 FM;false
```

Las listas internas usan `|` entre elementos y `:` entre los datos de cada uno. Si un texto contiene
`; | :` o `\`, se escribe con `\` delante (por ejemplo `\;`). Una línea inválida produce un mensaje con su
número de línea y no detiene el programa.

## Cambios realizados

### 1. Manejo de archivos
- `RepositorioCsv` lee y escribe el catálogo completo (UTF-8).
- Al guardar se escribe primero un archivo temporal y luego reemplaza al original, así un fallo a mitad de
  escritura no corrompe los datos.
- Todos los errores de E/S o de formato se convierten en `RepositorioException` con un mensaje claro.

### 2. Refactorización y código limpio
- **Método plantilla:** `obtenerDetalles()` en `ContenidoAudiovisual` arma la descripción y cada subclase solo
  aporta sus líneas. Antes cada clase repetía los mismos `System.out.println` de título, duración y género.
- **Sin impresión en el modelo:** `mostrarDetalles()` (void) pasó a `obtenerDetalles()` (String); `compartir()` y
  `emitirEnVivo()` también devuelven texto. Solo la vista imprime.
- **Validaciones centralizadas** en `Validaciones`: ya no se aceptan títulos vacíos ni duraciones menores o
  iguales a cero.
- **Error corregido:** el `id` nunca se asignaba (el contador `contar` no se incrementaba). Ahora cada contenido
  recibe un ID único.
- `Actor`, `Temporada` e `Investigador` ahora tienen getters y validación; antes no exponían sus datos.
- Nombres claros, métodos cortos, comentarios solo donde explican el porqué.

### 3. Principios SOLID
| Principio | Dónde se aplica |
|---|---|
| **SRP** | `CatalogoContenido` (colección), `RepositorioCsv` (archivos), `FormatoCsv` (formato), `VistaConsola` (E/S), `ControladorContenido` (flujo). |
| **OCP** | Un tipo nuevo se agrega con un `ConversorContenido` y una `FabricaContenido`, sin modificar `RepositorioCsv` ni el controlador. |
| **LSP** | Todas las subclases se usan como `ContenidoAudiovisual` sin cambiar el comportamiento (hay una prueba de ello). |
| **ISP** | `Compartible`, `Transmisible`, `LectorContenido` y `EscritorContenido` son interfaces pequeñas. |
| **DIP** | El controlador depende de `Vista` y `RepositorioContenido`; las implementaciones se inyectan desde `MainAudiovisual`. |

### 4. Patrón MVC
- **Modelo:** `modelo/` (sin dependencias de consola ni archivos).
- **Vista:** `Vista` (interfaz) y `VistaConsola`.
- **Controlador:** `ControladorContenido`, `AccionMenu` y las fábricas de contenido.

### 5. Pruebas unitarias
110 pruebas JUnit 4 en `test/`: validaciones y casos límite del modelo, ida y vuelta del CSV con todos los
tipos, caracteres especiales, archivos inexistentes o mal formados, entrada inválida en la consola y el flujo
completo del controlador (con un repositorio en memoria).

## Diagramas de clases

- `docs/diagrama_clases_modelo.png`: clases del modelo.
- `docs/diagrama_mvc.png`: capas MVC y sus dependencias.
- `docs/diagrama_persistencia.png`: persistencia en CSV.
- `docs/Audiovisual_original.uxf`: diagrama anterior (UMLet), conservado como referencia.

Los `.dot` son el código fuente de los diagramas (se regeneran con `dot -Tpng archivo.dot -o archivo.png`).
