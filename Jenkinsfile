@Library("jenkins-shared-library")

def gv
pipeline {
    agent any
    tools{
        maven "maven-3.8"
    }

    stages {
        stage('init'){
            steps{
                script{
                    gv = load "script.groovy"
                }
            }

        }
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
                    buildDockerImage()                
                }
            }
        }
        stage('test'){
            steps{
                script{
                    gv.testApp()    
                }
                
            }
        }
        stage('deploy'){
            steps{
                script{
                    gv.testApp()    
                }
            }
        }
    }
}