#$ delay 10
quarkus backstage template --help
#$ expect \$
quarkus backstage template generate
#$ expect \$
ls -al
#$ expect \$
tree .backstage
#$ expect \$
echo "Let's see the steps of the generated template"
#$ expect \$
yq -r '.spec.steps[] | "\(.id): \(.action) (\(.name))"' ".backstage/templates/hello-quarkus/template.yaml"
#$ expect \$
