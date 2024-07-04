import java.net.URI

// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

sourceSets{
  main {
    resources {
      srcDir("src/main/java")
      include("**/*.xml")
      include("**/*.txt")
    }
    resources {
      include(
              "**/*.png",
              "**/*.jpg",
              "**/*.xml",
              "**/*.svg",
              "**/*.css",
              "**/*.html",
              "**/*.properties",
              "**/*.template")
    }
  }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2022.1.2")
  type.set("IC") // Target IDE Platform

  plugins.set(listOf("java", "copyright"))
  intellij.updateSinceUntilBuild.set(false)
}

tasks.register<Zip>("packageDistribution") {
  archiveFileName.set("DBN.zip")
  destinationDirectory.set(layout.buildDirectory.dir("dist"))

  from("lib/ext/") {
    include("**/*.jar")
    into("dbn/lib/ext")
  }
  from(layout.buildDirectory.dir("libs")) {
    include("${project.name}-${project.version}.jar")
    into("dbn/lib")
  }
}

tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
  }

/* no kotlin code yet
withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
}
*/

  withType<JavaCompile>{
    copy {
      from("lib/ext")
      include("**/*.jar")
      into(layout.buildDirectory.dir("idea-sandbox/plugins/${project.name}/lib/ext"))
    }
  }

  patchPluginXml {
    sinceBuild.set("201.6668.113")
    untilBuild.set("242.*")
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
            "-Xms512m",
            "-Xmx2048m",
            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1044",
        )
   }
}
