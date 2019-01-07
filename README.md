# iw2fag
This project contains several modules

## core-java
`core-java`: This module is mainly related to Java core java and some algorithm.

## docker
`docker`: Under docker module, it has one sub module `skeleton`.

> What is `skeleton`:
* It provide one general solution to help user create micro service, e.g. create image, deploy to k8s
* It provide one basic web project skeleton to help user easily create docker project.

> How to use
```bash
mvn clean install -Pdocker-service -Ddocker.image.tag=latest -Ddocker.image.build.tag=latest -Dbuild.number=0 -Dcomponent.version=1.0.0
```

## nio
nio related
