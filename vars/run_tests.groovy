def call() {
    sh '''
        if [ -f package.json ]; then
            npm ci
            npm test -- --passWithNoTests
        elif [ -f requirements.txt ]; then
            pip install -r requirements.txt
            pytest
        else
            echo "No supported test configuration found. Skipping tests."
        fi
    '''
}
