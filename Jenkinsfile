pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK8'
    }
    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: '5'))
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'v2/target/v2-*-SNAPSHOT.jar', fingerprint: true
                    archiveArtifacts artifacts: 'v3/target/v3-*-SNAPSHOT.jar', fingerprint: true
                }
            }
        }

        stage ('Deploy') {
            when {
                branch "master"
            }

            steps {
                rtMavenDeployer (
                        id: "maven-deployer",
                        releaseRepo: "maven-releases",
                        snapshotRepo: "maven-snapshots"
                )
                rtMavenResolver (
                        id: "maven-resolver",
                        releaseRepo: "release",
                        snapshotRepo: "snapshot"
                )
                rtMavenRun (
                        pom: 'pom.xml',
                        goals: 'javadoc:javadoc javadoc:jar source:jar install -DskipTests',
                        deployerId: "maven-deployer",
                        resolverId: "maven-resolver"
                )
                step([$class: 'JavadocArchiver', javadocDir: 'target/site/apidocs', keepAll: false])
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
