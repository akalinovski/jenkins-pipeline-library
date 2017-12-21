#!/usr/bin/groovy
def call(accessToken) {
    def serviceName = "sonarqube";
    def port = "9000";
    def scannerVersion = "2.8"
    def runSonarScanner = "true"

    if (runSonarScanner) {
        try {
            def srcDirectory = pwd();
            def tmpDir = pwd(tmp: true)

            //work in tmpDir - as sonar scanner will download files from the server
            dir(tmpDir) {
                def prId = "${env.JOB_NAME}".tokenize('/').last().tokenize('-').last()
                def jobName = "${env.JOB_NAME}".tokenize('/')[0]

                def localScanner = "scanner-cli.jar"

                def scannerURL = "http://central.maven.org/maven2/org/sonarsource/scanner/cli/sonar-scanner-cli/${scannerVersion}/sonar-scanner-cli-${scannerVersion}.jar"

                echo "downloading scanner-cli"

                sh "curl -o ${localScanner}  ${scannerURL} "

                echo("executing sonar scanner ")

                sh "java -jar ${localScanner}  -Dsonar.host.url=http://${serviceName}:${port}  -Dsonar.projectKey=${jobName} -Dsonar.sources=${srcDirectory} -Dsonar.github.pullRequest=${prId} -Dsonar.github.oauth=${accessToken} -Dsonar.analysis.mode=preview"
            }

        } catch (err) {
            echo "Failed to execute scanner:"
            echo "Exception: ${err}"
            throw err;
        }
    }

}