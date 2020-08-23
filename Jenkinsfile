pipeline {
 agent {
     node {
         label 'master'
     }
 }
stages {
    stage ('This demo stage'){
     steps{    
    sh """
     df -h
     cd /tmp
     touch demo.txt 
    """
     }
}
    stage ('This to demo second stage') {
        steps{
        echo "step demo"
    }
    }
}
}
