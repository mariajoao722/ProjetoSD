# ProjetoSD

Coloque-se no diretório superior (ProjetoSD/) e compile e run os projetos da seguinte maneira.

## RING

compilar com o poisson
`$ javac -cp ds/assign/ring/ ds/assign/ring/*.java`

correr o Peer
`$ java ds.assign.ring.Peer <ip_peer> <ip_next_peer>`

Exemplos:
  - ``` java -cp .:ds/assign/ring ds.assign.ring.Peer 12345 12346```
  - ``` java -cp .:ds/assign/ring ds.assign.ring.Peer 12346 12347```
  - ``` java -cp .:ds/assign/ring ds.assign.ring.Peer 12347 12348```
  - ``` java -cp .:ds/assign/ring ds.assign.ring.Peer 12348 12349```
  - ``` java -cp .:ds/assign/ring ds.assign.ring.Peer 12349 12345```

correr Injector
`$ java ds.assign.ring.Injector <ip_first_peer>`

Exemplo:
  - ``` java ds.assign.ring.Injector 12345 ```

correr Servercalc
`$ java ds.assign.ring.Servercalc`

## ENTROPY

compilar com o poisson
`$ javac -cp ds/assign/entropy/ ds/assign/entropy/*.java`

correr o Peer
`$ java ds.assign.entropy.Peer <ip_peer> <ip_next_peer>'s`

Exemplos:
  - ``` java -cp .:ds/assign/entropy ds.assign.entropy.Peer 12345 12346 ```
  - ``` java -cp .:ds/assign/entropy ds.assign.entropy.Peer 12346 12345 12347 12348 ```
  - ``` java -cp .:ds/assign/entropy ds.assign.entropy.Peer 12347 12346 ```
  - ``` java -cp .:ds/assign/entropy ds.assign.entropy.Peer 12348 12346 12349 12344 ```
  - ``` java -cp .:ds/assign/entropy ds.assign.entropy.Peer 12349 12348 ```
  - ``` java -cp .:ds/assign/entropy ds.assign.entropy.Peer 12344 12348 ```

correr Injector
`$ java ds.assign.entropy.Injector <ip_peer1> <ip_peer2> <ip_peer3> <ip_peer4> <ip_peer5> <ip_peer6>`

Exemplo:
  - ``` java ds.assign.entropy.Injector 12345 12346 12347 12348 12349 12344 ```

## CHAT 

compilar com o poisson
`$ javac -cp ds/assign/chat/ ds/assign/chat/*.java`

correr o Peer
`$ java ds.assign.chat.Peer <ip_peer> <ip_next_peer>'s`

Exemplos:
  - ``` java -cp .:ds/assign/chat ds.assign.entropy.Peer 12345 12346 12347 12348 12349 12344 ```
  - ``` java -cp .:ds/assign/chat ds.assign.entropy.Peer 12346 12345 12347 12348 12349 12344 ```
  - ``` java -cp .:ds/assign/chat ds.assign.entropy.Peer 12347 12346 12345 12348 12349 12344 ```
  - ``` java -cp .:ds/assign/chat ds.assign.entropy.Peer 12348 12346 12349 12344 12345 12347 ```
  - ``` java -cp .:ds/assign/chat ds.assign.entropy.Peer 12349 12348 12345 12347 12346 12344 ```
  - ``` java -cp .:ds/assign/chat ds.assign.entropy.Peer 12344 12348 12346 12347 12345 12349 ```


![image](https://github.com/mariajoao722/ProjetoSD/assets/75786385/71f37503-9657-4ee8-9ed4-fed8935e16c2)
![image](https://github.com/mariajoao722/ProjetoSD/assets/75786385/87d257d0-7b8b-4b4e-a2ab-42d53e7a3357)
![image](https://github.com/mariajoao722/ProjetoSD/assets/75786385/9a6c6b05-6cad-48cb-b06a-379c5463b1a9)
![image](https://github.com/mariajoao722/ProjetoSD/assets/75786385/ab95635d-d69f-4858-88c9-0abf86325588)




