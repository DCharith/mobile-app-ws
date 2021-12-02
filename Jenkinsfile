@Library("jenkins-shared-library")_

pipeline {
    agent any
    tools{
        maven "maven-3.8"
    }

    stages {
        stage('Build and package war file') {
            steps {
                script{
                    buildWar()
                }  
            }
        }
        stage('Build Docker image'){
            steps{
                script{
                    buildDockerImage("dcharith/mobile-app-ws:2.0")                
                }
            }
        }
        stage('Test'){
            steps{
                script{
                    echo "Testing..." 
                }
                
            }
        }
        stage('Deploy'){
            steps{
                script{
                    echo "Deploying..."   
                }
            }
        }
    }
}