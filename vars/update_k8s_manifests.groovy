def call(Map config) {
    def imageTag = config.imageTag
    def manifestsPath = config.manifestsPath ?: 'kubernetes'
    def gitCredentials = config.gitCredentials ?: 'github-creds'
    def gitUserName = config.gitUserName ?: 'Jenkins CI'
    def gitUserEmail = config.gitUserEmail ?: 'jenkins@ci.local'

    powershell """
        Write-Host "Updating Kubernetes manifests with image tag: ${imageTag}"

        Get-ChildItem "${manifestsPath}\\*.yaml" | ForEach-Object {
            (Get-Content \$_.FullName) -replace 'image: vikas1432/qbshop-app:.*', 'image: vikas1432/qbshop-app:${imageTag}' | Set-Content \$_.FullName
            (Get-Content \$_.FullName) -replace 'image: vikas1432/qbshop-migration:.*', 'image: vikas1432/qbshop-migration:${imageTag}' | Set-Content \$_.FullName
        }

        git config user.name "${gitUserName}"
        git config user.email "${gitUserEmail}"

        git add "${manifestsPath}"
        git diff --cached --quiet
        if (\$LASTEXITCODE -ne 0) {
            git commit -m "Update Kubernetes image tags to ${imageTag}"
        }
    """

    withCredentials([usernamePassword(
        credentialsId: gitCredentials,
        usernameVariable: 'GIT_USERNAME',
        passwordVariable: 'GIT_PASSWORD'
    )]) {
        powershell '''
            $pair = "$env:GIT_USERNAME`:$env:GIT_PASSWORD"
            $encoded = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($pair))
            git -c "http.extraheader=Authorization: Basic $encoded" push https://github.com/vikas7666277467-sys/Qualibytes-Ecommerce_Project.git HEAD:main
        '''
    }
}