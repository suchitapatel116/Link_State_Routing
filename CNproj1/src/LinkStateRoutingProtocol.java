/*
 * Patel Suchita Kiranbhai
 * Seat No: 43
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LinkStateRoutingProtocol {
	
	Scanner scan = new Scanner(System.in);
	int fileLineCount = 0;
	int INFINITE_DIST = 999;

	public static void main(String[] args) 
	{
		int user_Input = 0;
		LinkStateRoutingProtocol lsrp = new LinkStateRoutingProtocol();
		int[][] topo_matrix = null;
		Scanner sc = new Scanner(System.in);
		int source_router = 0, dest_router = 0, del_router = -1;
		int[] forward_tab_dist = null;
		int[] list_Prevoius_Nodes = null;
		ArrayList<Integer> deleted_routers = new ArrayList<>();
		int flag = 0;
		boolean flag2 = true;
		
		while(user_Input != 6)
		{
			try
			{
				String inp = lsrp.print_Menu();		//Display the main menu for taking the user input
				user_Input = Integer.parseInt(inp);
			}
			catch(NumberFormatException e)
			{
				//If user enters character instead of numeric value
				user_Input = 0;
			}
			
			switch(user_Input)
			{
			case 1:
				topo_matrix = lsrp.create_Topology();
				//In case of any error, the matrix will not be created so check for null before printing
				if(topo_matrix != null)
					lsrp.display_Topo_Matrix(topo_matrix);
				
				//Empty the list as again the inputs are read from file again
				deleted_routers.clear();
				source_router = 0;		//Clear the existing values
				dest_router = 0;
				break;
				
			case 2:
				if(topo_matrix == null)		//Checks if first the input matrix file is given or not
					System.out.println("Please specify Topology input file.");
				if(topo_matrix != null)
				{
					//Initialize the values if they are null
					if(forward_tab_dist == null)
						forward_tab_dist = new int[lsrp.fileLineCount];
					if(list_Prevoius_Nodes == null)
						list_Prevoius_Nodes = new int[lsrp.fileLineCount];
					try
					{
						System.out.print("Select a source router: ");
						source_router = Integer.parseInt(sc.next()); //router # in program 0,1,2 and user enters 1,2,3
						
						//If the router is deleted previous then don't proceed further
						if(deleted_routers.contains(source_router - 1))
							System.out.println("Router "+ source_router + " is down. Please select another router.");
						else if(source_router <= 0 || source_router > topo_matrix.length)
							System.out.println("Please enter proper source router number");
						else
						{
							//Main logic for creating forwarding table, first implement the algorithm and then form the table 
							forward_tab_dist = lsrp.shrtst_Path_Dijkstra_Algo(topo_matrix, (source_router - 1), forward_tab_dist, list_Prevoius_Nodes);	
							lsrp.display_Forward_Table(topo_matrix, (source_router - 1), forward_tab_dist, list_Prevoius_Nodes, deleted_routers);
						}
					}
					catch(Exception e)
					{
						//If the user enters character or other router number other than existing then display error
						System.out.println("Please enter proper source router number");
					}
				}
				break;
				
			case 3:
				if(topo_matrix == null)		//Checks if first the input matrix file is given or not
					System.out.println("Please specify Topology input file.");
				if(topo_matrix != null)
				{
					//Initialize the values if they are null
					if(forward_tab_dist == null)
						forward_tab_dist = new int[lsrp.fileLineCount];
					if(list_Prevoius_Nodes == null)
						list_Prevoius_Nodes = new int[lsrp.fileLineCount];
					try
					{
						flag = 0;
						System.out.print("Enter soure router: ");
						source_router = Integer.parseInt(sc.next());
						
						//If the router is deleted previous then don't proceed further
						if(deleted_routers.contains(source_router - 1))
						{
							System.out.println("Router "+ source_router + " is down. Please select another router.");
							flag = 1;
						}
						else if(source_router <= 0 || source_router > topo_matrix.length)
						{
							System.out.println("Please enter proper source router number");
							flag = 1;
						}
						//Use of flag so that if source is inappropriate then don;t proceed further
						if(flag == 0)
						{
							System.out.print("Enter destination router: ");
							dest_router = Integer.parseInt(sc.next());
							
							if(deleted_routers.contains(dest_router - 1))
								System.out.println("Router "+ del_router + " is down. Please select another router.");
							else if(dest_router <= 0 || dest_router > topo_matrix.length)
								System.out.println("Please enter proper destination router number");
							else
							{
								//Implementation for displaying shortest path, first implement the algorithm and then traverse the path 
								forward_tab_dist = lsrp.shrtst_Path_Dijkstra_Algo(topo_matrix, (source_router - 1), forward_tab_dist, list_Prevoius_Nodes);
								lsrp.source_to_dest_path((source_router - 1), (dest_router - 1), forward_tab_dist, list_Prevoius_Nodes, deleted_routers);
							}
						} 
					}
					catch(Exception e)
					{
						//If the user enters character or other router number other than existing then display error
						System.out.println("Please enter proper source and destination values");
					}
				}
				break;
				
			case 4:
				if(topo_matrix == null) 	//Checks if first the input matrix file is given or not
					System.out.println("Please specify Topology input file.");
				if(topo_matrix != null)
				{
					try
					{
						System.out.print("Enter router to be deleted from the Network Topology: ");
						del_router = Integer.parseInt(sc.next());
						
						//If user enters delete router number greater than existing routers in topology then display msg
						if(del_router <= 0 || del_router > topo_matrix.length)
							System.out.println("Please enter proper router for deletion");
						else
						{
							//Call modify topology and then display the forwarding table and shortest path
							topo_matrix = lsrp.modify_Topology_del(topo_matrix, (del_router - 1));
							lsrp.display_Topo_Matrix(topo_matrix);
							deleted_routers.add(del_router - 1);
							
							//Checks if the source and destination are given previously
							if(source_router != 0 && dest_router != 0)
							{
								System.out.println("For shortest distance from "+ source_router +" to "+ dest_router +":");
								forward_tab_dist = lsrp.shrtst_Path_Dijkstra_Algo(topo_matrix, (source_router - 1), forward_tab_dist, list_Prevoius_Nodes);	
								flag2 = lsrp.display_Forward_Table(topo_matrix, (source_router - 1), forward_tab_dist, list_Prevoius_Nodes, deleted_routers);
								
								//flag2 used for checking that if deleted router detected in forward table then don't do further processing
								if(flag2 == true)
									lsrp.source_to_dest_path((source_router - 1), (dest_router - 1), forward_tab_dist, list_Prevoius_Nodes, deleted_routers);
								flag2 = true;
							}
							else
							{
								//In case if previously source and destination are not specified
								System.out.println("Proceed with command 3 for finding the shortest path traversed");
							}
						}
					}
					catch(Exception e)
					{
						//If the user enters character or other router number other than existing then display error
						System.out.println("Please enter valid router number");
					}
				}
				break;
			
			case 5:
				if(topo_matrix == null) 	//Checks if first the input matrix file is given or not
					System.out.println("Please specify Topology input file.");
				if(topo_matrix != null)
				{
					//Gets the best broadcasting router
					int router = lsrp.best_Broadcasting_Router(topo_matrix);
					System.out.println("\nBest Router for broadcast is: "+ router);
				}
				break;
				
			case 6:
				//Exit program
				System.out.println("\nExit CS542-04 2017 Fall project. Good Bye!");
				break;
				
			default:
				//If user enter any other input apart from the list show error message
				System.out.println("Please enter proper operation number from 1 to 6");
				break;
			}
		}
		
		lsrp.scan.close();
		sc.close();
		System.exit(0);
	}

	//Displays the main menu for user input
	private String print_Menu()
	{
		System.out.println("\n----------CS542 Link State Routing Simulator----------");
		System.out.println("(1) Create a Network Topology");
		System.out.println("(2) Build a Forward Table");
		System.out.println("(3) Shortest Path to Destination Router");
		System.out.println("(4) Modify a Topology (Change the status of the Router)");
		System.out.println("(5) Best Router for Broadcast");
		System.out.println("(6) Exit");
		System.out.print("Master Command: ");

		return scan.next();
	}
	
	//Prepares the topology matrix from file after reading and validating the file contents
	private int[][] create_Topology()
	{
		int count = 0;
		String[] tokens = null;
		boolean isValid = true;
		ArrayList<String> fileContents = null;
		int[][] matrix_table = null;
		int errCode = 0;
		
		System.out.print("Enter Network Topology matrix data file: ");
		File fileName = new File(scan.next());

		try
		{
			//Reads the file and stores the contents in variable
			fileContents = readFileAndCountLines(fileName);
			
			if(!fileContents.isEmpty())
			{
				count = fileContents.size();
				tokens = new String[count];
				matrix_table = new int[count][count];
				
				for(int row=0; row<count; row++)
				{
					//Spit the column values in each row of the file and checks for same number of rows and columns
					//'\\s+' is the regular expression that checks for multiple spaces
					tokens = fileContents.get(row).trim().split("\\s+");
					if(count != tokens.length)
					{
						errCode = 1;
						isValid = false;
						break;
					}
					else
					{
						try{
							for(int col=0; col<tokens.length; col++)
							{
								int test = Integer.parseInt(tokens[col]);
								matrix_table[row][col] = test;
								
								//Checks that diagonal values are 0, for distance of a router to itself
								if(row == col && matrix_table[row][col] != 0)
								{
									errCode = 2;
									isValid = false;
									break;
								}
							}
						}
						catch(Exception e){
							isValid = false;
							break;
						}
					}
				}
				//Display the error message appropriately according to the error occured
				if(isValid == false && errCode == 1)
				{
					System.out.println("Please enter square matrix");
					matrix_table = null;
				}
				else if(isValid == false && errCode == 2)
				{
					System.out.println("Please check diagonal values");
					matrix_table = null;
				}
				else if(isValid == false && errCode == 0)		
				{
					System.out.println("Please provide proper input file contents");
					matrix_table = null;
				}
				else
				{
					//Show what is the size of the matrix
					System.out.println("The input matrix is " + count + " * " + count);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("IOException!");
		}
		return matrix_table;
	}
	
	//Read the file contents to get no of routers
	private ArrayList<String> readFileAndCountLines(File fileName) throws IOException
	{
		String line = "";
		FileReader strm_reader = null;
		BufferedReader bffr_reader = null;
		ArrayList<String> fileContents = new ArrayList<String>();
		
		try
		{
			strm_reader = new FileReader(fileName);	
			bffr_reader = new BufferedReader(strm_reader);
			
			while((line = bffr_reader.readLine()) != null)
			{
				//Add the contents of a file in a variable
				fileContents.add(line);
				//Counts the number of routers
				fileLineCount++;
			}
			if(fileContents.isEmpty())
				System.out.println("The File is Empty!");
		}
		catch(Exception e)
		{
			System.out.println("File not Found!");
		}
		finally
		{
			if(bffr_reader != null)
				bffr_reader.close();
			if(strm_reader != null)
				strm_reader.close();
		}
		return fileContents;
	}

	//Display the matrix topology
	private void display_Topo_Matrix(int[][] matrix) 
	{
		System.out.println("\nReview Topology Matrix:");
		for(int row=0; row<matrix.length; row++)
		{
			for(int col=0; col<matrix[row].length; col++)
			{
				System.out.print(matrix[row][col] + "\t");
			}
			System.out.println();
		}
	}

	//Prints the forwarding table for selected router
	private boolean display_Forward_Table(int[][] matrixTopo ,int source, int[] forward_tab_dist, int[] list_Prevoius_Nodes, ArrayList<Integer> del_router) 
	{
		int last_visited_prev = 0;
		boolean retVal = true;
		
		//If the router is deleted then don't check further. First check if it is not null otherwise exception
		if(del_router != null && del_router.contains(source))
		{
			System.out.println("Router "+ (source+1) +" is removed from topology");
			retVal = false;
		}
		else
		{
			System.out.println("\nRouter " + (source + 1) + " Connection Table");
			System.out.println("Destination\tInterface");
			System.out.println("===========================");
			
			for(int n=0; n<matrixTopo.length; n++)
			{
				//for source node just display '-' else find the previous node
				if(n == source)
					System.out.println((source + 1) + "\t\t - ");
				else
				{
					last_visited_prev = n;
					//if it is source node or already previous node then exit else iterate to find the min dist node
					while(!(list_Prevoius_Nodes[last_visited_prev] == last_visited_prev || list_Prevoius_Nodes[last_visited_prev] == source))
					{
						last_visited_prev = list_Prevoius_Nodes[last_visited_prev]; //iterate till first previous node
					}
					if(forward_tab_dist[n] == INFINITE_DIST)
						System.out.println((n+1) + "\t\t Router is Down or Unreachable"); //when a node is disconnected to the graph or in case of deleted router
					else
						System.out.println((n+1) + "\t\t " + (last_visited_prev + 1));	
				}
			}
		}
		//If router is deleted from the topology then return false indicating the same
		return retVal;
	}

	//Implementation of shortest path Dijkstra Algorithm
	private int[] shrtst_Path_Dijkstra_Algo(int[][] matrx, int sNode, int[] forward_tab_dist, int[] list_Prevoius_Nodes) 
	{
		ArrayList<Integer> visitedNodes_M = new ArrayList<>(); //3. set of visited vertices is initially empty
		ArrayList<Integer> unvisitedNodes_Q = new ArrayList<>(); //4. the queue initially contains all vertices
		
		//1. Distance to source vertex = 0
		forward_tab_dist[sNode] = 0;

		//2. Initialize: Set all other distances to infinity
		int k=0;
		while(k < matrx.length)
		{
			unvisitedNodes_Q.add(k);
			if(k != sNode)
			{
				forward_tab_dist[k] = INFINITE_DIST;
			}
			k++;
		}
		
		//list of previous nodes, to traverse back
		list_Prevoius_Nodes[sNode] = sNode;
		
		//5. While queue is not empty
		while(!unvisitedNodes_Q.isEmpty())
		{
			int minDist = INFINITE_DIST;
			int u_minDistNode = -1;
			
			//6. select the element of Q with min distance
			int l = 0;
			while(l < unvisitedNodes_Q.size())
			{
				int n = unvisitedNodes_Q.get(l);
				if(!(forward_tab_dist[n] <= minDist))
				{
					l++;
					continue;
				}
				else
				{
					u_minDistNode = n;
					minDist = forward_tab_dist[n];
				}
				l++;
			}
			//No minimum distance node found then return
			if(u_minDistNode == -1)
			{	break;	}
			
			//7. add u to list of visited vertices
			visitedNodes_M.add(u_minDistNode);
			
			int index = unvisitedNodes_Q.indexOf(u_minDistNode);	//remove this node from unvisited nodes
			unvisitedNodes_Q.remove(index);
			
			//8.0 algo - consider neighbor as direct distance nodes except source
			for(int v=0; v<matrx.length; v++)
			{
				//matrix[minDistNode_u][v] > 0 ensures it is not source node
				//unvisitedNodes_Q.contains(v) only check for unvisited nodes
				
				if(unvisitedNodes_Q.contains(v) && matrx[u_minDistNode][v] > 0)
				{
					int val = forward_tab_dist[u_minDistNode] + matrx[u_minDistNode][v];
					if(val < forward_tab_dist[v])
					{
						//stores shortest distance
						forward_tab_dist[v] = val;
						//stores last node for back traversal to find path
						list_Prevoius_Nodes[v] = u_minDistNode;
					}
				}
			}
		}
		return forward_tab_dist;
	}
	
	//Calculates and displays shortest Path to Destination Router
	private void source_to_dest_path(int source, int dest, int[] forward_dist_tab, int[] list_Prevoius_Node, ArrayList<Integer> del_router)
	{
		if(del_router != null && del_router.contains(dest))
			System.out.println("Router "+ (dest+1) +" is deleted");
		else if(del_router != null && del_router.contains(source))
			System.out.println("Router "+ (source+1) +" is deleted");
		else
		{
			int[] prevNodes_List = list_Prevoius_Node;
			int k = dest;
			String path = "" + (dest+1);
			
			//Check if node is reachable that if it is connected in the graph then only proceed further
			if(forward_dist_tab[dest] != INFINITE_DIST)
			{
				while(prevNodes_List[k] != source)
				{
					path += " >- " + (prevNodes_List[k] + 1);
					k = prevNodes_List[k];
				}
				//for source node
				path += " >- " + (source+1);
				
				//Back traversal is done using the previous node list
				String disp_path= new StringBuilder(path).reverse().toString();
				
				System.out.println("\nShortest distance from "+ (source+1) +" to "+ (dest+1) +":");
				System.out.println("PATH: "+ disp_path);
				System.out.println("COST: " + forward_dist_tab[dest]);
			}
			else
			{
				System.out.println("\nRouter "+ (dest+1) +" is dead or unreachable.");
			}
		}
	}
	
	//Modifies the Topology, when a router is down it changes the status of the Router
	private int[][] modify_Topology_del(int[][] m, int del_router)
	{
		int[][] matrx = m;
		
		//Display the distance of deleted router to other routers as -1 when deleted
		for(int r=0; r<matrx.length; r++)
		{
			for(int c=0; c<matrx[r].length; c++)
			{
				if(r == del_router)
					matrx[r][c] = -1;
				else if(c == del_router)
					matrx[r][c] = -1;
			}
		}		
		return matrx;
	}
	
	//Finds the router that has minimum cost to other nodes for broadcasting
	private int best_Broadcasting_Router(int[][] matrx)
	{
		int best_router = -1;
		int min = INFINITE_DIST;
		int[] min_dist = new int[matrx.length];
		int[] forward_min_dist_table = new int[matrx.length];
		int[] prev_node = new int[matrx.length];
		
		for(int s=0; s<matrx.length; s++)
		{
			//Compute shortest path for every node
			forward_min_dist_table = shrtst_Path_Dijkstra_Algo(matrx, s, forward_min_dist_table, prev_node);
			
			//only include reachable nodes for total
			for(int i=0; i<forward_min_dist_table.length; i++)
			{
				if(forward_min_dist_table[i] != INFINITE_DIST)
					min_dist[s] += forward_min_dist_table[i];
			}
		}
		//min_dist array contains min distance from every node to every other node
		for(int n=0; n<min_dist.length; n++)
		{
			System.out.println("Total cost of router "+ (n+1) +" to other nodes is: "+ min_dist[n]);
			if(min_dist[n] < min && min_dist[n] != 0)
			{
				min = min_dist[n];
				best_router = n;
			}
		}
		return (best_router + 1);
	}

}


