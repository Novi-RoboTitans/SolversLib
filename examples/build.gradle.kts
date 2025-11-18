plugins {
    id("dev.frozenmilk.teamcode") version "10.3.0-0.1.4"
}

ftc {
    kotlin

    // note: sdk automatically added
}

dependencies {
    implementation("org.solverslib:core")
    implementation("org.solverslib:pedroPathing")

    implementation("com.pedropathing:ftc:2.0.4")
}

repositories {
    maven("https://maven.brott.dev/")
    maven("https://maven.pedropathing.com/")
}
