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
