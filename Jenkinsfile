pipeline {

    agent any 

    tools {

        maven 'maven_3'

        jdk 'java_8'

    }

    stages {

        stage('Checkout the code') { 

            steps {

               git branch: 'develop', url: 'https://github.com/devopstrainers1/addressbook.git'

            }

        }

        stage('Code Compile') { 

            steps {

                sh 'mvn compile' 

            }

        }

        stage('Run Unit Test') { 

            steps {

                sh 'mvn test' 

            }

        }



        stage('Create Package') { 

            steps {

                sh 'mvn package' 

            }

        }

    }

}
