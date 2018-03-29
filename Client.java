/*
 *
 * This program implements the client side of a client server application using Remote Method Invocation (RMI).
 * 
 * The program locates the RMI registry at the specified IP address, then the client creates a Remote Interface object 
 * using a predetermined keyword on the registry.
 * After the user selects a command from the list, the user is asked how many times to run the command.
 * After recording the time, the program calls the corresponding method from the Remote Interface object to execute
 * the desired command on the server the requested number of times.
 * After the commands are finished executing, the end time is recorded and the total time it took to execute all of the
 * commands is calculated.
 * When the response is recieved, it prints the response, and waits for the user to select a new command. 
 * When number 7 is selected it closes connection to the server and exits.
 *
 * Input: The IP address is supplied via the command line.  
 *
 * Output: The program prints a menu for user to select a command, and response from server.
 *
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		String host = "";
		String response;
		Registry registry;
		Scanner reader = new Scanner(System.in);
		boolean connected;
		int choice = 0;
		int numTimes = 0;
		String echo;
		long startTime;
		long endTime;

		// Reports an error and exits if there is no command line argument
		if (args.length > 0) {
			host = args[0];
		} else {
			System.out.println("Error, no server IP specified.");
			System.exit(0);
		}

		try {
			// Locates the registry for the specified host
			registry = LocateRegistry.getRegistry(host);

			// Obtains the stub from the registry bound to the keyword "RMI"
			RemoteInterface stub = (RemoteInterface) registry.lookup("RemoteInterface");
			connected = true;

			// Loop to allow the user to enter multiple commands
			while (connected) {
				response = "";
				startTime = 0;
				endTime = 0;
				numTimes = 0;

				// Print command list to user.
				System.out.println("Select a command below:");
				System.out.println("1. Server current Date and Time ");
				System.out.println("2. Server number of running processes ");
				System.out.println("3. Server number of active socket connections");
				System.out.println("4. Server time of last system boot ");
				System.out.println("5. Server list of current users");
				System.out.println("6. Server echo back what is sent from client");
				System.out.println("7. Quit \n");
				System.out.print("Input: ");

				// Verifies that the input is an integer
				try {
					choice = reader.nextInt();
					reader.nextLine();
					System.out.println();
				} catch (Exception e) {
					reader.nextLine();
				}

				// Verifies that the input is 1-7
				if (choice >= 1 && choice <= 7) {
					if (choice < 7) {
						System.out.println("Enter the number of times to run the command: ");

						// Verifies that the input is an integer
						try {
							numTimes = reader.nextInt();
							reader.nextLine();
							System.out.println();
						} catch (Exception e) {
							reader.nextLine();
							System.out.println("Invalid input. Please enter an integer.\n");
						}
					}

					// Calls the desired method from the stub
					switch (choice) {
					case 1:
						// Records the time the commands began being executed
						startTime = System.currentTimeMillis();

						// Executes the chosen command the desired number of times
						for (int i = 0; i < numTimes; i++) {
							response = stub.GetDateTime();
						}

						// Records the time after the commands are finished
						// being executed
						endTime = System.currentTimeMillis();

						// Prints the response
						System.out.println("Response: " + response);

						// Prints the how long the request took
						System.out.println("Request 1 took " + (endTime - startTime) + "ms\n");
						break;

					case 2:
						startTime = System.currentTimeMillis();
						for (int i = 0; i < numTimes; i++) {
							response = stub.GetNumProcs();
						}
						endTime = System.currentTimeMillis();
						System.out.println("Response: " + response);
						System.out.println("Request 2 took " + (endTime - startTime) + "ms\n");
						break;

					case 3:
						startTime = System.currentTimeMillis();
						for (int i = 0; i < numTimes; i++) {
							response = stub.GetNumConns();
						}
						endTime = System.currentTimeMillis();
						System.out.println("Response: " + response);
						System.out.println("Request 3 took " + (endTime - startTime) + "ms\n");
						break;

					case 4:
						startTime = System.currentTimeMillis();
						for (int i = 0; i < numTimes; i++) {
							response = stub.GetLastBoot();
						}
						endTime = System.currentTimeMillis();
						System.out.println("Response: " + response);
						System.out.println("Request 4 took " + (endTime - startTime) + "ms\n");
						break;

					case 5:
						startTime = System.currentTimeMillis();
						for (int i = 0; i < numTimes; i++) {
							response = stub.GetCurrUsers();
						}
						endTime = System.currentTimeMillis();
						System.out.println("Response: " + response);
						System.out.println("Request 5 took " + (endTime - startTime) + "ms\n");
						break;

					case 6:
						System.out.println("Enter the string to be echoed.");
						echo = reader.nextLine();
						startTime = System.currentTimeMillis();
						for (int i = 0; i < numTimes; i++) {
							response = stub.Echo(echo);
						}
						endTime = System.currentTimeMillis();
						System.out.println("Response: " + response);
						System.out.println("Request 6 took " + (endTime - startTime) + "ms\n");
						break;

					case 7:
						// Causes the loop to end
						connected = false;
						System.out.println("Client Quitting");
					//	stub.RageQuit();
						break;
					}
				} else {
					System.out.println("Invalid input. Please enter a number 1-7.\n");
				}
				choice = 0;
				numTimes = 0;
			} // While
			reader.close();

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}

	}

}
