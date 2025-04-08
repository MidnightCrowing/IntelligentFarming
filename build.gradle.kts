plugins {
    kotlin("jvm") version "2.1.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.midnightcrowing"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))

    implementation(platform("org.lwjgl:lwjgl-bom:3.3.6"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-bgfx")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-vulkan")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-bgfx", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-nanovg", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-nuklear", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-par", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = "natives-windows")

    implementation("com.googlecode.soundlibs:vorbisspi:1.0.3.3")
    implementation("com.googlecode.soundlibs:tritonus-share:0.3.7.4")

    testImplementation(kotlin("test"))
}

val generateVersionFile by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/resources/version").get().asFile
    val versionFile = outputDir.resolve("version.properties")

    inputs.property("version", project.version)
    outputs.file(versionFile)

    doLast {
        outputDir.mkdirs()
        versionFile.writeText("version=${project.version}")
    }
}

sourceSets.main {
    resources.srcDir("build/generated/resources/version")
}

tasks.processResources {
    dependsOn(generateVersionFile)

    from("LICENSE") {
        into(".") // 表示拷贝到资源根目录
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.midnightcrowing.MainKt"
        )
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        attributes(
            "Main-Class" to "com.midnightcrowing.MainKt"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}