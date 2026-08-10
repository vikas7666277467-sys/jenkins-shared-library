def call(Map config) {
    def imageTag = config.imageTag
    def manifestsPath = config.manifestsPath ?: 'kubernetes'
    def gitCredentials = config.gitCredentials ?: 'github-creds'
    def gitUserName = config.gitUserName ?: 'Jenkins CI'
    def gitUserEmail = config.gitUserEmail ?: 'jenkins@ci.local'

    sh """
        echo "Updating Kubernetes manifests with image tag: ${imageTag}"

        sed -i -E "s|image: vikas1432/qbshop-app:.*|image: vikas1432/qbshop-app:${imageTag}|g" ${manifestsPath}/*.yaml

        sed -i -E "s|image: vikas1432/qbshop-migration:.*|image: vikas1432/qbshop-migration:${imageTag}|g" ${manifestsPath}/*.yaml

        git config user.name "${gitUserName}"
        git config user.email "${gitUserEmail}"

        git add ${manifestsPath}
        git diff --cached --quiet || git commit -m "Update Kubernetes image tags to ${imageTag}"
    """

    withCredentials([usernamePassword(
        credentialsId: gitCredentials,
        usernameVariable: 'GIT_USERNAME',
        passwordVariable: 'GIT_PASSWORD'
    )]) {
        sh '''
            git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/vikas7666277467-sys/Qualibytes-Ecommerce_Project.git HEAD:main
        '''
    }
}
