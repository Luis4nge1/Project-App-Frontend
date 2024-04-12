import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.optic.deliveryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.optic.deliveryapp"
        minSdk = 23
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.github.tommasoberlose:progress-dialog:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.4.0")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.github.malikdawar:drawroute:1.5")
    implementation("com.github.nkzawa:socket.io-client:0.6.0")
    implementation("io.stormotion:creditcardflow:1.0.0")

    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")

    implementation ("com.google.maps.android:maps-ktx:3.2.0")
    implementation ("com.google.maps.android:maps-utils-ktx:3.2.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.1")
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.google.maps.android:android-maps-utils:2.2.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}