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
    }

    post {
        always {
            deleteDir()
        }
    }
}
