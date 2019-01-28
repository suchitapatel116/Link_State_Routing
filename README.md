# Link_State_Routing
Simulation of working of Open Shortest Path First routing protocol (Dijkstra’s algorithm) 

- It models the network in the form of weighted graph where each node is a router and edge is the link.
- The link also specifies the cost of traversal between the routers. It can be applied to the networks in which the direct distance is known.
- It takes each node individually to calculate the optimal path to every other node in the network. 
- For nodes that are not connected directly it first finds the neighbouring nodes by the information provided in forwarding table and connects to destination via intermediate nodes.

- Application Design: 
(1) Creating a Network Topology 
(2) Building a Forward Table 
(3) Calculating Shortest Path to Destination Router 
(4) Modifying a Topology (Change the status of the Router) 
(5) Finding the best Router for Broadcast 
