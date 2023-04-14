# PixelSimulation

This is a very simple material simulation using pixels. <br>
Based on a [GDC talk](https://www.gdcvault.com/play/1025695/Exploring-the-Tech-and-Design "	Exploring the Tech and Design of 'Noita'"). <br><br>
The simulation is aimed to contain:
- **Sand** like simulations <br>
![Sand](./assets/sand-sim.gif)
- Settling **Liquid** simulation <br>
![Liquid](./assets/liquid-sim.gif)
- Rising **Gas** simulations <br>
![Gas](./assets/gas-sim.gif)
- Simulations interacting with each-other

Stacks used:
- Java <sup>17</sup>
- JavaFX <sup>Framework</sup>

## How to use <br>

Current version is WIP. <br>
Because of that you will need [Maven](https://maven.apache.org/) to compile and run this java application.

### To start
Linux:<br>
Navigate to the projects root folder where the ```pom.xml``` file is.

In the Terminal type: ```mvn clean install``` <sup>***clean*** keyword is optional but recommended if you already built this project once</sup><br>
This will create a clean target folder where maven will compile build and install the build result.

To start the application type: ```mvn javafx:run``` in the terminal.

## Application guide

Currently, there is 4 different pixel typesThat can be accessed by pressing the corresponding number key on the keyboard.
1. "Platform" is a solid. static pixel with collisions.
2. "Sand" is a very basic type of object that falls and rolls over edges.
3. "Liquid" is an object that after falling it will flow in a set direction if it can.
4. "Gas" is a basic object that will slowly rise to the top of the container its in.

Pressing and Holding the **left mouse button** will create the currently active object type. <br>
Pressing and Holding the **right mouse button** will delete the current objectif possible.