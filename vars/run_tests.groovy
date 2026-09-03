def call() {
    bat '''
        if exist package.json (
            npm ci
            npm test -- --passWithNoTests
        ) else if exist requirements.txt (
            pip install -r requirements.txt
            pytest
        ) else (
            echo No supported test configuration found. Skipping tests.
        )
    '''
}