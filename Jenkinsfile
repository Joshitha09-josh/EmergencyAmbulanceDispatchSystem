pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK25'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Joshitha09-josh/EmergencyAmbulanceDispatchSystem.git'
            }
        }

        stage('Java Version') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }
    }

    post {
        success {
            echo 'Emergency Ambulance Dispatch System CI/CD completed successfully!'
        }

        failure {
            echo 'Build or tests failed. Check the Jenkins console output.'
        }
    }
}