# Raízes do Nordeste

Este repositório contém a API REST para a rede de lanchonetes **Raízes do Nordeste**. A arquitetura foi projetada com foco em alta escalabilidade, isolamento de regras de negócio

## Conteúdos

- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Endpoints](#endpoints)
- [Informações Insomnia](#insomnia)
- [Fluxo Principal](#fluxo-principal)

## Tecnologias

- Java 17
- Spring Boot 3.5.16
- PostgreSQL 16
- Docker & Docker Compose

## Requisitos

- Antes de iniciar a instalação, certifique-se de possuir o docker e docker compose instalado em sua máquina https://docs.docker.com/engine/install/
- Ter as portas 8080 e 5432 liberadas no seu sistema.

## Instalação

O projeto está totalmente configurado para rodar em containers isolados, garantindo que o banco de dados relacional e a aplicação Java Spring inicializem em perfeita sincronia.

### Passo 1: Clonar o repositório e acessar o diretório

```bash
git clone https://github.com/jpedro-cf/raizes-do-nordeste.git
cd raizes-do-nordeste
```

### Passo 2: Configurar as Variáveis de Ambiente

O projeto utiliza um arquivo `.env` para gerenciar credenciais e parâmetros operacionais de forma segura. Crie uma cópia do arquivo de exemplo:

```bash
cp .env.example .env
```

Abra o arquivo `.env` gerado e ajuste as propriedades caso seja necessário.

### Passo 3: Inicializar a Infraestrutura

Execute o comando abaixo para construir as imagens e subir os containers em segundo plano:

```bash
docker compose up -d --build
```

O comando irá:

1. Inicializar o container do banco de dados e aguardar que ele esteja pronto para receber conexões (_healthcheck_).
2. Compilar a aplicação Java Spring dentro de um container de compilação temporário.
3. Inicializar a API Spring Boot em http://localhost:8080, expondo os endpoints para consumo.
4. Ao iniciar, a aplicaçao cria automaticamente um usuário ADMIN, para realizar login com essa conta, utilize o email `admin@email.com` e a senha definida na variável de ambiente `ADMIN_PASSWORD`

Para derrubar o ambiente e manter a persistência dos dados:

```bash
docker compose down
```

### ⚙️ Variáveis de Ambiente (`.env.example`)

O arquivo `.env.example` serve como o mapa de configuração da aplicação.

```env
DB_USER="user"
DB_PASSWORD="password"
DB_NAME="raizes_nordeste"
ADMIN_PASSWORD="password"
JWT_SECRET="2bd3855646db537030105d119dbdf37d39b16fb174dafbaa1424d8441b1671a9"
```

Observação: Há também a variável "JWT_SECRET" que é usada para verificar a integridade dos tokens JWT na aplicação. Essa variável deve ser uma chave secreta de 256 bits (32 bytes). Para gerá-la, é recomendado usar o comando OpenSSL caso tenha instalado na sua máquina:

```bash
openssl rand -hex 32
```

Caso não queira ter problemas, pode deixar como está definido mesmo.

## Endpoints

Com base no arquivo de ambiente de testes `insomnia.json`, os contratos de dados e rotas disponíveis na API estão distribuídos conforme as tabelas a seguir.

| Método | Rota | Perfil Autorizado | Descrição |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Público | Autentica o usuário com e-mail e senha |
| `POST` | `/auth/register` | Público | Cadastra um novo usuário. |
| `POST` | `/units` | `ADMIN` | Cria uma unidade da rede. |
| `DELETE` | `/units/{id}` | `ADMIN` | Deleta uma unidade da rede. |
| `GET` | `/units` | Público | Lista as unidades da rede. |
| `GET` | `/units/{id}` | Público | Lista uma unidade da rede. |
| `POST` | `/products` | `ADMIN` | Cadastrar um produto. |
| `DELETE` | `/products/{id}` | `ADMIN` | Deleta um produto. |
| `GET` | `/products` | Publico | Lista os produtos disponíveis. |
| `GET` | `/products/{id}` | Publico | Lista os produtos disponíveis. |
| `POST` | `/stock` | `ADMIN` | Adicionar um produto ao estoque de uma unidade. |
| `DELETE` | `/stock/{stockItemId}` | `ADMIN` | Remover um produto do estoque de uma unidade. |
| `PATCH` | `/stock/{stockItemId}` | `ADMIN` | Atualizar um produto do estoque de uma unidade. |
| `GET` | `/stock/{stockId}` | Público | Listar os produtos disponíveis de uma unidade. |
| `POST` | `/orders` | `ADMIN`, `CUSTOMER` | Criar um pedido. |
| `PATCH` | `/orders/{id}/status` | `ADMIN` | Atualizar status de um pedido. |
| `PATCH` | `/orders/{id}/cancel` | `ADMIN` | Cancelar um pedido. |
| `GET` | `/orders` | `ADMIN` | Listar todos os pedidos. |
| `GET` | `/orders/{id}` | `ADMIN`, `CUSTOMER` | Listar um pedido (Se o usuário for CUSTOMER e o pedido não pertence a ele, retorna 403). |
| `POST` | `/payments` | `ADMIN`, `CUSTOMER` | Processar pagamento de um pedido. |
| `GET` | `/payments` | `ADMIN` | Listar todos os pagamentos. |
| `GET` | `/loyalty` | `ADMIN`, `CUSTOMER` | Listar os histórico de pontos de fidelidade. |

## Insomnia

1. Abra o seu **Insomnia Client**.
2. No canto superior direito, clique em **Import** -> **From File**.
3. Selecione o arquivo `insomnia.json` localizado na raiz deste projeto.
4. Configure as variáveis `url` e `token` caso já não esteja configurado, recomendado configurar para buscar automaticamente o token dentro de `/auth/login`.

## Fluxo Principal

Certifique-se de que pelo menos uma unidade, pelo menos um produto e pelo menos um estoque com esse produto já foram registrados anteriormente.

1. Registrar usuário -> `/auth/register` 
2. Fazer login -> `/auth/login` (guarde o token)
3. Criar pedido -> `/orders` (use o token no header)
4. Processar pagamento -> `/payments`