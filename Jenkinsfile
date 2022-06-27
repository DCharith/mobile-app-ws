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
        stage('Test'){
            steps{
                script{
                    gv.testApp()    
                }
                
            }
        }
        stage('Deploy'){
            steps{
                script{
                    gv.testApp()    
                }
            }
        }
    }
}
