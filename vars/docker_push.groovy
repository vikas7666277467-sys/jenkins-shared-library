def call(Map config) {
    def imageName = config.imageName
    def imageTag = config.imageTag
    def credentialsId = config.credentials ?: 'dockerhub-creds'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKER_USERNAME',
        passwordVariable: 'DOCKER_PASSWORD'
    )]) {

        powershell '''
            $ErrorActionPreference = "Stop"

            Write-Host "Docker username: $env:DOCKER_USERNAME"
            Write-Host "Docker password received: $([bool]$env:DOCKER_PASSWORD)"
            Write-Host "Docker password length: $($env:DOCKER_PASSWORD.Length)"

            $env:DOCKER_PASSWORD | docker login -u $env:DOCKER_USERNAME --password-stdin

            if ($LASTEXITCODE -ne 0) {
                Write-Error "Docker login failed"
                exit $LASTEXITCODE
            }

            Write-Host "Docker login successful"
        '''

        bat """
            docker push ${imageName}:${imageTag}
        """
    }
}