pipeline {
    agent any
    tools{
        maven "maven 3.8"
    }

    stages {
        stage('Build and package war file') {
            steps {
                script{
                    echo "Packaging war file..."
                    sh "mvn -Dmaven.test.skip=true clean package"
                }
                
            }
        }
        stage('Build Docker image'){
            steps{
                script{
                    echo "Building Docker image..."
                    withCredentials([usernamePassword(credentialsId: "dockerhub-credentials", usernameVariable: "username", passwordVariable: "password")]){
                        sh "mkdir -p mobile-app-ws"
                        sh "cd .."
                        sh "cp -r /var/jenkins_home/workspace/declarative-pipeline-job/src /var/jenkins_home/workspace/declarative-pipeline-job/mobile-app-ws/"
                        sh "cp -r /var/jenkins_home/workspace/declarative-pipeline-job/pom.xml /var/jenkins_home/workspace/declarative-pipeline-job/mobile-app-ws/"
                        sh "docker build -t dcharith/mobile-app-ws:2.0 ."
                        sh "echo $password | docker login -u $username --password-stdin"
                        sh "docker push dcharith/mobile-app-ws:2.0"
                    }
                }
            }
        }
        stage('test'){
            steps{
                echo 'This is the test step!'
            }
        }
        stage('deploy'){
            steps{
                echo 'This is the deploy step!'
            }
        }
    }
}