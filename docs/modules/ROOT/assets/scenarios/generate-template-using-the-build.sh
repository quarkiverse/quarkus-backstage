#$ expect \$
rm -rf .backstage
#$ expect \$
ls -al
#$ expect \$
echo "quarkus.backstage.template.generation.enabled=true" >> src/main/resources/application.properties
#$ expect \$
mvn clean install -DskipTests
#$ expect \$
ls -al
#$ expect \$
tree .backstage
#$ expect \$
