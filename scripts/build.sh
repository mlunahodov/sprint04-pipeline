  #!/bin/bash

  # ========================
  # CONFIGURAÇÕES
  # ========================
  RM_NUMERO="557074"                         # <<< troque pelo seu RM
  ACR_NAME="acrvroomrm$RM_NUMERO"
  RG_NAME="rg-vroom-rm$RM_NUMERO"
  IMAGE_NAME="appvroom:latest"
 
  # ========================
  # CRIAR RESOURCE GROUP
  # ========================
  echo ">> Criando Resource Group..."
  az group create --name $RG_NAME --location eastus


  # ========================
  # CRIAR ACR
  # ========================
  echo ">> Criando Azure Container Registry..."
  az acr create --resource-group $RG_NAME --name $ACR_NAME --sku Basic

  # ========================
  # LOGIN NO ACR
  # ========================
  echo ">> Fazendo login no ACR..."
  az acr login --name $ACR_NAME


  # ========================
  # TAG E PUSH
  # ========================
  echo ">> Tageando a imagem para o ACR..."
  docker tag $IMAGE_NAME $ACR_NAME.azurecr.io/$IMAGE_NAME

  echo ">> Enviando a imagem para o ACR..."
  docker push $ACR_NAME.azurecr.io/$IMAGE_NAME

  # ========================
  # HABILITAR ADMIN DO ACR
  # ========================
  echo ">> Habilitando acesso admin no ACR..."
  az acr update --name $ACR_NAME --admin-enabled true

  echo "✅ Build e Push concluídos com sucesso!"
