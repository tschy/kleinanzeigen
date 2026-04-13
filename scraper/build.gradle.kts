plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("application")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.5")
    }
}

repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":shared"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("org.jsoup:jsoup:1.22.1")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // ... your testcontainers deps
}

application {
    mainClass.set("classifiedslifecycle.ScraperApplicationKt")
}