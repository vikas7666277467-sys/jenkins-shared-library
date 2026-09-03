def call() {
    bat """
        "C:\\Tools\\Trivy\\trivy.exe" image --exit-code 0 --severity HIGH,CRITICAL ${env.DOCKER_IMAGE_NAME}:${env.DOCKER_IMAGE_TAG}
        "C:\\Tools\\Trivy\\trivy.exe" image --exit-code 0 --severity HIGH,CRITICAL ${env.DOCKER_MIGRATION_IMAGE_NAME}:${env.DOCKER_IMAGE_TAG}
    """
}