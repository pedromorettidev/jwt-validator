# jwt-validator
Sistema de validação de JWT desenvolvido com Kotlin + Spring Boot, com foco em regras de domínio, qualidade de código, testes automatizados e visão de arquitetura moderna backend.

## Como rodar localmente?

./gradlew clean build

./gradlew bootRun

## Overview

Este projeto implementa um validador de JWT baseado em regras específicas de negócio.

O token é considerado válido apenas se:

- Deve ser um JWT válido
- Deve conter apenas 3 claims (Name, Role e Seed)
- A claim Name não pode ter carácter de números
- A claim Role deve conter apenas 1 dos três valores (Admin, Member e External)
- A claim Seed deve ser um número primo.
- O tamanho máximo da claim Name é de 256 caracteres.

## Architecture

Arquitetura simples, desacoplada e testável:

Controller -> Service (TokenValidatorService) -> Decoder (JwtDecoder) -> Domain (TokenClaims)

Responsabilidades:

Controller → exposição HTTP da API

Service → regras de validação de JWT

Decoder → parsing e extração de claims

Domain → modelo de dados do token


## Tech Stack
- Kotlin
- Spring Boot
- Java 21
- Spring Web
- Auth0 Java JWT
- JUnit 5
- MockK
- JaCoCo (85% de coverage)
- Detekt
- Docker
- GitHub Actions
- Terraform (AWS ECR e ECS modelado)
- Spring Boot Actuator
 
## API
Validar JWT

POST /jwt/validate?jwt={token}

Response:

true | false

## Testing Strategy

O projeto possui dois níveis de testes:

### Unit Tests

validação isolada de regras de negócio

mocking do decoder JWT

cobertura de todos os cenários críticos

### Integration Tests

testes via MockMvc

validação end-to-end do fluxo HTTP

./gradlew test

### Coverage

./gradlew jacocoTestReport

## Code Quality

### Análise estática com Detekt:

./gradlew detekt

Regras aplicadas:

complexidade de funções

magic numbers

boas práticas Kotlin

consistência de código

## Observability

### Logs estruturados via SLF4J:

início de validação de token

falhas de decode

violações de regras de negócio

validação bem-sucedida

Nenhum dado sensível do JWT é exposto em logs.

### Actuator

Endpoints disponíveis:

/actuator/health

/actuator/info

## Docker

Build da imagem

docker build -t jwt-validator .

Execução

docker run -p 8080:8080 jwt-validator

Arquitetura da imagem

Aplicação empacotada em um JAR Spring Boot executado em Java 21 JRE.

## CI/CD (GitHub Actions)

Pipeline automatizada com os seguintes estágios:

1. Build da aplicação
2. Execução de testes unitários e integração
3. Análise estática (Detekt)
4. Geração de relatório de cobertura (JaCoCo)
5. Build da imagem Docker

Executado em:

push na branch principal

pull requests

## Infrastructure as Code (Terraform)

Infraestrutura modelada com Terraform para AWS:

### Recursos:

Amazon ECR (repositório de imagens Docker)

Amazon ECS (Para executar a imagem docker do registry em FARGATE)

### Objetivo:

Demonstrar conhecimento em:

infraestrutura como código

container registry

base para deploy cloud-native

## Project Status

✔ API funcional  
✔ Regras de domínio implementadas  
✔ Testes unitários e integração  
✔ CI completo (GitHub Actions)  
✔ Docker container funcional  
✔ Qualidade de código (Detekt + JaCoCo)  
✔ Observabilidade (Logs + Actuator)  
✔ Infraestrutura como código (Terraform com ECR e ECS modelado)

A pipeline atual cobre todo o ciclo de integração contínua. Não foi implementado qualquer tipo de CD por questões de maior praticidade e custo. Contudo, em teoria essa seria a evolução natural:

- Push de imagem para ECR/GHCR

- Deploy automatizado via ECS Fargate ou Kubernetes

- Versionamento de imagem por commit SHA

Exemplo de action no Github Actions que poderia enviar uma imagem ao ECR
```yaml
  - name: Configure AWS credentials
  uses: aws-actions/configure-aws-credentials@v4
  with:
    aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
    aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
    aws-region: us-east-1

- name: Login to Amazon ECR
  uses: aws-actions/amazon-ecr-login@v2

- name: Build, tag and push image to ECR
  run: |
    IMAGE_TAG=${{ github.sha }}
    docker build -t $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG .
    docker push $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG
```

## Design Decisions

O endpoint POST /jwt/validate?jwt={token} envia o token via query string, o que é um risco: tokens aparecem em logs de servidor, proxies e histórico de browser. O mais seguro seria receber via header Authorization ou no body. Foi feito dessa forma seguindo os requisitos escritos do desafio de forma literal.

Separação Decoder vs Service

Permite:

1. isolamento de responsabilidade
2. testabilidade com mocks
3. desacoplamento de parsing JWT e regras de negócio

As validações são interrompidas assim que uma regra falha, otimizando execução.

Uso de enum para Role para mapear String para um dos valores listados no Enum de domínio

Mapeamento seguro de valores com fallback controlado para entradas inválidas. Permite ao usuário identificar que o token JWT é inválido de toda forma e loga erro evidenciando o que ocorreu.

Foi considerada a adoção de uma camada de Use Cases para isolar regras de domínio. Entretanto, foi optado por manter uma arquitetura mais pragmática, dado o escopo do projeto, mantendo separação entre Controller e Service.
