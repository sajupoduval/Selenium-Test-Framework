pipeline {
    agent any

    tools {
        maven 'maven_3.9.9' //Jenkins tools reflect
    }

     // 🛠️ MAC PATH FIX: This forces Jenkins to find Docker for ALL stages automatically
    environment {
         PATH = "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:${env.PATH}"
         COMPOSE_PATH = "${WORKSPACE}/docker" // 🔁 Adjust if compose file is elsewhere
         SELENIUM_GRID = "true"
     }

    stages {
         stage('Start Selenium Grid via Docker Compose') {
             steps {
                     script {
                         echo "Cleaning up any loose background instances..."
                                             // This will now execute perfectly without 'command not found'
                                             sh "docker compose -f ${COMPOSE_PATH}/docker-compose.yml down"

                                             echo "Starting Unified Infrastructure (Grid + App + DB)..."
                                             sh "docker compose -f ${COMPOSE_PATH}/docker-compose.yml up -d"

                                             echo "Waiting 15 seconds for DB initialization..."
                                             sleep 15

                                             echo "Executing Silent Auto-Installer..."
                                             sh """
                                             docker exec -i orangehrm-app php /var/www/html/installer/index.php install \
                                             --db_host=orangehrm-db \
                                             --db_name=orangehrm_db \
                                             --db_user=orangehrm_user \
                                             --db_password=orangehrm_password \
                                             --admin_user=Admin \
                                             --admin_password=admin123
                                             """
                     }
                 }
            }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sajupoduval/Selenium-Test-Framework.git'
            }
        }

        stage('Build') {
            steps {
                 sh 'mvn clean install -DseleniumGrid=true'
//                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                 sh "mvn clean test -DseleniumGrid=true"
//                sh "mvn clean test"
            }
        }

         stage('Stop Selenium Grid') {
             steps {
                 script {
                     echo "Stopping Selenium Grid..."
                     sh "docker compose -f ${COMPOSE_PATH}/docker-compose.yml down"
                 }
             }
         }

        stage('Reports') {
            steps {
                publishHTML(target: [
                    reportDir: 'src/test/resources/ExtentReport',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report'
                ])
            }
        }
    }

//     post {
//         always {
//             archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
//             junit 'target/surefire-reports/*.xml'
        post {
            always {
                // Fix 1: Added allowEmptyArchive to prevent crashes if no HTML report is generated
                archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html',
                                 fingerprint: true,
                                 allowEmptyArchive: true

                // Fix 2: Added a double asterisk (**/) to search recursively for the XML results
                // Fix 3: Added allowEmptyResults to stop Jenkins from marking the build as an error when files are missing
                junit testResults: '**/target/surefire-reports/*.xml',
                      allowEmptyResults: true
                  }

        success {
            emailext (
                to: 'sajuanidile@gmail.com',
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <html>
                <body>
                <p>Hello Team,</p>
                <p>The latest Jenkins build has completed successfully.</p>
                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                <p><b>Extent Report:</b> <a href="http://localhost:8080/job/${env.JOB_NAME}/${env.BUILD_NUMBER}/artifact/src/test/resources/ExtentReport/ExtentReport.html">Click here</a></p>
                <p>Best regards,</p>
                <p><b>Automation Team</b></p>
                </body>
                </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }

        failure {
            emailext (
                to: 'sajuanidile@gmail.com',
                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <html>
                <body>
                <p>Hello Team,</p>
                <p>The latest Jenkins build has <b style="color: red;">FAILED</b>.</p>
                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> <span style="color: red;"><b>FAILED &#10060;</b></span></p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                <p><b>Please check the logs and take necessary actions.</b></p>
                <p><b>Extent Report (if available):</b> <a href="http://localhost:8080/job/${env.JOB_NAME}/HTML_20Extent_20Report/">Click here</a></p>
                <p>Best regards,</p>
                <p><b>Automation Team</b></p>
                </body>
                </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}