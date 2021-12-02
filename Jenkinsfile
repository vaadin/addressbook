pipeline {
    agent { label 'tomcat' }
    stages {
        stage ('compilation') {
            steps {
                git 'https://github.com/skeeto/sample-java-project.git'
                sh 'mvn -B compile'
            }
        }
        stage ('static code analysis') {
            steps {
//                sh 'echo hello there. this  is deployment'
                sh '/home/ubuntu/sonar-scanner-4.6.2.2472-linux/bin/sonar-scanner'
            }
        }
        stage ('package') {
            steps {
                sh 'mvn -B package'
            }
        }
        stage ('deploy to tomcat') {
            steps {
                sh 'cp ./target/addressbook-2.0.war /var/lib/tomcat9/webapps/addressbook.war'
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
