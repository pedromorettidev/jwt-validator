# jwt-validator
Sistema de validação de JWT desenvolvido com Kotlin + Spring Boot, com foco em regras de domínio, qualidade de código, testes automatizados e visão de arquitetura moderna backend.

## Como rodar localmente?

Digite os seguintes comandos no terminal:

Clone o repositório através do comando git clone https://github.com/pedromorettidev/jwt-validator.git no terminal

Acesse o projeto através do comando "cd" até alcançar o diretório clonado e execute os dois comandos

./gradlew clean build

./gradlew bootRun

Está presente nos arquivos do repositório a collection Postman para testar o endpoint da validação JWT.

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

A arquitetura do projeto foi baseada em 3 principais camadas. API, application (service e decoder) e domain.

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
- JaCoCo
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

Os testes unitários do projeto testam as partes isoladas do sistema. Temos testes em 2 classes do projeto, sendo elas TokenValidatorServiceImpl e JwtDecoderImpl.

Nos testes de TokenValidatorServiceImpl, recebemos como entrada um token JWT em formato de String, e logo em seguida mockamos a resposta do JwtDecoderImpl com mockk para inferir o comportamento que esperamos.

### Cenários testados da TokenValidatorServiceImpl:

### Happy path

retorna true para token completamente válido

### Validação da claim name

rejeita nomes com números

rejeita nomes com mais de 256 caracteres

rejeita name nulo

### Validação da claim seed

rejeita valores não primos

rejeita seed nulo

### Estrutura do token

rejeita quando número de claims é diferente do esperado

### Campos obrigatórios

rejeita quando name, role ou seed são nulos

### Falhas do decoder

retorna false quando ocorre exceção no decoding

### Cobertura geral

cobre regras de negócio

cobre casos de borda

cobre falhas de dependências externas

### Cenários testados da JwtDecoderImpl:

### Decodificação

decodifica JWT válido corretamente

lança exceção para JWT inválido

### Claims completas

constrói TokenClaims corretamente com payload válido

contabiliza corretamente número de claims

### Claims parciais

aceita JWT sem name, role ou seed

mantém campos ausentes como null

### Regras de domínio

converte role válido para RoleEnum

define role como null quando inválido

### Cobertura geral

parsing de JWT

mapeamento de claims

casos com payload parcial

tratamento de erro de decoding

### Integration Tests

Os testes integrados por sua vez instanciam o sistema de ponta-a-ponta. Simulamos todos os quatro cenários exibidos pelo challenge e validamos a resposta do endpoint de fato.

### Coverage

Para garantir a cobertura de cenários de teste implementados, o JaCoCo constrói um report que possui um threshold mínimo de 85% de código testado. Caso contrário, a task de coverage falha. Podemos visualizar o report usando:

./gradlew jacocoTestReport e ./gradlew jacocoTestCoverageVerification para visualizar se os testes passam do mínimo esperado.

## Code Quality

### Análise estática com Detekt:

./gradlew detekt

### Regras aplicadas:

Complexidade de funções para evitar funções de má manutenção e legibilidade

Magic numbers para evitar números de difícil entendimento para novos atuantes

Boas práticas Kotlin

Consistência de código

## Observability

### Logs estruturados via SLF4J:

Início de validação de token

Falhas de decode

Violações de regras de negócio

Validação bem-sucedida

Nenhum dado sensível do JWT é exposto em logs.

### Actuator

Utilizamos a dependência de Actuator que nos auxilia principalmente nos ambientes a visualizar a saúde de um serviço.

Endpoints disponíveis:

/actuator/health

/actuator/info

## Docker

Para criar a imagem da aplicação, temos um arquivo Dockerfile que utiliza do arquivo executável Jar gerado pela task de build do projeto para enviar a imagem ao ECR via pipeline CI/CD. 

Build da imagem:

docker build -t jwt-validator .

Execução:

docker run -p 8080:8080 jwt-validator

Arquitetura da imagem:

Aplicação empacotada em um JAR Spring Boot executado em Java 21 JRE.

## CI/CD (GitHub Actions)

Pipeline automatizada com os seguintes estágios:

1. Build da aplicação
2. Execução de testes unitários e integração
3. Análise estática (Detekt)
4. Geração de relatório de cobertura (JaCoCo)
5. Build da imagem Docker

Executado quando:

- Acontece push na branch principal
- Em pull requests

Foi implementado ao fazer push na branch principal do projeto apenas como demonstração, mas seu uso em ambiente produtivo seria tanto no momento onde o código é mergeado em uma branch de ambiente, quanto no momento da abertura de pull requests (sem geração de build da imagem docker, neste caso).

## Infrastructure as Code (Terraform)

Infraestrutura modelada com Terraform para AWS presente no projeto:

### Recursos:

- Amazon ECR (repositório de imagens Docker)
- Amazon ECS (Para executar a imagem docker do registry em FARGATE)

### Objetivo:

Demonstrar conhecimento em:
- Infraestrutura como código
- Container registry
- Base para deploy cloud-native
- Entendimento de provisionamento de recursos para possibilitar a entrega contínua no momento de subida de novas releases

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
