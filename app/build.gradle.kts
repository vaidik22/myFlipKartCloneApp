plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.flicpcartClone"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.flicpcartClone"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.shreyaspatil:EasyUpiPayment:2.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.github.smarteist:autoimageslider:1.4.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.cepheuen.elegant-number-button:lib:1.0.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}