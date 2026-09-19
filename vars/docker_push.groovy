def call(Map config) {
    def imageName = config.imageName
    def imageTag = config.imageTag
    def credentialsId = config.credentials ?: 'docker-hub-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKER_USERNAME',
        passwordVariable: 'DOCKER_PASSWORD'
    )]) {

        powershell '''
            $env:DOCKER_PASSWORD | docker login -u $env:DOCKER_USERNAME --password-stdin
        '''

        bat """
            docker push ${imageName}:${imageTag}
        """
    }
}