#$ delay 10
quarkus plug add io.quarkiverse.backstage:quarkus-backstage-cli:999-SNAPSHOT
#$ expect \$
quarkus backstage --help
#$ expect \$
quarkus backstage entities --help
#$ expect \$
rm -rf catalog-info.yaml
#$ expect \$
ls -al
#$ expect \$
quarkus backstage entities generate
#$ expect \$
ls -al
#$ expect \$
cat catalog-info.yaml
#$ expect \$
