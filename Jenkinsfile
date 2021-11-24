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
