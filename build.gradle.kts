plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ms.pp"
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
    implementation("org.springframework.boot:spring-boot-starter")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("me.paulschwarz:spring-dotenv:4.0.0")
    runtimeOnly("mysql:mysql-connector-java:8.0.33")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
