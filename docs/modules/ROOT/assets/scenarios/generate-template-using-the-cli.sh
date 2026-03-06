#$ delay 10
quarkus backstage template --help
#$ expect \$
quarkus backstage template generate
#$ expect \$
ls -al
#$ expect \$
tree .backstage
#$ expect \$
