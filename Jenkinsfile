def gv
pipeline {
    agent any
    tools{
        maven "maven 3.8"
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
                    gv.buildWar()
                }
                
            }
        }
        stage('Build Docker image'){
            steps{
                script{
                    gv.buildDockerImage()                
                }
            }
        }
        stage('test'){
            steps{
                gv.testApp()
            }
        }
        stage('deploy'){
            steps{
                gv.deployApp()
            }
        }
    }
}