#$ delay 10
quarkus create app hello-backstage
#$ expect \$
cd hello-backstage
#$ expect \$
quarkus ext add io.quarkiverse.backstage:quarkus-backstage:999-SNAPSHOT
#$ expect \$
mvn clean install -DskipTests
#$ expect \$
ls -al
#$ expect \$
cat catalog-info.yaml 
#$ expect \$
