plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.10"
    application
    id("org.flywaydb.flyway") version "12.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:12.1.0")
    implementation("org.flywaydb:flyway-database-postgresql:12.1.0")
//    implementation("org.postgresql:postgresql:42.7.3")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.27.7")
}

flyway {
    url      = "jdbc:postgresql://localhost:5432"
    user     = "postgres"
    schemas  = arrayOf("public")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("ebuparser.TestAwsKt")
}

