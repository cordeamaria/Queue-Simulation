# Queue Simulation

Acest proiect Java simulează un sistem cu cozi multiple, unde clienții (task-uri) sunt alocați dinamic către servere în funcție de o strategie de alocare selectată.

## Funcționalități:
- Generarea automată a clienților cu timp de sosire și service aleator
- Alocarea lor în servere după politica selectată (SHORTEST_QUEUE / SHORTEST_TIME)
- Vizualizare grafică în timp real a stării cozilor
- Logare în fișier .txt a evoluției sistemului (Storage)
- Afișarea orei de vârf și a timpilor medii la finalul simulării

##Cum se folosește:
1. Rulează aplicația (clasa SimulationFrame)
2. Introdu parametrii: număr clienți, număr servere, timpi min/max de sosire și procesare
3. Selectează politica de alocare:
   - SHORTEST_QUEUE – coada cu cei mai puțini clienți
   - SHORTEST_TIME – coada cu cel mai mic timp de procesare estimat
4. Apasă Start și urmărește simularea

##Structură Proiect:
- Model:
  - Task – reprezintă un client
  - Server – coadă care procesează clienți

- BusinessLogic:
  - Scheduler – alocă task-urile pe baza politicii
  - Strategy – interfață pentru politici
  - ConcreteStrategyQueue / ConcreteStrategyTime – implementări pentru alocare

- GUI:
  - SimulationFrame – interfață grafică
  - Animație live a cozilor și informații în timp real

- SimulationManager:
  - Logica principală a simulării
  - Coordonează fluxul general și afișarea rezultatelor

- Utils:
  - Storage – scrierea în fișier .txt a evoluției în timp a sistemului
