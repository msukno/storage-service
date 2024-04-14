plugins {
	java
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.springboot.podkaster"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	//implementation("org.springframework.boot:spring-boot-starter-actuator")
	//implementation("org.springframework.boot:spring-boot-starter-security")

	//aws s3
	implementation(platform("software.amazon.awssdk:bom:2.21.1"))
	implementation("software.amazon.awssdk:s3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Added dependencies
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
