def call(Map config) {
    def imageName = config.imageName
    def imageTag = config.imageTag
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'

    sh """
        docker build -f ${dockerfile} -t ${imageName}:${imageTag} ${context}
    """
}
