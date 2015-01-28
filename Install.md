#INSTALLATION

You will need java 7

##Database
* If you are using database other than PostgreSQL, the driver needs to be added to www/WEB-INF/lib
* We included the script and image of database schema that we used in [github url]


##No instruction for Windows for now

##Mac:

* Download eclipse IDE for EE -- http://eclipse.org/downloads/
* Download Tomcat 7 -- http://tomcat.apache.org/
* Unzip eclipse
* Unzip tomcat

###Setting Up Tomcat Server
* Open Eclipse
* Go to View/Window > Show View > Servers
* Go back to main view, go to console view, select servers
* Right click > Add Server
* Select the version of tomcat you are using
* Click next, and point it to the directory where tomcat lives

###Setting Up Tomcat Plugin
* Download the plugin from eclipse.totale.com [Installation guide can be found on the website]
* Go to directory where eclipse lives, find folder called 'dropins'
* Unzip tomcat plugin
* Restart Eclipse

###Set Up Tomcat on Eclipse
* Go to Preferences > Tomcat
* Set the path to directory where tomcat lives
* Set tomcat version

###Clone the repo

###Server.xml
* Make a copy of server.xml from [APACHE_HOME]/conf
* add this to copy:
	
    <Host name="[HOST_NAME]" appBase="[APP_PATH]"
          unpackWARs="false" autoDeploy="false">
          <Valve className="org.apache.catalina.valves.AccessLogValve"
          directory="logs"  prefix="[PREFIX]" suffix=".txt"
          pattern="common" resolveHosts="false"/>
          <Context path="" reloadable="false" docBase="www" allowLinking="true"/>
    </Host>

HOST_NAME = url to reach the app
APP_PATH = where the application lives
PREFIX = for tomcat logging purposes

* Go to Preference > Tomcat. Under 'context declaration mode', select 'server.xml'
* Set configuration file to the copy of server.xml above


###Tomcat Project
* Click File > New > Project 
* Under 'Java", select 'Tomcat Project'
* Select 'Use Existing Location', point it to where application lives
* Set context to 'www'


###Configuration
* Two files need to be modified with your configuration

* Base.xml :
	- param value of Admin Login : this is the login when you go to myApp:8080/control
	- param value of Admin Password : this is the password when you go to myApp:8080/control

* web.xml :
	- Your database connection’s information
	- LMS Login : login that the LRS will expect from LMS
	- LMS Password : password that the LRS will expect from LMS
	- debug : ‘yes’ or ‘no’, type in ‘yes’ if you want to turn off the security features
	- Log path : where you want your log files to be 
	- Credentials list path : where your credentials list will be when the server is killed
	- Accepted domains list : only requests from this domains will be accepted, separated by commas
		example input : ‘http://google.com,https://google.com,http://mydomain.com:8080,https://mydomain.com:8080'

* tomcat project needs to have a web.xml inside of app_home/www/WEB-INF.
  We recommend create a symbolic link of web.xml in app_home/www/WEB-INF/conf/local/ directory


###Logging
For logging purposes, we use log4j2. and in order for log4j2 to run, there needs to be 
a copy of it under app_home/www/WEB-INF/classes/



###Dependencies
* On package explorer or project explorer, right click on the project, select 'Properties'
* Go to 'Java Build Path' > Add library > Server Runtime
* Add all jars in www/WEB-INF/lib if they are not added already

###Lastly
* Run Tomcat server 
* Type in [HOST_NAME]:8080 on your browser and you should see a web page
* Now you can make requests to the LRS
