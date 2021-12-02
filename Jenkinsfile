pipeline {
    agent any
    stages {
        stage ('compilation') {
            steps {
                sh 'echo hello there. this  is compilation'
                sh 'echo hello world'
            }
        }
        stage ('deployment') {
            steps {
                sh 'echo hello there. this  is deployment'
            }
        }
        stage ('finalize') {
            steps {
                sh 'hello there. this  is finalization'
            }
        }
        stage ('send mail') {
            steps {
                mail bcc: '', body: 'the build is finished', cc: '', from: '', replyTo: '', subject: 'This is the build emai', to: 'usntechnologies@gmail.com'
            }
        }
    }
    post {
        failure {
            sh 'echo the build failed'
        }
        success {
            sh 'echo the build is SUCCESSFULL'
        }
        always {
            sh 'echo the build as completed'
        }
    }
}
