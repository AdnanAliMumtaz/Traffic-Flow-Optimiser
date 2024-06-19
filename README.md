## Traffic Flow Optimiser
The Gridlock-on-Ribble Traffic Simulation project models the traffic flow in the fictional industrial town of Gridlock-on-Ribble, notorious for its severe morning traffic jams. The town's layout includes entry points from the north, south, and east, leading to various destinations like an industrial park, shopping centre, university, and station. This project aims to simulate and analyse the impact of different traffic light timings at intersections to optimise traffic flow and reduce congestion.

### What Does This Project Do?

- **Simulates Traffic Flow**: Models the movement of cars through the town's road network, from entry points to destinations, via traffic-light controlled intersections.
- **Analyses Traffic Patterns**: Evaluates the effect of different traffic light timings on overall traffic flow and congestion.
- **Reports Performance**: Generates detailed logs of traffic light operations, including cars passed, queue lengths, and instances of gridlock.
- **Configurable Simulation**: Allows customisation of entry rates and traffic light timings through configuration files, enabling various scenarios to be tested.

The project is implemented in Java and leverages concurrency to simulate real-time traffic management effectively.

### Road Network Diagram

Here is a diagram of the road network in Gridlock-on-Ribble, including entry points, junctions, and destinations:
<div align="center">
  <img width="550" alt="Road Network Diagram" src="https://github.com/AdnanAliMumtaz/Traffic-Flow-Optimiser/assets/81415901/8fc67974-2f7f-4dad-a649-28e4836b0a33">
</div>

### Classes and Architecture

The Gridlock-on-Ribble Traffic Simulation project is structured around several key classes that represent different components of the simulation:

- **Car Class**: Represents individual cars within the simulation.
  
- **EntryPoint Class**: Simulates vehicles entering the town from specific entry points.
  
- **Road Class**: Acts as a buffer between different components (entry points, junctions, and car parks).
  
- **CarPark Class**: Represents destinations within Gridlock-on-Ribble where vehicles ultimately park.
  
- **Junction Class**: Controls traffic flow between different roads using simulated traffic lights.
  
- **Configuration Class**: Reads configuration files to set up initial parameters for entry point rates and junction timings.
  
- **Simulation Class**: Coordinates the initialization and execution of the entire simulation.

This architecture ensures a clear separation of concerns and facilitates the realistic modeling of traffic behaviors and interactions within Gridlock-on-Ribble.


### Simulation Details

The Gridlock-on-Ribble Traffic Simulation project models traffic dynamics in the town with the following features:

- **Traffic Flow**: Vehicles enter from designated entry points at configurable rates, navigate roads controlled by junctions with simulated traffic lights, and park at specified destinations.
  
- **Concurrency**: Uses Java's concurrency to handle multiple components (entry points, junctions, car parks) as threads, ensuring realistic traffic interactions.
  
- **Road Management**: Roads act as fixed-size buffers between components, facilitating safe vehicle movement and updates within the simulation.
  
- **Traffic Light Control**: Junctions manage traffic flow with configurable light timings, controlling when vehicles can proceed through intersections.
  
- **Simulation Clock**: Manages time progression, enabling time-based events, logging, and reporting of simulation activities. The simulation runs at a scaled time rate where one hour in simulation time corresponds to approximately 6 minutes of real-time, allowing for efficient evaluation of traffic scenarios.

## Reporting
1. **Console Output**: At the start, the program outputs the name of the configuration file being used.
2. **Log Files**: Each junction records its activities, including cars passed, queue lengths, and potential gridlocks, in a log file.

Example log entry:
- `Time: 12m13s - Junction D: 4 cars through from East, 6 cars waiting.`
- `Time: 12m14s - Junction B: 0 cars through from North, 12 cars waiting. GRIDLOCK`

## Disclaimer
This project is for educational purposes only. Unauthorized copying and distribution of this project are strictly prohibited. Contributions are not accepted.
