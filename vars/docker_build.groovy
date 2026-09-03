def call(Map config) {
    def imageName = config.imageName
    def imageTag = config.imageTag
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'

    bat """
        docker build -f "${dockerfile}" -t "${imageName}:${imageTag}" "${context}"
    """
}