# Sistema Simplificado da Shopee
Este projeto consiste em um sistema simples que replica a Shopee, desenvolvido como trabalho final para a disciplina de Programação Orientada a Objetos (POO) do curso de Análise e Desenvolvimento de Sistemas (ADS).

## Estrutura de Diretórios

A estrutura do projeto segue rigorosamente a arquitetura em camadas solicitada, organizada sob o pacote base 'com.shopee':

```text
src/
└── main/
    ├── java/
    │   └── com/shopee/
    │       ├── model/          # Entidades (Classes fundamentais do sistema)
    │       ├── dao/            # Data Access Object (Persistência e acesso ao banco)
    │       ├── service/        # Regras de negócio e lógica de processamento
    │       ├── controller/     # Controladores para intermediação entre View e Service
    │       ├── view/           # Interfaces do usuário
    │       │   ├── console/    # Implementação em modo texto
    │       │   └── swing/      # Implementação em interface gráfica
    │       ├── util/           # Classes utilitárias (Conexão, Log e Validação)
    │       ├── pattern/        # Padrões de Projeto implementados
    │       │   ├── factory/    # Criação de objetos
    │       │   ├── strategy/   # Algoritmos de pagamento
    │       │   ├── builder/    # Construção de pedidos complexos
    │       │   └── observer/   # Sistema de notificações
    │       └── App.java        # Ponto de entrada (Main) do sistema
    └── resources/              # Arquivos de configuração e scripts SQL