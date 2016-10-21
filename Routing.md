Os args são recebidos e tratados pela classe CommandParser, que os divide em:
	* Uma string method, que contém POST ou GET, ou poderá conter outros valores em versões futuras.
	* Uma array de strings que contém o path, separado pelo caracter '/'.
	* Um Hashmap params, cujas chaves correspondem ao que vem escrito antes do '=' e os valores correspondem ao que vem depois.
Cada elemento do hashmap corresponde a um elemento dos parâmetros de entrada separado pelo caracter '&'. As strings value também são tratadas para que os '+'s sejam traduzidos para ' '.
O objecto CommandParser é depois passado para o Router, que irá procurar numa LinkedList de comandos implementados o comando que corresponde aos dados do CommandParser.
Os comandos são comparados posição a posição das respetivas arrays (uma para o comando a procurar, e uma para cada comando da lista).
Se existir inigualdade é verificado se esse elemento do path corresponde a um id ({cid}, {tid} ou {lid}), e se for, o seu valor é adicionado ao hashmap params com a chave correspondente ao tipo de id.
Depois do comando ser encontrado, é chamado o seu execute.