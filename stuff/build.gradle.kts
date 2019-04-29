import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.21"
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // logging
    implementation("io.github.microutils:kotlin-logging:1.6.+")
    implementation("net.logstash.logback:logstash-logback-encoder:5.+")
    val logbackJsonVersion = "0.1.5"
    implementation("ch.qos.logback.contrib:logback-json-classic:$logbackJsonVersion")
    implementation("ch.qos.logback.contrib:logback-jackson:$logbackJsonVersion")

    // serialization: jackson json
    val jacksonVersion =  "2.9.8"
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-modules-java8:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    // jmespath ... you know "jq" ;)
    implementation("io.burt:jmespath-jackson:0.2.1")
    // fp
    implementation("org.funktionale:funktionale-all:1.2")
    val arrow_version = "0.9.0"
    implementation("io.arrow-kt:arrow-core-data:$arrow_version")
    implementation("io.arrow-kt:arrow-core-extensions:$arrow_version")
    implementation("io.arrow-kt:arrow-syntax:$arrow_version")
    implementation("io.arrow-kt:arrow-typeclasses:$arrow_version")
    implementation("io.arrow-kt:arrow-extras-data:$arrow_version")
    implementation("io.arrow-kt:arrow-extras-extensions:$arrow_version")
    //kapt("io.arrow-kt:arrow-meta:$arrow_version")



    // test: junit5
    val junitVersion = "5.3.1"
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    // test: kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.amshove.kluent:kluent:1.49")
    testImplementation("io.mockk:mockk:1.9.+")
    testImplementation("dev.minutest:minutest:1.4.+")

    /*
testCompile("org.mockito:mockito-core:2.23.4") {
    isForce = true
    because("version that is enforced by Spring Boot is not compatible with Java 11")
}
testCompile("net.bytebuddy:byte-buddy:1.9.3") {
    isForce = true
    because("version that is enforced by Spring Boot is not compatible with Java 11")
}
 */
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
