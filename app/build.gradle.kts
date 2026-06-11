plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase)
}

android {
    namespace = "com.example.plantas"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.plantas"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core Android y UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Navegación
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Animaciones y Listas
    implementation(libs.lottie)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Firebase
    implementation(platform(libs.firebase.bom))
    // CORRECCIÓN: Quitamos firebase.analytics porque tu profe no lo declaró en su TOML,
    // y dejamos Firestore y Auth que son los que usas para el Login y tus datos.
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    // Corrutinas (Requeridas por tu nuevo TOML)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Conexión a la API (Retrofit)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging) // CORRECCIÓN: Cambiado 'loggin' por 'logging' (con G al final)

    // Imagenes (Glide)
    implementation(libs.glide)
    implementation(libs.androidx.recyclerview)

    // Pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Para el RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Para el AppCompat y SearchView
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Para el ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Room Database
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
}