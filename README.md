# Credit Simulator API

API RESTful para simulação de empréstimos baseada em valor, prazo e idade do cliente.

## Descrição

A API Credit Simulator permite aos usuários simular empréstimos e visualizar as condições de pagamento com base no valor solicitado, taxa de juros e prazo de pagamento. A taxa de juros varia dependendo da faixa etária do cliente.

## Funcionalidades

*   **Simulação de Empréstimo:** Calcula o valor total a ser pago, as parcelas mensais e os juros totais com base em:
    *   Valor do empréstimo.
    *   Data de nascimento do cliente.
    *   Prazo de pagamento em meses.
*   **Taxa de Juros Dinâmica:** A taxa de juros é calculada com base na idade do cliente:
    *   Ata 60 anos: 2% ao ano.
    *   Acima de 60 anos: 4% ao ano.
*   **Validação de Entrada:** Valida o valor do empréstimo (deve ser positivo), data de nascimento (deve ser no passado) e prazo de pagamento (deve ser positivo).
*   **Tratamento de Erros:** Retorna códigos de status HTTP e mensagens apropriadas para entradas inválidas e erros internos do servidor.
*   **Documentação Swagger/OpenAPI:** Documentação interativa da API.

## Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot 3.4.4
    *   Spring Web
    *   Spring Validation
*   **Maven**
*   **Lombok**
*   **JUnit 5 & Mockito** (Testes)
*   **springdoc-openapi** (Swagger/OpenAPI)
*   **Docker**

## Como Começar

### Pré-requisitos

*   **Java Development Kit (JDK) 17** ou superior.
*   **Apache Maven**
*   **Git**
*   **Docker** (Para execução em container)

### Instalação e Build

1.  **Clone o repositório:**
    git clone https://github.com/aleffalves/credit-simulator.git cd credit-simulator

2.  **Compile e empacote o projeto usando Maven:**
    mvn clean package

### Executando a Aplicação

Você pode executar a aplicação de várias formas:

1.  **Usando Maven:**
    mvn spring-boot:run

2.  **Executando o JAR:**
   java -jar target/credit-simulator-0.0.1-SNAPSHOT.jar

3.  **Usando Docker:**
   docker-compose up -d

A aplicação estará disponível em `http://localhost:8080/api` por padrão.

### Acessando a UI do Swagger/OpenAPI

Após iniciar a aplicação, você pode acessar a documentação interativa da API em: http://localhost:8080/api/swagger-ui/index.html

## Endpoints da API

### Simular Empréstimo

*   **URL:** `/load-simulator`
*   **Método:** `POST`
*   **Content-Type:** `application/json`

#### Exemplo de Corpo da Requisição
    json { "loanAmount": 15000.00, "dateOfBirth": "1988-10-25", "paymentTermMonths": 24 }

#### Exemplo de Resposta de Sucesso (200 OK)
    json { "monthlyInstallment": 644.14, "totalAmountPayable": 15459.36, "totalInterestPaid": 459.36 }
    
## Estrutura do Projeto

O projeto segue uma arquitetura em camadas, comum em aplicações Spring Boot:

*   **Camada de Controller:**
    *   Lida com requisições e respostas HTTP.
    *   Usa anotações `@RestController`, `@RequestMapping`, `@PostMapping`, `@RequestBody`, e `@Valid`.
    *   Valida requisições de entrada e retorna HTTP 400 para requisições inválidas.
    *   Usa `ResponseEntity` do Spring para gerenciar códigos de status e respostas HTTP.
    *   Usa anotações `@Tag`, `@Operation`, `@ApiResponse`, e `@Schema` para documentação OpenAPI.
    *   Injeta o `LoadSimulatorService` para lógica de negócio usando `@Autowired`.
*   **Camada de Serviço:**
    *   Contém a lógica de negócio principal para a simulação de empréstimo.
    *   Lida com o cálculo de idade, taxa de juros, valor das parcelas, valores totais e juros totais.
    *   Define e usa constantes para taxas de juros e faixas etárias, e usa tratamento de exceções.
*   **Camada de Domínio:**
    *   Contém DTOs (`LoadSimulatorRequest`, `LoadSimulatorResponse`).
    *   DTOs são usados para transferir dados entre o Controller e o Service.
    *   DTOs usam anotações `@Schema` para documentação.
    *   DTOs usam anotações de validação (`@NotNull`, `@Positive`, `@Past`) para garantir a integridade dos dados.
*   **Camada de Teste:**
    *   Usa `MockMvc` para simular requisições e respostas HTTP.
    *   Usa anotações `@SpringBootTest` e `@AutoConfigureMockMvc`.
    *   Usa `ObjectMapper` para lidar com serialização e desserialização JSON.
    *   Usa `@InjectMocks` para injetar dependências na classe de teste.
