plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "study"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

//lombok 라이브러리 추가 시작
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
//lombok 라이브러리 추가 끝

repositories {
	mavenCentral()
}

dependencies {
    // Web & SSR(Thymeleaf)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    //dev-tools
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Validation (요청 파라미터 검증용)
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Persistence: JDBC
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // DB 드라이버
    implementation("com.mysql:mysql-connector-j")

    // Provider 의존성 추가
    implementation("jakarta.inject:jakarta.inject-api")

    //test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
