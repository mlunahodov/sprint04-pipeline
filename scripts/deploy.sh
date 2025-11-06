#!/bin/bash

# Variáveis
RM=557074  # <<< troque pelo seu RM
RESOURCE_GROUP=rg-vroom-rm$RM
ACR_NAME=acrvroomrm$RM
APP_IMAGE_NAME=appvroom
DB_IMAGE_NAME=postgres
POSTGRES_VERSION=17-alpine
DB_TAG=17-alpine
APP_TAG=latest

DB_NAME=mydatabase
DB_USER=myuser
DB_PASSWORD=secret

# Verificar se RESOURCE_GROUP está definido
if [ -z "$RESOURCE_GROUP" ]; then
  RESOURCE_GROUP=rg-vroom-rm$RM
  echo "Resource Group definido como: $RESOURCE_GROUP"
fi

# Fazer login no ACR
echo "🔑 Fazendo login no ACR..."
az acr login --name $ACR_NAME

# Habilitar admin user no ACR (necessário para puxar imagem no ACI)
echo "⚙️ Habilitando admin user no ACR..."
az acr update -n $ACR_NAME --admin-enabled true

# Baixar a imagem do postgres do Docker Hub
echo "⬇️ Baixando imagem oficial do PostgreSQL..."
docker pull postgres:$POSTGRES_VERSION

# Tagear a imagem do postgres para o ACR
echo "🏷️ Tageando a imagem do PostgreSQL para o ACR..."
docker tag postgres:$POSTGRES_VERSION $ACR_NAME.azurecr.io/$DB_IMAGE_NAME:$DB_TAG

# Enviar a imagem do postgres para o ACR
echo "📤 Enviando a imagem do PostgreSQL para o ACR..."
docker push $ACR_NAME.azurecr.io/$DB_IMAGE_NAME:$DB_TAG

# Confirmar se a imagem existe no ACR
echo "🔍 Verificando imagem do Banco de dados no ACR..."
az acr repository show-tags -n $ACR_NAME --repository $DB_IMAGE_NAME -o table

# Obter credenciais do ACR
echo "🔑 Obtendo credenciais do ACR..."
ACR_USERNAME=$(az acr credential show -n $ACR_NAME --query username -o tsv)
ACR_PASSWORD=$(az acr credential show -n $ACR_NAME --query "passwords[0].value" -o tsv)


# Deploy do PostgreSQL com imagem do ACR
echo "🚀 Criando container do PostgreSQL no ACI..."
az container create \
  --resource-group $RESOURCE_GROUP \
  --name aci-db-vroom-rm$RM \
  --image $ACR_NAME.azurecr.io/$DB_IMAGE_NAME:$DB_TAG \
  --cpu 1 --memory 2 \
  --registry-login-server $ACR_NAME.azurecr.io \
  --registry-username $ACR_USERNAME \
  --registry-password $ACR_PASSWORD \
  --environment-variables \
    POSTGRES_PASSWORD=$DB_PASSWORD \
    POSTGRES_DB=$DB_NAME \
    POSTGRES_USER=$DB_USER \
  --ports 5432 \
  --os-type Linux \
  --dns-name-label aci-db-vroom-rm$RM

echo "⏳ Aguardando o PostgreSQL iniciar..."
sleep 60

# Obtém o FQDN do banco de dados
DB_FQDN=$(az container show --resource-group $RESOURCE_GROUP --name aci-db-vroom-rm$RM --query ipAddress.fqdn -o tsv)
echo "✅ Banco de dados disponível em: $DB_FQDN:5432"

echo "🔍 Verificando imagem da aplicação no ACR..."
az acr repository show-tags -n $ACR_NAME --repository $APP_IMAGE_NAME -o table

# Deploy da aplicação usando imagem do ACR (usa APP_TAG separado)
echo "🚀 Criando container da aplicação no ACI..."
az container create \
  --resource-group $RESOURCE_GROUP \
  --name aci-app-vroom-rm$RM \
  --image $ACR_NAME.azurecr.io/$APP_IMAGE_NAME:$APP_TAG \
  --cpu 2 --memory 3 \
  --registry-login-server $ACR_NAME.azurecr.io \
  --registry-username $ACR_USERNAME \
  --registry-password $ACR_PASSWORD \
  --environment-variables \
    SPRING_DATASOURCE_URL="jdbc:postgresql://$DB_FQDN:5432/$DB_NAME" \
    SPRING_DATASOURCE_USERNAME=$DB_USER \
    SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    SPRING_FLYWAY_URL="jdbc:postgresql://$DB_FQDN:5432/$DB_NAME" \
    SPRING_FLYWAY_USER=$DB_USER \
    SPRING_FLYWAY_PASSWORD=$DB_PASSWORD \
  --ports 8080 \
  --os-type Linux \
  --dns-name-label aci-app-vroom-rm$RM

echo "✅ Aplicação implantada: http://aci-app-vroom-rm$RM.eastus.azurecontainer.io:8080"