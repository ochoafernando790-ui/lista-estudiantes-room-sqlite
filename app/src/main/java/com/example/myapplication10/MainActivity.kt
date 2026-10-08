    package com.example.myapplication10

    import android.graphics.Bitmap
    import android.net.Uri
    import android.os.Bundle
    import android.provider.MediaStore
    import android.util.Patterns
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.activity.result.PickVisualMediaRequest
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.animation.AnimatedVisibility
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ArrowBack
    import androidx.compose.material.icons.automirrored.filled.Sort
    import androidx.compose.material.icons.filled.Add
    import androidx.compose.material.icons.filled.Clear
    import androidx.compose.material.icons.filled.Delete
    import androidx.compose.material.icons.filled.DeleteSweep
    import androidx.compose.material.icons.filled.Edit
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.DropdownMenuItem
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.ExposedDropdownMenuBox
    import androidx.compose.material3.ExposedDropdownMenuDefaults
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.OutlinedButton
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.RadioButton
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.material3.TopAppBar
    import androidx.compose.material3.darkColorScheme
    import androidx.compose.material3.lightColorScheme
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavType
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import androidx.navigation.navArgument
    import coil.compose.rememberAsyncImagePainter
    import com.example.myapplication10.data.AppDatabase
    import com.example.myapplication10.data.Estudiante
    import kotlinx.coroutines.launch
    import java.io.ByteArrayOutputStream
    import java.util.UUID

    // =============================================================
    // 1. MODELO DE DATOS (ver com.example.myapplication10.data.Estudiante,
    //    anotado con @Entity para persistirse en SQLite a través de Room)
    // =============================================================
    val opcionesSexo = listOf("Masculino", "Femenino")
    val opcionesCarrera = listOf(
        "Ingeniería en Sistemas",
        "Ingeniería Industrial",
        "Licenciatura en Computación",
        "Licenciatura en Administración",
        "Diseño Gráfico"
    )

    // Datos de ejemplo: solo se insertan en la base de datos la primera vez
    // que se abre la app (cuando la tabla todavía está vacía).
    val estudiantesDeEjemplo = listOf(
        Estudiante("1", "Carlos Mendoza", "20", "Masculino", "Ingeniería en Sistemas", "78901234", "carlos.mendoza@estudiante.edu.sv", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400"),
        Estudiante("2", "Ana Lucía Torres", "22", "Femenino", "Licenciatura en Computación", "71234567", "ana.torres@estudiante.edu.sv", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400"),
        Estudiante("3", "Roberto Gómez", "21", "Masculino", "Ingeniería Industrial", "75678901", "roberto.gomez@estudiante.edu.sv", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400"),
        Estudiante("4", "Sofía Hernández", "19", "Femenino", "Diseño Gráfico", "73456789", "sofia.hernandez@estudiante.edu.sv", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400"),
        Estudiante("5", "Diego Ramos", "23", "Masculino", "Licenciatura en Administración", "79012345", "diego.ramos@estudiante.edu.sv", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400"),
        Estudiante("6", "Valeria Castro", "20", "Femenino", "Ingeniería en Sistemas", "72345678", "valeria.castro@estudiante.edu.sv", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400"),
        Estudiante("7", "Fernando López", "24", "Masculino", "Ingeniería Industrial", "76789012", "fernando.lopez@estudiante.edu.sv", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=400"),
        Estudiante("8", "Gabriela Morales", "21", "Femenino", "Diseño Gráfico", "74567890", "gabriela.morales@estudiante.edu.sv", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400"),
        Estudiante("9", "Jorge Martínez", "22", "Masculino", "Licenciatura en Computación", "70123456", "jorge.martinez@estudiante.edu.sv", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400"),
        Estudiante("10", "Mariana Aguilar", "20", "Femenino", "Licenciatura en Administración", "77890123", "mariana.aguilar@estudiante.edu.sv", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400")
    )

    // =============================================================
    // 2. ACTIVIDAD PRINCIPAL
    // =============================================================
    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                val esTemaOscuro = false
                val esquemaColores = if (esTemaOscuro) darkColorScheme() else lightColorScheme()

                MaterialTheme(colorScheme = esquemaColores) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        GestionEstudiantesApp(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    // =============================================================
    // 3. NAVEGACIÓN Y ESTADO GENERAL
    // =============================================================
    @Composable
    fun GestionEstudiantesApp(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        val context = LocalContext.current
        val dao = remember { AppDatabase.getDatabase(context).estudianteDao() }
        val scope = rememberCoroutineScope()

        // Siembra la base de datos con los estudiantes de ejemplo solo
        // la primera vez que se instala la app (tabla vacía).
        LaunchedEffect(Unit) {
            if (dao.contarEstudiantes() == 0) {
                dao.insertarTodos(estudiantesDeEjemplo)
            }
        }

        val listaEstudiantes by dao.obtenerTodos().collectAsState(initial = emptyList())

        NavHost(
            navController = navController,
            startDestination = "pantalla_lista",
            modifier = modifier
        ) {
            composable("pantalla_lista") {
                PantallaListaEstudiantes(
                    lista = listaEstudiantes,
                    onEstudianteClick = { id -> navController.navigate("pantalla_detalle/$id") },
                    onAgregarClick = { navController.navigate("pantalla_formulario/nuevo/-1") },
                    onBorrarTodos = { scope.launch { dao.borrarTodos() } }
                )
            }

            composable(
                route = "pantalla_detalle/{estudianteId}",
                arguments = listOf(navArgument("estudianteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("estudianteId") ?: ""
                val estudiante = listaEstudiantes.find { it.id == id }

                if (estudiante != null) {
                    PantallaDetalleEstudiante(
                        estudiante = estudiante,
                        onVolver = { navController.popBackStack() },
                        onEditar = { navController.navigate("pantalla_formulario/editar/${estudiante.id}") },
                        onEliminarConfirmado = {
                            scope.launch { dao.eliminar(estudiante) }
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(
                route = "pantalla_formulario/{modo}/{estudianteId}",
                arguments = listOf(
                    navArgument("modo") { type = NavType.StringType },
                    navArgument("estudianteId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val modo = backStackEntry.arguments?.getString("modo") ?: "nuevo"
                val id = backStackEntry.arguments?.getString("estudianteId") ?: "-1"
                val estudianteExistente = listaEstudiantes.find { it.id == id }

                PantallaFormularioEstudiante(
                    estudianteExistente = estudianteExistente,
                    onGuardar = { estudianteGuardado ->
                        scope.launch {
                            if (modo == "editar") {
                                dao.actualizar(estudianteGuardado)
                            } else {
                                dao.insertar(estudianteGuardado)
                            }
                        }
                        navController.popBackStack()
                    },
                    onCancelar = { navController.popBackStack() }
                )
            }
        }
    }

    // =============================================================
    // 4. PANTALLA 1: LISTA CON BÚSQUEDA, FILTROS Y ORDENAMIENTO
    // =============================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaListaEstudiantes(
        lista: List<Estudiante>,
        onEstudianteClick: (String) -> Unit,
        onAgregarClick: () -> Unit,
        onBorrarTodos: () -> Unit
    ) {
        var textoBusqueda by remember { mutableStateOf("") }
        var carreraSeleccionada by remember { mutableStateOf("Todas las carreras") }
        var ordenAscendente by remember { mutableStateOf(true) }
        var mostrarDialogoBorrarTodos by remember { mutableStateOf(false) }
        var expandedCarreraDropdown by remember { mutableStateOf(false) }

        val opcionesFiltroCarrera = listOf("Todas las carreras") + opcionesCarrera

                
        val listaFiltrada = lista.filter { estudiante ->
            val coincideNombre = estudiante.nombre.contains(textoBusqueda, ignoreCase = true)
            val coincideCarrera = carreraSeleccionada == "Todas las carreras" || estudiante.carrera == carreraSeleccionada
            coincideNombre && coincideCarrera
        }


        val listaProcesada = if (ordenAscendente) {
            listaFiltrada.sortedBy { it.nombre.lowercase() }
        } else {
            listaFiltrada.sortedByDescending { it.nombre.lowercase() }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Estudiantes (${listaProcesada.size}/${lista.size})") },
                    actions = {
                        // Botón para Borrar Todos
                        if (lista.isNotEmpty()) {
                            IconButton(onClick = { mostrarDialogoBorrarTodos = true }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = "Borrar todos",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAgregarClick) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Estudiante")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // BUSCADOR POR NOMBRE
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    label = { Text("Buscar por nombre...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (textoBusqueda.isNotEmpty()) {
                            IconButton(onClick = { textoBusqueda = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // CONTROLES DE FILTRO Y ORDEN
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Filtro por Carrera
                    ExposedDropdownMenuBox(
                        expanded = expandedCarreraDropdown,
                        onExpandedChange = { expandedCarreraDropdown = !expandedCarreraDropdown },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = carreraSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Carrera") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCarreraDropdown) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            singleLine = true
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCarreraDropdown,
                            onDismissRequest = { expandedCarreraDropdown = false }
                        ) {
                            opcionesFiltroCarrera.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        carreraSeleccionada = opcion
                                        expandedCarreraDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Botón para alternar Orden (A-Z / Z-A)
                    OutlinedButton(
                        onClick = { ordenAscendente = !ordenAscendente },
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (ordenAscendente) "A-Z" else "Z-A")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ESTADO VACÍO O LISTA DE ESTUDIANTES
                if (listaProcesada.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (lista.isEmpty()) {
                            Text(
                                text = "No hay estudiantes registrados.",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        } else {
                            Text(
                                text = "No se encontraron estudiantes con los filtros aplicados.",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(listaProcesada, key = { it.id }) { estudiante ->
                            ItemEstudianteCard(
                                estudiante = estudiante,
                                onClick = { onEstudianteClick(estudiante.id) }
                            )
                        }
                    }
                }
            }
        }

        // DIÁLOGO DE CONFIRMACIÓN PARA BORRAR TODOS
        if (mostrarDialogoBorrarTodos) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrarTodos = false },
                title = { Text("Eliminar todos los estudiantes") },
                text = { Text("¿Está seguro de que desea borrar absolutamente todos los registros? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoBorrarTodos = false
                            onBorrarTodos()
                        }
                    ) {
                        Text("Borrar Todos", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoBorrarTodos = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    @Composable
    fun ItemEstudianteCard(
        estudiante: Estudiante,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!estudiante.fotoUri.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = Uri.parse(estudiante.fotoUri)),
                        contentDescription = "Foto perfil",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = estudiante.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = estudiante.carrera,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    // =============================================================
    // 5. PANTALLA 2: DETALLE DEL ESTUDIANTE
    // =============================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaDetalleEstudiante(
        estudiante: Estudiante,
        onVolver: () -> Unit,
        onEditar: () -> Unit,
        onEliminarConfirmado: () -> Unit
    ) {
        var mostrarDialogoEliminar by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Estudiante") },
                    navigationIcon = {
                        IconButton(onClick = onVolver) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!estudiante.fotoUri.isNullOrEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(model = Uri.parse(estudiante.fotoUri)),
                                contentDescription = "Foto perfil grande",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar grande",
                                    modifier = Modifier.size(60.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = estudiante.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "🎓 Carrera: ${estudiante.carrera}", fontSize = 16.sp)
                        Text(text = "🎂 Edad: ${estudiante.edad} años", fontSize = 16.sp)
                        Text(text = "⚧ Sexo: ${estudiante.sexo}", fontSize = 16.sp)
                        Text(text = "📞 Teléfono: ${estudiante.telefono}", fontSize = 16.sp)
                        Text(text = "✉️ Correo: ${estudiante.correo}", fontSize = 16.sp)

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = onEditar) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Editar")
                            }

                            Button(
                                onClick = { mostrarDialogoEliminar = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogoEliminar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoEliminar = false },
                title = { Text("Confirmar Eliminación") },
                text = { Text("¿Está seguro de que desea eliminar a ${estudiante.nombre}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoEliminar = false
                            onEliminarConfirmado()
                        }
                    ) { Text("Eliminar", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoEliminar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    // =============================================================
    // 6. PANTALLA 3: FORMULARIO DE REGISTRO
    // =============================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaFormularioEstudiante(
        estudianteExistente: Estudiante?,
        onGuardar: (Estudiante) -> Unit,
        onCancelar: () -> Unit
    ) {
        val context = LocalContext.current

        var nombre by remember { mutableStateOf(estudianteExistente?.nombre ?: "") }
        var edad by remember { mutableStateOf(estudianteExistente?.edad ?: "") }
        var sexo by remember { mutableStateOf(estudianteExistente?.sexo ?: opcionesSexo[0]) }
        var carrera by remember { mutableStateOf(estudianteExistente?.carrera ?: opcionesCarrera[0]) }
        var telefono by remember { mutableStateOf(estudianteExistente?.telefono ?: "") }
        var correo by remember { mutableStateOf(estudianteExistente?.correo ?: "") }
        var fotoUri by remember { mutableStateOf(estudianteExistente?.fotoUri) }

        var errorNombre by remember { mutableStateOf(false) }
        var errorEdad by remember { mutableStateOf(false) }
        var errorTelefono by remember { mutableStateOf(false) }
        var errorCorreo by remember { mutableStateOf(false) }

        var expandedDropdown by remember { mutableStateOf(false) }

        val galeriaLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                fotoUri = uri.toString()
            }
        }

        val camaraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->
            if (bitmap != null) {
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    bitmap,
                    "Foto_${System.currentTimeMillis()}",
                    null
                )
                fotoUri = path
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (estudianteExistente == null) "Nuevo Estudiante" else "Editar Estudiante") },
                    navigationIcon = {
                        IconButton(onClick = onCancelar) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (!fotoUri.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = Uri.parse(fotoUri)),
                            contentDescription = "Foto elegida",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(50.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = {
                        galeriaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Text("Galería")
                    }
                    OutlinedButton(onClick = {
                        camaraLauncher.launch(null)
                    }) {
                        Text("Cámara")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; errorNombre = false },
                    label = { Text("Nombre completo") },
                    isError = errorNombre,
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(visible = errorNombre) {
                    Text("El nombre no puede estar vacío", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it; errorEdad = false },
                    label = { Text("Edad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errorEdad,
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(visible = errorEdad) {
                    Text("Ingrese una edad válida (1 a 120)", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Sexo:", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    opcionesSexo.forEach { opcion ->
                        RadioButton(
                            selected = (sexo == opcion),
                            onClick = { sexo = opcion }
                        )
                        Text(opcion, modifier = Modifier.clickable { sexo = opcion })
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = carrera,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Carrera") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        opcionesCarrera.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    carrera = opcion
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it; errorTelefono = false },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = errorTelefono,
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(visible = errorTelefono) {
                    Text("Ingrese un teléfono de al menos 8 dígitos", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it; errorCorreo = false },
                    label = { Text("Correo Electrónico") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = errorCorreo,
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(visible = errorCorreo) {
                    Text("Ingrese un correo electrónico válido", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val edadNum = edad.toIntOrNull()
                        val esNombreValido = nombre.isNotBlank()
                        val esEdadValida = edadNum != null && edadNum in 1..120
                        val esTelefonoValido = telefono.length >= 8
                        val esCorreoValido = Patterns.EMAIL_ADDRESS.matcher(correo).matches()

                        errorNombre = !esNombreValido
                        errorEdad = !esEdadValida
                        errorTelefono = !esTelefonoValido
                        errorCorreo = !esCorreoValido

                        if (esNombreValido && esEdadValida && esTelefonoValido && esCorreoValido) {
                            val nuevoEstudiante = Estudiante(
                                id = estudianteExistente?.id ?: UUID.randomUUID().toString(),
                                nombre = nombre,
                                edad = edad,
                                sexo = sexo,
                                carrera = carrera,
                                telefono = telefono,
                                correo = correo,
                                fotoUri = fotoUri
                            )
                            onGuardar(nuevoEstudiante)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Estudiante")
                }
            }
        }
    }