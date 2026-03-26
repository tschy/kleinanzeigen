buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:12.1.0")
        classpath("org.postgresql:postgresql:42.7.3")
    }
}

plugins {
    kotlin("jvm") version "2.2.21"
    // kotlin("plugin.spring") version "2.2.21"
    // id("org.springframework.boot") version "4.0.3"
    // id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "12.1.0"
    id("application")
}

group = "io.github.mateyjack"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // implementation("org.springframework.boot:spring-boot-starter")
    // implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.flywaydb:flyway-core:12.1.0")
    implementation("org.flywaydb:flyway-database-postgresql:12.1.0")
    implementation("org.postgresql:postgresql:42.7.3")
    // testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jsoup:jsoup:1.22.1")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
}

flyway {
    url      = "jdbc:postgresql://localhost:5432/kleinanzeigen"
    user     = "postgres"
    password = "fennpfuhl"
    schemas  = arrayOf("public")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


  application {
      mainClass.set("classifieds_lifecycle.classifiedsLifecycleApplication")
  }
