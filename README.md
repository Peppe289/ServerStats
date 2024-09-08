# ServerStats

This repository is created to easily see the resources on the server. Access does not require login or other credentials. Just log in with IP and port on your browser. You will have a minimal dashboard where you can see CPU, RAM and disk usage along with other related information such as CPU frequency.

# How to use?

Just take the war in the release notes and put it in tomcat's webapps folder and launch it. By default the tomcat port is 8080 on http. You can consult all the information relating to tomcat in their [docs](https://tomcat.apache.org/tomcat-10.1-doc/index.html).

### Quick setup

_Remember that this version of the project only works with tomcato 10.1.28, do not report errors without checking the correct version._

first, download and extract tomcat in `/opt/apache-tomcat-x.x.x` directory:

```sh
cd /opt
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.28/bin/apache-tomcat-10.1.28.tar.gz
tar -xf apache-tomcat-10.1.28.tar.gz
```

then, download last war from release tag into `/opt/apache-tomcat-10.1.28/webapps/`:

```sh
cd /opt/apache-tomcat-10.1.28/webapps/
wget https://github.com/Peppe289/ServerStats/releases/download/v1.0/ServerStats-1.0.war
```

now, start tomcat server.

```sh
cd ../bin
./startup.sh
```

Stop server:

```sh
cd /opt/apache-tomcat-10.1.28/bin/
./shutdown.sh
```
