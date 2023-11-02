import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "diego09310"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
//	implementation("org.springframework.boot:spring-boot-starter-security")
	// TODO: Add only if easier to configure timeout
//	implementation("org.springframework.session:spring-session-core:3.1.2")
	implementation("org.webjars:bootstrap:5.3.2")
	implementation("org.webjars:popper.js:2.11.7")
	implementation("org.webjars.npm:bootstrap-icons:1.11.1")
	implementation("org.webjars:stomp-websocket:2.3.4")
	implementation("org.webjars:webjars-locator:0.47")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
	implementation("se.michaelthelin.spotify:spotify-web-api-java:8.3.0")
	implementation("com.github.kenglxn.QRGen:javase:3.0.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
