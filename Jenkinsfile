pipeline {
  agent any
  tools {
    jdk 'Java11'
  }

  options {
    buildDiscarder logRotator(numToKeepStr: '10')
  }

  stages {
    stage('Publish to repository') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Release ZIP') {
      steps {
        sh 'mkdir -p temp'

        sh 'cp v2/target/v2-1.0-SNAPSHOT.jar temp/ConsoleBot-v2.jar'
        sh 'cp v3/target/v3-1.0-SNAPSHOT.jar temp/ConsoleBot-v3.jar'
        zip archive: true, dir: 'temp', glob: '', zipFile: 'ConsoleBot.zip'

        sh 'rm -r temp/'
      }
    }

    stage('Archive') {
      steps {
        archiveArtifacts artifacts: '**/build/libs/*.jar'
      }
    }
  }
}

pipeline {
    agent any
    tools {
        maven 'Maven 3'
        jdk 'Java 8'
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
