Khane Be Doosh project
---
To deploy the project set `TomcatServer` server name in tomcat-users <br>
For moe guide use[this link](https://gitlab.com/internet-engineering/khane-be-doosh.git)<br>


####Deploying Project
Use Following command for first build:
```bash
mvn tomcat7:deploy
```

and for rest of times:
```bash
mvn clean tomcat7:redeploy
```
