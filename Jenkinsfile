#!groovy
node {
    try {
        stage('Checkout') {
            retry(3) {
                checkout scm
            }
            def v = version(readFile('pom.xml'))
            if (v) {
                echo "Building version ${v}"
            }
        }
        stage('Build') {
            maven(maven: 'M3', mavenOpts: '-Xmx1024M -XX:+TieredCompilation -XX:TieredStopAtLevel=1') {
	           
                sh "mvn clean install -DskipTests"
            }
            archiveArtifacts artifacts: '**/helloworldrest/target/helloworld.war'
        }
	    stage('SonarQube analysis') {
	       maven(maven: 'M3', mavenOpts: '-Xmx1024M -XX:-UseGCOverheadLimit') {
	            sh "mvn sonar:sonar -Dsonargraph.prepareForSonar=true"
	       }
	    }
        if (params.DEPLOY_DEV) {
            stage('Deploy DEV') {
                withEnv(["PATH+ANSIBLE=${tool 'Ansible 2.3.1.0'}"]) {
                	def version = version(readFile('pom.xml'))
                	writeFile file: 'extras.json', text: "{'app_version':'${version}'}"
                    ansiblePlaybook(
                            playbook: '3rdParty/ansible/playbook.yml',
                            inventory: '3rdParty/ansible/cap.ini',
                            credentialsId: "7c964255-1bbc-4256-9afa-fa8215ac9325",
                            installation: 'Ansible 2.3.1.0',
                            ansibleName: 'Ansible 2.3.1.0',
                            extras: '-e "@extras.json"')
                }
            }
        }
        
        if (params.DEPLOY_QUAL) {
            stage('Deploy QUAL') {
                withEnv(["PATH+ANSIBLE=${tool 'Ansible 2.3.1.0'}"]) {
                	def version = version(readFile('pom.xml'))
                	writeFile file: 'extras.json', text: "{'app_version':'${version}'}"
                    ansiblePlaybook(
                            playbook: '3rdParty/ansible/playbook.yml',
                            inventory: '3rdParty/ansible/qual.ini',
                            credentialsId: "536eb292-6e27-4142-85a6-ea30b82c69d3",
                            installation: 'Ansible 2.3.1.0',
                            ansibleName: 'Ansible 2.3.1.0',
                            extras: '-e "@extras.json"')
                }
            }
        }
        
        if (params.UT == null || params.UT) {
	        stage('Unit-Tests') {
	            maven(maven: 'M3', mavenOpts: '-Xmx1024M') {
	                sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent surefire:test"
	            }
	            step([
	                    $class     : 'JUnitResultArchiver',
	                    testResults: '**/target/surefire-reports/TEST*.xml'
	            ])
	        }
        }
        if (params.IT == null || params.IT) {
	        stage('Integrations-Tests') {
	            maven(maven: 'M3', mavenOpts: '-Xmx1024M') {
	            
	            	def proxyOrange=""
				    if(params.ORANGE){
				       	proxyOrange="-DproxySet=true -DproxyHost=proxy.rd.francetelecom.fr -DproxyPort=3128"
				    }
	                sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent surefire:test -Dtest=*IT test -DfailIfNoTests=false"
	            }
	            step([
	                    $class     : 'JUnitResultArchiver',
	                    testResults: '**/target/surefire-reports/TEST*.xml'
	            ])
	        }
        }
        
    } catch (any) {
       
       echo "ERROR "
       throw any
    } 
}
@NonCPS
def version(text) {
    def matcher = text =~ '<version>(.+)</version>'
    matcher ? matcher[0][1] : null
}
