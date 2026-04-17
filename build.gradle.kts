buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        // By putting these here, they are available to ALL modules
        // and the Flyway Gradle task itself.
        classpath("org.flywaydb:flyway-database-postgresql:11.8.2")
        classpath("org.postgresql:postgresql:42.7.3")
    }
}


plugins {
    kotlin("jvm") version "2.1.20" apply false
    kotlin("plugin.spring") version "2.1.20" apply false
    kotlin("plugin.jpa") version "2.1.20" apply false
    id("org.springframework.boot") version "3.4.5" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.flywaydb.flyway") version "11.8.2" apply false
    id("com.google.cloud.tools.jib") version "3.5.3" apply false
}

// Java toolchain applied to all submodules
subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
        }
        jvmToolchain(21)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}