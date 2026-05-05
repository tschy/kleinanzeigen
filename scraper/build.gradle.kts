plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("application")
    id("com.google.cloud.tools.jib")
    id("org.flywaydb.flyway")
}

jib {
    to {
        auth {
            username = System.getenv("GITHUB_ACTOR") ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: ""
        }
    }
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
   implementation("com.squareup.okhttp3:logging-interceptor:5.3.0")
    implementation("org.flywaydb:flyway-core:11.8.2")
    implementation("org.flywaydb:flyway-database-postgresql:11.8.2")
    implementation("org.postgresql:postgresql:42.7.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.14.9")
}

application {
    mainClass.set("classifiedslifecycle.ScraperApplicationKt")
}

tasks.named<JavaExec>("bootRun") {
    workingDir = rootProject.projectDir
}

flyway {
    url = "jdbc:postgresql://localhost:5432/kleinanzeigen"
    user = "postgres"
    password = "fennpfuhl"
    schemas = arrayOf("public")
    configurations = arrayOf("runtimeClasspath")
}