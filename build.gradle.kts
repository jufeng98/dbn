import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "com.dbn"
version = "3.4.5203.0"

repositories {
    maven { url = URI("https://maven.aliyun.com/nexus/content/groups/public/") }
    mavenCentral()
}

sourceSets["main"].java.srcDirs("src/main/gen")

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("junit:junit:4.13.1")

    implementation("org.projectlombok:lombok:1.18.30")
    implementation("com.github.mwiede:jsch:0.2.17")
    implementation("net.sf.trove4j:trove4j:3.0.3")

    // poi libraries (xls export)
    implementation("org.apache.poi:poi:4.1.2")
    implementation("org.apache.poi:poi-ooxml:4.1.2")
    implementation("org.apache.poi:poi-ooxml-schemas:4.1.2")

    // poi library dependencies
    implementation("org.apache.commons:commons-io:1.3.2")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.logging.log4j:log4j-api:2.23.0")
    implementation("org.apache.xmlbeans:xmlbeans:3.1.0")
    implementation("org.mybatis.generator:mybatis-generator-core:1.4.2")
    implementation("com.github.spullara.mustache.java:compiler:0.9.6")
}

sourceSets {
    main {
        resources {
            srcDir("src/main/java")
            include("**/*.properties")
            include("**/*.xml")
            include("**/*.gif")
            include("**/*.png")
            include("**/*.svg")
            include("**/*.jpeg")
            include("**/*.jpg")
            include("**/*.html")
            include("**/*.dtd")
            include("**/*.tld")
            include("**/*.txt")
            include("**/*.jar")
            include("**/*.css")
            include("**/*.mustache")
            include("**/*.template")
        }
        resources {
            include(
                "**/*.properties",
                "**/*.xml",
                "**/*.gif",
                "**/*.png",
                "**/*.svg",
                "**/*.jpeg",
                "**/*.jpg",
                "**/*.html",
                "**/*.dtd",
                "**/*.tld",
                "**/*.txt",
                "**/*.jar",
                "**/*.css",
                "**/*.mustache",
                "**/*.template",
                "**/*.java",
            )
        }
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("java", "copyright"))
}

tasks.register<Jar>("packageSource") {
    archiveFileName.set("instrumented-${project.name}-${project.version}-sources.jar")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))

    from("src/main/java") {
        include("**/*.java")
    }

    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Gradle-Version" to "8.5"
        )
    }
}

tasks.getByName("processResources") {
    doLast {
        copy {
            from("lib/ext")
            include("**/*.jar")
            into(layout.buildDirectory.dir("idea-sandbox/plugins/${project.name}/lib/ext"))
        }
    }
}

tasks.register<Copy>("unZip") {
    from(zipTree("${layout.buildDirectory}/distributions/DataBaseManager-${project.version}.zip"))
    into("${layout.buildDirectory}/distributions")

    dependsOn(tasks.buildPlugin)
}

tasks.register<Copy>("buildPluginAndUnzip") {
    from("${layout.buildDirectory}/libs")
    include("instrumented-DataBaseManager-${project.version}-sources.jar")
    into("${layout.buildDirectory}/distributions/DataBaseManager/lib")

    dependsOn(tasks["unZip"])
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }


    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    prepareSandbox {
        doLast {
            copyLib()
        }
    }

    jar {
        // kt文件不知道被哪个配置影响导致被编译了两次,所以这里暂时配置下
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    buildPlugin {
        dependsOn("packageSource")
    }

    patchPluginXml {
        sinceBuild.set("243")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runIde {
        systemProperties["idea.auto.reload.plugins"] = true
        jvmArgs = listOf(
            "-Xms1024m",
            "-Xmx2048m",
            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1044",
        )
    }
}

fun copyLib() {
    val bundlesDir = File(project.projectDir, "/lib/ext")
    val targetDir = File(project.projectDir, "/build/idea-sandbox/plugins/${project.name}/lib/ext")
    copyDirectory(bundlesDir.toPath(), targetDir.toPath())
}

fun copyDirectory(sourceDir: Path, destDir: Path) {
    // 确保目标目录存在
    Files.createDirectories(destDir)

    // 获取源目录的所有文件和子目录
    for (path in Files.newDirectoryStream(sourceDir)) {
        // 获取相对路径
        val relativePath: Path = sourceDir.relativize(path)
        // 目标路径
        val destPath: Path = destDir.resolve(relativePath)
        // 如果是文件，则复制文件
        if (Files.isRegularFile(path)) {
            Files.copy(path, destPath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            // 如果是目录，则递归调用
            copyDirectory(path, destPath)
        }
    }
}