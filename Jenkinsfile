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
            withMaven(maven: 'M3', mavenOpts: '-Xmx1024M -XX:+TieredCompilation -XX:TieredStopAtLevel=1', jdk: 'JAVA8') {
	           
                bat "mvn clean install -DskipTests"
            }
            archiveArtifacts artifacts: '**/helloworldrest/target/helloworld.war'
        }
	    stage('SonarQube analysis') {
	       withMaven(maven: 'M3', mavenOpts: '-Xmx1024M -XX:-UseGCOverheadLimit') {
	            bat "mvn sonar:sonar -Dsonargraph.prepareForSonar=true"
	       }
	    }
        stage('Stop Old Envs') {
	       bat "docker stop postgresql"
           bat "docker rm postgresql"
           bat "docker stop tomcat"
           bat "docker rm tomcat"
	    }
        stage('Start POSTGRESQL container') {
	       bat "docker run --name postgresql -e POSTGRESQL_USERNAME=demo -e POSTGRESQL_PASSWORD=password -e POSTGRESQL_DATABASE=DEMO -p 5432:5432 -d bitnami/postgresql:latest"
           sleep 60
	    }
         stage('Start Tomcat container') {
	       bat "docker run --name tomcat -p 8080:8080 -v C:/Users/ayoub/workspace_Demo/Data:/opt/bitnami/tomcat/logs --link postgresql:postgresqlalias -d bitnami/tomcat:8.5.35"
           sleep 60
	    }
        stage('Deploy War') {
	       bat "docker cp helloworldrest/target/helloworld.war tomcat:/app"
           sleep 60
	    }


        stage('Tests Cucumber') {
	        parallel 'Firefox': {
	        	echo 'Firefox'
	        	stage ('Starting Tests') {
	        		withMaven(maven: 'M3', mavenOpts: '-Xmx1024M') {
				      
			          bat "mvn test -Dtest=*IT -Dbrowser=FIREFOX -DfailIfNoTests=false"
			        }
			        step([
			            	$class: 'CucumberReportPublisher',
			            	jsonReportDirectory: 'helloworld-cucumber/target/results/JSON',
			            	fileIncludePattern: 'cucumber.json'
			        ])
			        
				}
	        },
	        	'Chrome': {
	        		echo 'Chrome'
	        		stage ('Starting Tests') {
	        		withMaven(maven: 'M3', mavenOpts: '-Xmx1024M') {
				            
			                bat "mvn test -Dtest=*IT -Dbrowser=CHROME -DfailIfNoTests=false"
			            }
			            
			            step([
			            	$class: 'CucumberReportPublisher',
			            	jsonReportDirectory: 'helloworld-cucumber/target/results/JSON',
			            	fileIncludePattern: 'cucumber.json'
			            ])
		
					}
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
