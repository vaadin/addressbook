pipeline {
 agent {
     node {
         label 'master'
     }
 }
stages {
    stage ('This demo stage'){
     steps{    
         parallel(
          job1: {   
                 sh """
                df -h
                cd /tmp
                touch demo.txt 
                """
     },
        job2: {
             sh """
             cat /etc/passwd
             """

        }
         )
}
    }
    stage ('This to demo second stage') {
        steps{
        echo "step 1"
    }
    }
}
}
