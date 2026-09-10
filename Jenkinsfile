pipeline {
    agent any

    tools {
        jdk 'JDK25'
        maven 'Maven3.9.14'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
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
                echo 'Building the Emergency Ambulance Dispatch System...'
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                bat 'mvn clean test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'
                bat 'mvn package -DskipTests'
            }
        }
    }

    post {
        success {
            echo '======================================'
            echo 'CI/CD PIPELINE SUCCESSFUL'
            echo 'Emergency Ambulance Dispatch System'
            echo '======================================'
        }

        failure {
            echo '======================================'
            echo 'CI/CD PIPELINE FAILED'
            echo 'Check the console output for details.'
            echo '======================================'
        }
    }
}