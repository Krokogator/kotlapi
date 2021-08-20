import org.gradle.kotlin.dsl.execution.ProgramText.Companion.from
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
	id("scala")
	id("idea")
}

idea {
	module {
		isDownloadJavadoc = true
		isDownloadSources = true
	}
}

group = "com.krokogator"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

sourceSets {
	getByName("test").scala.srcDirs("src/test/scala")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.5.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.6.1")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

task<JavaExec>("loadTest") {
	description = "Load Test With Gatling"
	group = "Load Test"
	classpath = sourceSets["test"].runtimeClasspath
	jvmArgs = listOf(
		"-Dgatling.core.directory.binaries=${sourceSets["test"].output.classesDirs.toString()}"
	)
	main = "io.gatling.app.Gatling"
	args = listOf(
		"--simulation", "com.krokogator.kotlapi.BasicSimulation",
		"--results-folder", "${buildDir}/gatling-results",
		"--binaries-folder", sourceSets["test"].output.classesDirs.toString(),
		"--bodies-folder", sourceSets["test"].resources.srcDirs.toList().first().toString() + "/gatling/bodies",
	)
}
