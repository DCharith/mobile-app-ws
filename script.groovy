def buildWar(){
    echo "Packaging war file..."
    sh "mvn -Dmaven.test.skip=true clean package"
}

def buildDockerImage(){
    echo "Building Docker image..."
    withCredentials([usernamePassword(credentialsId: "dockerhub-credentials", usernameVariable: "username", passwordVariable: "password")]){
        sh "docker build -t dcharith/mobile-app-ws:2.0 ."
        sh "echo $password | docker login -u $username --password-stdin"
        sh "docker push dcharith/mobile-app-ws:2.0"
    }
}

def testApp(){
    echo 'This is the test step!'
}

def deployApp(){
    echo 'This is the deploy step!'
}

return this