# EasyTrip

O projeto EasyTrip tem o objetivo de facilitar a jornada do usuário ao procurar e realizar hospedagens. Para os que desejam encontrar uma boa oportunidade, o sistema conta com a possibilidade de busca de opções anunciadas a partir dos menores preços, e para os que desejam anunciar e gerenciar suas reservas, é possível realizar buscas para melhor organizar a gestão do imóvel. 

## Testes Unitários e de Integração

- Para este projeto, foi obtido 100% de coverage nos testes unitários.
  
- Muitas das funcionalidades deste projeto envolvem diretamente o banco de dados, não havendo grandes manipulações na camada Service. Portanto, foram realizados testes de integração (de maneira simples, porém completa) utilizando a plataforma Postman. A Collection com os testes está disponível junto do projeto, no arquivo "Gabriel Souza - Reset TCC.postman_collection". Os testes contemplam todos os cenários que envolvem consulta ao banco, incluindo exclusões lógicas, recursos duplicados, atualizações de usuários, sobreposição de reservas, entre outros. 
  - Para executar a collection, basta importá-la no Postman, clicar na pasta "TESTES", e em seguida clicar em "Run". 

## Explicações Adicionais sobre o BackEnd

- Além dos domínios mencionados na documentação, salienta-se que as entidades Imóvel e Anúncio possuem o atributo booleano "ativo". Ao fazer o POST dessas entidades, o atributo é settado para True. Em caso de realizar a exclusão de algum destes, é settado para False, não sendo possível voltar a ser True.

- O domínio Reserva possui o atributo booleano statusReserva, e pode ser modificado através da chamada ao método setStatusReserva(). Este método não reserva parâmetro, pois sua função é verificar se a reserva está Ativa ou não, através do seu status de Pagamento. Estará ativa se o pagamento estiver como "PENDENTE/PAGO", e inativa se estiver como "CANCELADA/ESTORNADA". Portanto, todas os métodos que alteram o statusPagamento devem chamar este método setStatusReserva(), para poder atualizar o atributo StatusReserva. 

- Ao realizar a exclusão lógica de um anúncio, e em seguida realizar a pesquisa por uma Reserva que tenha este anúncio, o objeto Anúncio será retornado normalmente dentro do objeto Reserva, mesmo tendo sido excluído logicamente, pois esta informação é importante dentro do contexto da Reserva realizada. Portanto, este é o único caso em que os anúncios excluídos continuam sendo retornados em pesquisas.

## Bugs Conhecidos 

- ConsultarReservaPorSolicitante. Este método retorna apenas as reservas que estão 100% contempladas no período informado, portanto, qualquer reserva que tenha uma parte de seu intervalo fora do período pesquisado, não será mostrada. 

## Próximos Passos

- Criar o front-end em React 
  - Formulário de cadastros
  - Tabelas para visualizar usuários cadastrados

## Requisitos para rodar o Projeto

- Java 8+
- Instalar as dependências do arquivo POM.
- Maven

## Estrutura de Classes do Domínio

- Endereco
    - id;
    - cep;
    - logradouro;
    - numero;
    - complemento;
    - bairro;
    - cidade;
    - estado;
- Usuario
    - id;
    - nome;
    - email;
    - senha;
    - cpf;
    - dataNascimento;
    - Endereco endereco;
- CaracteristicaImovel
    - id;
    - descricao;
- Imovel
    - id;
    - identificacao;
    - TipoImovel tipoImovel
        - Apartamento
        - Casa
        - Hotel
        - Pousada
    - Endereco endereco;
    - Usuario proprietario;
    - List \<CaracteristicaImovel\> caracteristicas;
- FormaPagamento
    - Cartão de Crédito
    - Cartão de Débito
    - Pix
    - Dinheiro
- Anuncio
    - id;
    - TipoAnuncio tipoAnuncio;
        - Completo
        - Quarto
    - Imovel imovel;
    - Usuario anunciante;
    - valorDiaria;
    - List\<FormaPagamento\> formasAceitas;
    - descricao;
- Periodo
    - dataHoraInicial;
    - dataHoraFinal;
- Reserva
    - id;
    - Usuario solicitante;
    - Anuncio anuncio;
    - Periodo periodo;
    - quantidadePessoas;
    - dataHoraReserva;
    - Pagamento pagamento;
- Pagamento
    - valorTotal;
    - FormaPagamento formaEscolhida;
    - StatusPagamento statusPagamento;
        - Pendente
        - Pago
        - Estornado
        - Cancelado
    
# Funcionalidades

## 1. Usuário

### 1.1. Cadastro de Usuário
  - Assinatura
    - `POST /usuarios`
  - Parâmetros de Entrada: 
    - Usuario
        - nome*
        - email*
        - senha*
        - cpf*
        - dataNascimento*
        - Endereco endereco
            - cep*
            - logradouro*
            - numero*
            - complemento
            - bairro*
            - cidade*
            - estado*   
  - Saída esperada em caso de sucesso:
    - Status: `201 CREATED`
    - Body: 
        - Objeto `Usuario` contendo todos os campos cadastrados exceto o campo senha
  - Regras
    - Não deve ser possível cadastrar mais de um usuário com o mesmo E-Mail
        - Caso já exista outro usuário com o e-mail informado, lança uma exceção que retorna o status 400 e uma mensagem informando o problema     
        - Ex: `Já existe um recurso do tipo Usuario com E-Mail com o valor 'teste1@teste.com'.`
    - Não deve ser possível cadastrar mais de um usuário com o mesmo CPF
        - Caso já exista outro usuário com o CPF informado, lança uma exceção que retorna o status 400 e uma mensagem informando o problema     
          - Ex: `Já existe um recurso do tipo Usuario com CPF com o valor '12345678900'.`
    - O campo CEP aceita somente o formato 99999-999.
      - Caso seja informado um CEP com outro formato, retorna um erro com a mensagem: `O CEP deve ser informado no formato 99999-999.`
    - O campo CPF aceita somente o formato 99999999999.
      - Caso seja informado um CPF com outro formato, retorna um erro com a mensagem: `O CPF deve ser informado no formato 99999999999.`    
    - Caso seja informado um endereço, então os campos marcados com * são obrigatórios.

### 1.2. Listar usuários
  - Assinatura
    - `GET /usuarios`
  - Parâmetros de Entrada
    - N/A    
  - Saída esperada em caso de sucesso
    - Status: `200 SUCCESS`
    - Body
        - List\<Usuario>
            - Usuario
                - id
                - nome
                - email
                - cpf
                - dataNascimento
                - Endereco endereco
                    - id
                    - cep
                    - logradouro
                    - numero
                    - complemento
                    - bairro
                    - cidade
                    - estado
  - Regras
    - O campo senha não é retornado, porém o objeto a ser retornado é o `Usuario`  
    - Listar os usuários com paginação, em ordem alfabética pelo nome por padrão    

### 1.3. Buscar um usuário por id
  - Assinatura
    - `GET /usuarios/{idUsuario}`
  - Parâmetros de Entrada: 
    - idUsuario (path parameter)      
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: 
        - Usuario
            - id
            - nome
            - email
            - cpf
            - dataNascimento
            - Endereco endereco
                - id
                - cep
                - logradouro
                - numero
                - complemento
                - bairro
                - cidade
                - estado    
  - Comportamentos:
    - A aplicação obtem o usuário através do id informado.
        - Caso nenhum usuário seja localizado, lança uma exceção que retorna o status 404       
          - Ex: `Nenhum(a) Usuario com Id com o valor '999' foi encontrado.`
  
### 1.4. Buscar um usuário por cpf
  - Assinatura
    - `GET /usuarios/cpf/{cpf}`
  - Parâmetros de Entrada: 
    - cpf (path parameter)      
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body:
        - Usuario
            - id
            - nome
            - email
            - cpf
            - dataNascimento
            - Endereco endereco
                - id
                - cep
                - logradouro
                - numero
                - complemento
                - bairro
                - cidade
                - estado     
  - Comportamentos:
    - A aplicação obtem o usuário através do CPF informado.
        - Caso nenhum usuário seja localizado, lança uma exceção que retorna o status 404        
          - Ex: `Nenhum(a) Usuario com CPF com o valor '01245487848' foi encontrado.` 

### 1.5. Alterar um usuário
  - Assinatura
    - `PUT /usuarios/{id}`
  - Parâmetros de Entrada: 
    - id (path parameter)
    - AtualizarUsuarioRequest
        - nome*
        - email*
        - senha*
        - dataNascimento*
        - Endereco endereco
            - cep*
            - logradouro*
            - numero*
            - complemento
            - bairro*
            - cidade*
            - estado*
  - Saída em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: 
        - Objeto `Usuario` contendo todos os campos exceto o campo senha
  - Regras
    - Os atributos marcados com um "*" são obrigatórios.
    - Não é possível alterar o CPF de um usuário já cadastrado
    - A aplicação obtem o usuario através do Id informado.
        - Caso nenhum usuario seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema        
          - Ex: `Nenhum(a) Usuario com Id com o valor '999' foi encontrado.`
    - Não deve ser possível cadastrar mais de um usuário com o mesmo E-Mail
        - Caso já exista outro usuário com o e-mail informado, deve lançar uma exceção que retorne o status 400 e uma mensagem informando o problema        
          - Ex: `Já existe um recurso do tipo Usuario com E-Mail com o valor 'teste1@teste.com'.`
    - O campo CEP aceita somente o formato 99999-999.
      - Caso seja informado um CEP com outro formato, será retornado um erro com a mensagem: `O CEP deve ser informado no formato 99999-999.`   

## 2. Imóvel

### 2.1. Cadastro de Imóvel
  - Assinatura
    - `POST /imoveis`
  - Parâmetros de Entrada: 
    - CadastrarImovelRequest
        - TipoImovel tipoImovel*
        - Endereco endereco*
            - cep*
            - logradouro*
            - numero*
            - complemento
            - bairro*
            - cidade*
            - estado*   
        - identificacao*
        - idProprietario*
        - List \<CaracteristicaImovel\> caracteristicas
            - CaracteristicaImovel
                - descricao 
  - Saída esperada em caso de sucesso:
    - Status: `201 CREATED`
    - Body: Objeto Imovel contendo todos os campos
  - Comportamentos:
    - A aplicação obtem o usuario através do Id informado, para poder vincular ao Imovel antes de persistir.
        - Caso nenhum seja usuario seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema        
          - Ex: `Nenhum(a) Usuario com Id com o valor '999' foi encontrado.`
  - Regras
    - Os atributos marcados com um "*" são obrigatórios.
    - O campo "identificacao" serve pra que o proprietário identifique o imóvel textualmente. Algo como "Casa da praia".
    - O campo CEP deve aceitar somente o formato 99999-999, como na clase Usuário.    

### 2.2. Listar imóveis
  - Assinatura
    - `GET /imoveis`
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: 
        - Lista de Imóveis
            - Exibe todos os atributos do Imóvel, bem como os atributos de Endereço, proprietário e características.  
            - Lista os imóveis com paginação, em ordem alfabética pelo campo identificacao por padrão

### 2.3. Listar imóveis de um proprietário específico
  - Assinatura
    - `GET /imoveis/proprietarios/{idProprietario}`
  - Parâmetros de Entrada: 
    - idProprietario (path parameter)      
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body:
        - Lista de Imóveis
            - Exibe todos os atributos do Imóvel, bem como os atributos de Endereço, proprietário e características.    
  - Comportamentos:
    - O sistema deve retornar somente os imóveis do proprietário informado
    - Lista os imóveis com paginação, em ordem alfabética pelo campo identificacao por padrão

### 2.4. Buscar um imóvel por id
  - Assinatura
    - `GET /imoveis/{idImovel}`
  - Parâmetros de Entrada: 
    - idImovel (path parameter)      
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: Objeto Imóvel
        - Exibe todos os atributos do Imóvel, bem como os atributos de Endereço, proprietário e características.    
  - Comportamentos:
    - A aplicação obtem o Imóvel através do Id informado.
        - Caso nenhum seja localizado, deve lança uma exceção com o status 404 e uma mensagem informando o problema        
          - Ex: `Nenhum(a) Imovel com Id com o valor '999' foi encontrado.`

### 2.5. Excluir um imóvel
  - Assinatura
    - `DELETE /imoveis/{idImovel}`
  - Parâmetros de Entrada: 
    - idImovel (path parameter)      
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: vazio
  - Comportamentos:
    - A aplicação exclui o Imóvel através do Id informado.
      - Caso nenhum seja localizado, deve lançar uma exceção com o status 404 e uma mensagem informando o problema      
        - Ex: `Nenhum(a) Imovel com Id com o valor '999' foi encontrado.`
  - Não é possível excluir um imóvel que possua algum anúncio
    - Caso o imóvel possuia anúncios, o sistema lança uma exceção com o status 400 e uma mensagem informando o problema
      - Mensagem: `Não é possível excluir um imóvel que possua um anúncio.`

## 3. Anúncio

### 3.1 Anunciar imóvel
  - Assinatura
    - `POST /anuncios`
  - Parâmetros de Entrada: 
    - CadastrarAnuncioRequest
      - idImovel*
      - idAnunciante*
      - TipoAnuncio tipoAnuncio*
      - valorDiaria*
      - formasAceitas*
      - descricao*
  - Saída esperada em caso de sucesso:
    - Status: `201 CREATED`
    - Body: Anuncio
  - Comportamentos:
    - A aplicação obtem o Imóvel através do Id informado.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema           
          - Ex: `Nenhum(a) Imovel com Id com o valor '999' foi encontrado.`
    - A aplicação obtem o anunciante (Usuario) através do Id informado.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema           
          - Ex: `Nenhum(a) Usuario com Id com o valor '999' foi encontrado.`
  - Regras
    - Os atributos marcados com um "*" são obrigatórios.
    - Não é possível criar mais de um anúncio para o mesmo imóvel
      - Caso já exista algum anúncio para o mesmo imóvel, a aplicação lança uma exceção que retorna o código 400 e uma mensagem.        
        - Ex: `Já existe um recurso do tipo Anuncio com IdImovel com o valor '12'.`

### 3.2. Listar anúncios
  - Assinatura
    - `GET /anuncios`
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: Lista de Anuncio
        - Exibe todos os atributos do Anuncio, bem como os atributos de Imóvel e anunciante.
  - Comportamentos:
    - Deve listar todos os anúncios armazenados no banco de dados  
    - Lista os anúncios com paginação, em ordem pelo valor da diária (valores menores primeiro)

### 3.3. Listar anúncios de um anunciante específico
  - Assinatura
    - `GET /anuncios/anunciantes/{idAnunciante}`
  - Parâmetros de Entrada: 
    - idAnunciante (path parameter)    
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: Lista de Anuncio
        - Exibe todos os atributos do Anuncio, bem como os atributos de Imóvel e anunciante.
  - Comportamentos:
    - O sistema retorna somente os anúncios que tenham sido realizados pelo anunciante informado 
    - Lista os anúncios com paginação, em ordem pelo valor da diária (valores menores primeiro)

### 3.4. Excluir um anúncio
  - Assinatura
    - `DELETE /anuncios/{idAnuncio}`
  - Parâmetros de Entrada: 
    - idAnuncio (path parameter)      
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: vazio
  - Comportamentos:
    - A aplicação obtem o Anúncio através do Id informado.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema        
          - Ex: `Nenhum(a) Anuncio com Id com o valor '999' foi encontrado.`
    - O anúncio não é removido do banco de dados. É feita uma exclusão lógica do registro. O registro permaneçe na base mas não é mais considerado em nenhuma outra busca.

## 4. Reserva

### 4.1. Realizar uma reserva
  - Assinatura
    - `POST /reservas`
  - Parâmetros de Entrada: 
    - CadastrarReservaRequest
      - idSolicitante*
      - idAnuncio*
      - Periodo periodo*
      - quantidadePessoas*
  - Saída esperada em caso de sucesso:
    - Status: `201 CREATED`
    - Body: 
      - InformacaoReservaResponse
        - idReserva
        - DadosSolicitanteResponse solicitante
          - id
          - nome
        - quantidadePessoas
        - DadosAnuncioResponse anuncio
          - id
          - Imovel
          - Usuario anunciante
          - List\<FormaPagamento\> formasAceitas
          - descricao
        - Periodo periodo (e todos os seus atributos)
        - Pagamento pagamento (e todos os seus atributos)
  - Comportamentos:
    - O formato da data esperado no período é: `yyyy-MM-dd HH:mm:ss`
    - É definida e registrada a Data/Hora do momento em que a reserva foi realizada
    - A aplicação aceita somente reservas iniciando as 14:00 e finalizando as 12:00. Caso seja informado um horário diferente, a aplicação sobrescreve a informação e considera estes horários arbitrariamente.    
    - Calcula o valor total para o pagamento, baseado no valor da diária e na quantidade de diárias solicitadas da reserva
    - A aplicação obtem o usuario solicitante através do Id informado, para poder vincular à reserva antes de persistir.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema        
          - Ex: `Nenhum(a) Usuario com Id com o valor '999' foi encontrado.`
    - A aplicação obtem o anuncio através do Id informado, para poder vincular à reserva antes de persistir.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema        
          - Ex: `Nenhum(a) Anuncio com Id com o valor '999' foi encontrado.`
  - Regras
    - Os atributos marcados com um "*" são obrigatórios.
    - Não é possível reservar um imóvel com um período cuja data final seja menor que a data inicial
      - Caso ocorra essa situação, a aplicação lança uma exceção com o status 400 com uma mensagem informando o problema
        - Mensagem: `Período inválido! A data final da reserva precisa ser maior do que a data inicial.`
    - Não é possível reservar um imóvel com um período cuja diferença entre a data final e inicial seja menor que 1 dia
      - Caso ocorra essa situação, a aplicação lança uma exceção com o status 400 com uma mensagem informando o problema
        - Mensagem: `Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.`
    - Não é possível reservar um imóvel cujo solicitante seja o mesmo anunciante
      - Caso ocorra essa situação, a aplicação lança uma exceção com o status 400 com uma mensagem informando o problema
        - Mensagem: `O solicitante de uma reserva não pode ser o próprio anunciante.`
    - Não é possível reservar um imóvel que já possua uma reserva ativa no mesmo período.         
        - O conceito de reserva "ativa" consiste em uma reserva que não tenha sido estornada nem cancelada.
        - Caso ocorra essa situação, a aplicação lança uma exceção com o status 400 com uma mensagem informando o problema
          - Mensagem: `Este anuncio já esta reservado para o período informado.`
    - Caso a reserva seja de um Hotel, o número mínimo de pessoas é 2
        - Caso seja informado um número inferior, lança uma exceção com o status 400 com uma mensagem informando o problema   
          - Ex: `Não é possivel realizar uma reserva com menos de 2 pessoas para imóveis do tipo Hotel`
    - Caso a reserva seja de uma Pousada, o número mínimo de diárias é 5
        - Caso seja informado um período inferior, lança uma exceção com o status 400 com uma mensagem informando o problema   
          - Ex: `Não é possivel realizar uma reserva com menos de 5 diárias para imóveis do tipo Pousada`

### 4.2. Listar reservas de um solicitante específico
  - Assinatura
    - `GET /reservas/solicitantes/{idSolicitante}`
  - Parâmetros de Entrada: 
    - idSolicitante (path parameter)     
    - Periodo periodo (opcional)
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: List de Reserva
      - Exibe todos os atributos da Reserva, bem como os atributos de Anuncio e Soliciante.
  - Comportamentos:
    - O sistema retorna somente as reservas do solicitante informado    
    - Caso nenhum período seja informado, o sistema retorna todas as reservas. Considere sempre o período completo, se for informada apenas uma das duas datas, o sistema vai considerar que nenhuma data foi informada.
    - Caso seja informado um período, o sistema deve retornar somente as reservas cujas datas estejam dentro do período informado.
    - Lista as reservas com paginação, ordenando pela data do fim da reserva(Datas maiores primeiro).

### 4.3. Listar reservas de um anunciante específico
  - Assinatura
    - `GET /reservas/anuncios/anunciantes/{idAnunciante}`
  - Parâmetros de Entrada: 
    - idAnunciante (path parameter)    
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: List de Reserva
      - Exibe todos os atributos da Reserva, bem como os atributos de Anuncio e Soliciante.
  - Comportamentos:
    - O sistema retorna somente as reservas do anunciante informado    
    - Lista as reservas com paginação, ordenando pela data do fim da reserva(Datas maiores primeiro).

### 4.4. Pagar reserva
  - Assinatura
    - `PUT /reservas/{idReserva}/pagamentos`
  - Parâmetros de Entrada: 
    - idReserva (path parameter)
    - formaPagamento
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: vazio
  - Comportamentos:
    - A aplicação obtem a Reserva através do Id informado.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema           
          - Ex: Nenhum(a) Reserva com Id com o valor '999' foi encontrado.        
    - Altera o status do Pagamento da reserva para "Pago"
  - Regras
    - Não é possível realizar um pagamento com uma forma de pagamento que não seja aceita pelo anúncio.
      - Caso isso aconteça, lança uma exceção com o status 400 e uma mensagem informando o problema         
          - Ex: `O anúncio não aceita PIX como forma de pagamento. As formas aceitas são DINHEIRO, CARTAO_CREDITO.`
    - Não é possível realizar o pagamento de uma reserva paga, estornada ou cancelada
      - Caso esteja em algum desses status, lança uma exceção com o status 400 e uma mensagem informando o problema 
        - Mensagem: `Não é possível realizar o pagamento para esta reserva, pois ela não está no status PENDENTE.`

### 4.5. Cancelar uma reserva
  - Assinatura
    - `PUT /reservas/{idReserva}/pagamentos/cancelar`
  - Parâmetros de Entrada: 
    - idReserva (path parameter)
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: vazio
  - Comportamentos:
    - A aplicação obtem a Reserva através do Id informado.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema          
          - Ex: `Nenhum(a) Reserva com Id com o valor '999' foi encontrado.`
    - Altera o status do Pagamento da reserva para "Cancelado"
  - Regras
    - Não é possível realizar o cancelamento de uma reserva paga, estornada, cancelada
      - Caso esteja em algum desses status, lança uma exceção com o status 400 e uma mensagem informando o problema    
        - Mensagem: `Não é possível realizar o cancelamento para esta reserva, pois ela não está no status PENDENTE.`

### 4.6. Estornar reserva
  - Assinatura
    - `PUT /reservas/{idReserva}/pagamentos/estornar`
  - Parâmetros de Entrada: 
    - idReserva (path parameter)
  - Saída esperada em caso de sucesso:
    - Status: `200 SUCCESS`
    - Body: vazio
  - Comportamentos:
      - A aplicação obtem a Reserva através do Id informado.
        - Caso nenhum seja localizado, lança uma exceção com o status 404 e uma mensagem informando o problema            
          - Ex: `Nenhum(a) Reserva com Id com o valor '999' foi encontrado.`
    - Altera o status do Pagamento da reserva para "Estornado"
    - A forma de pagamento escolhida deve ser removida
  - Regras
    - Não é possível estornar o pagamento de uma reserva pendente, estornada, cancelada
      - Caso esteja em algum desses status, lança uma exceção com o status 400 e uma mensagem informando o problema    
        - Mensagem: `Não é possível realizar o estorno para esta reserva, pois ela não está no status PAGO.`

### 5.1 Imagem de avatar para usuários

A aplicação realiza a chamada à uma API externa que retorna essa URL e então vincula ao usuário que estiver sendo criado. A integração é feita com Feign Client

- GET https://some-random-api.ml/img/dog
- Retorno:
  ```json
  {
      "link": "https://i.some-random-api.ml/kC1VFB2J2F.jpg"
  }
  ```

Referências: 
- https://cloud.spring.io/spring-cloud-openfeign/reference/html/
- https://www.baeldung.com/spring-cloud-openfeign

--- 

- A paginação funciona com valores padrões (através da anotação `@PageableDefault`). Caso nenhum parâmetro seja informado na url, serão assumidos os valores padrões do `@PageableDefault`. Para que seja possível o cliente navegar nas páginas, são necessários informar alguns parâmetros na url:
  - `page`: Permite definir em qual página estamos. Inicia em 0 (zero) e não pode ser negativo.
  - `size`: define a quantidade de itens por página. Precisa ser maior que zero.
  - `sort`: define o atributo que será usado para ordenar e o sentido da ordenação (crescente = `asc` e descrescente = `desc`) 
  - Exemplos: 
    - http://localhost:8080/usuarios - Vai utilizar as informações padrões do `@PageableDefault`
    - http://localhost:8080/usuarios?page=1&size=10&sort=nome,desc - Vai exibir até 10 itens da segunda página e ordenar pelo nome de Z para A
    - http://localhost:8080/usuarios?page=2&size=4&sort=nome,asc - Vai exibir até 4 itens da terceira página e ordenar pelo nome de A para Z
    - http://localhost:8080/usuarios?page=1&size=10&sort=nome,desc&sort=dataNascimento,desc - Vai exibir até 10 itens da segunda página e ordenar pelo nome de Z para A e depois ainda ordenar pela data de nascimento em ordem descrente

  - Referência: 
    - [5.2. Customizing the Paging Parameters](https://www.baeldung.com/spring-data-web-support#2-customizing-the-paging-parameters)
    - [Pagination and Sorting using Spring Data JPA](https://www.baeldung.com/spring-data-jpa-pagination-sorting)

- Para acessar o Swagger, utilize a URL: http://localhost:8080/swagger-ui/