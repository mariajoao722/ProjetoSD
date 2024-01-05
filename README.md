# ProjetoSD

## RING

compilar com o poisson
`$ javac -cp ds/assign/ring/ ds/assign/ring/*.java`

correr o Peer
`$ java ds.assign.ring.Peer <ip_peer> <ip_next_peer>`

Exemplos:
  - ``` java ds.assign.ring.Peer 12345 12346```
  - ``` java ds.assign.ring.Peer 12346 12347```
  - ``` java ds.assign.ring.Peer 12347 12348```
  - ``` java ds.assign.ring.Peer 12348 12349```
  - ``` java ds.assign.ring.Peer 12349 12345```

correr Injector
`$ java ds.assign.ring.Injector <ip_first_peer>`

correr Servercalc
`$ java ds.assign.ring.Servercalc`



## ENTROPY

compilar com o poisson
`$ javac -cp ds/assign/entropy/ ds/assign/entropy/*.java`

correr o Peer
`$ java ds.assign.entropy.Peer <ip_peer> <ip_next_peer>'s`

Exemplos:
  - ``` java ds.assign.entropy.Peer 12345 12346 ```
  - ``` java ds.assign.entropy.Peer 12346 12345 12347 12348 ```
  - ``` java ds.assign.entropy.Peer 12347 12346 ```
  - ``` java ds.assign.entropy.Peer 12348 12346 12349 12344 ```
  - ``` java ds.assign.entropy.Peer 12349 12348 ```
  - ``` java ds.assign.entropy.Peer 12344 12348 ```

correr Injector
`$ java ds.assign.entropy.Injector <ip_peer1> <ip_peer2> <ip_peer3> <ip_peer4> <ip_peer5> <ip_peer6>`

Exemplo:
  - ``` java ds.assign.entropy.Injector 12345 12346 12347 12348 12349 12344 ```

## CHAT 

### IP Map
p1 12345 - 12346 12347 12348 12349 12344

p2 12346 - 12345 12347 12348 12349 12344

p3 12347 - 12346 12345 12348 12349 12344

p4 12348 - 12346 12349 12344 12345 12347

p5 12349 - 12348 12345 12347 12346 12344

p6 12344 - 12348 12346 12347 12345 12349
