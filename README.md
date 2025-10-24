# Queue Simulation

This Java project simulates a multi-queue system where clients (tasks) are dynamically assigned to servers based on a selected allocation strategy.

## Features:
- Automatic generation of clients with random arrival and service times  
- Dynamic allocation to servers according to the selected policy (SHORTEST_QUEUE / SHORTEST_TIME)  
- Real-time graphical visualization of the queue states  
- Logging of system evolution to a `.txt` file (Storage)  
- Display of peak time and average times at the end of the simulation  

## How to Use:
1. Run the application (SimulationFrame class)  
2. Enter the parameters: number of clients, number of servers, min/max arrival and service times  
3. Select the allocation policy:
   - **SHORTEST_QUEUE** – assigns to the queue with the fewest clients  
   - **SHORTEST_TIME** – assigns to the queue with the smallest estimated processing time  
4. Press **Start** and observe the simulation  

## Project Structure:
- **Model:**
  - `Task` – represents a client  
  - `Server` – a queue that processes clients  

- **BusinessLogic:**
  - `Scheduler` – allocates tasks based on the chosen policy  
  - `Strategy` – interface for allocation policies  
  - `ConcreteStrategyQueue` / `ConcreteStrategyTime` – implementations of allocation strategies  

- **GUI:**
  - `SimulationFrame` – graphical user interface  
  - Live animation of queues and real-time information display  

- **SimulationManager:**
  - Main simulation logic  
  - Coordinates the overall flow and result display  

- **Utils:**
  - `Storage` – writes the system’s time evolution to a `.txt` file  
