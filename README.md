## Khane Be Doosh project
To deploy the project set `TomcatServer` server name in tomcat-users <br>
For moe guide use[this link](https://www.mkyong.com/maven/how-to-deploy-maven-based-war-file-to-tomcat/)<br>


## Deploying Project
Use Following command for first build:
```bash
mvn tomcat7:deploy
```
and for rest of times:
```bash
mvn clean tomcat7:redeploy
```