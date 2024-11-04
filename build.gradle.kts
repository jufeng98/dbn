import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.dbn"
version = "3.4.4179.0"

repositories {
    maven { url = URI("https://maven.aliyun.com/nexus/content/groups/public/") }
    mavenCentral()
}

sourceSets["main"].java.srcDirs("src/main/gen")

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

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
                "**/*.template"
            )
        }
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.1.2")
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
            "Gradle-Version" to "7.6"
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
    from(zipTree("$buildDir/distributions/DataBaseManager-${project.version}.zip"))
    into("$buildDir/distributions")

    dependsOn(tasks.buildPlugin)
}

tasks.register<Copy>("buildPluginAndUnzip") {
    from("$buildDir/libs")
    include("instrumented-DataBaseManager-${project.version}-sources.jar")
    into("$buildDir/distributions/DataBaseManager/lib")

    dependsOn(tasks["unZip"])
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }


    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    prepareSandbox {
        copy {
            from("lib/ext")
            include("**/*.jar")
            into(layout.buildDirectory.dir("idea-sandbox/plugins/${project.name}/lib/ext"))
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
        sinceBuild.set("221")
        untilBuild.set("241.*")
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
