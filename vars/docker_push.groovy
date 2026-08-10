def call(Map config) {
    def imageName = config.imageName
    def imageTag = config.imageTag
    def credentialsId = config.credentials ?: 'docker-hub-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKER_USERNAME',
        passwordVariable: 'DOCKER_PASSWORD'
    )]) {
        sh '''
            echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
        '''

        sh """
            docker push ${imageName}:${imageTag}
        """
    }
}
