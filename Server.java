/*
 *
 * This program implements the Server side of a client server application.  The client 
 * connects to the server, then uses Remote Method Invocation to invoke methods on the server. 
 * The methods on this server send back the appropriate response about the server based on the 
 * invoked method. When the user invokes the RageQuit method the server unbinds and closes.
 *
 */
 
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.InputStreamReader;
import java.io.BufferedReader; 
       
public class Server implements RemoteInterface {
String[] cmd;
Process cmdProc;
Registry registry;

    public Server() {}
    //Send client date and time on server
    public String GetDateTime() {
       System.out.println("Responding to date request from the client ");
       try{
          cmd = new String[] { "bash", "-c", "date" };
          cmdProc = Runtime.getRuntime().exec(cmd);
          BufferedReader cmdin = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
          return cmdin.readLine();
	   } catch (Exception e) {
		  return "Failure";
	   }
    }
    //Send client number of active processes on server
    public String GetNumProcs() {
       System.out.println("Responding to number of running processes request from client ");
       try{
          cmd = new String[] { "bash", "-c", "ps -ef|wc -l" };
          cmdProc = Runtime.getRuntime().exec(cmd);
          BufferedReader cmdin = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
          return cmdin.readLine();
       } catch (Exception e) {
           return "Failure";
       }
    }
    //Send client number of active sockets on server
    public String GetNumConns() {
       System.out.println("Responding to number of active socket connections request from client ");
       try{
          cmd = new String[] { "bash", "-c", "ss -t -a|grep ESTAB|wc -l" };
          cmdProc = Runtime.getRuntime().exec(cmd);
          BufferedReader cmdin = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
          return cmdin.readLine();
       } catch (Exception e) {
           return "Failure";
       }  
    }
    //Send client last boot time of server
    public String GetLastBoot() {
       System.out.println("Responding to time of last server system boot request from client ");
       try{
          cmd = new String[] { "bash", "-c", "who -b" };
          cmdProc = Runtime.getRuntime().exec(cmd);
          BufferedReader cmdin = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
          return cmdin.readLine();
       } catch (Exception e) {
          return "Failure";
       }
    }
    //Send client number current users on server
    public String GetCurrUsers() {
       System.out.println("Responding to list of current users request from client ");
        try{
          cmd = new String[] { "bash", "-c", "users" };
          cmdProc = Runtime.getRuntime().exec(cmd);
          BufferedReader cmdin = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
          return cmdin.readLine();
        } catch (Exception e) {
            return "Failure";
        }
    }
    //Echo input from user back to client
    public String Echo(String input) {
    System.out.println("Responding to echo request from client ");
      try{
         cmd = new String[] { "bash", "-c", "echo " + input};
         cmdProc = Runtime.getRuntime().exec(cmd);
         BufferedReader cmdin = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
         return cmdin.readLine();
      }catch (Exception e) {
          return "Failure";
      }
    }  
   //Unbind and quit program
   public void RageQuit(){
	   try{
		   registry.unbind("RemoteInterface");
		   UnicastRemoteObject.unexportObject(this,true);
	   } catch (Exception e){
		   System.out.println("Server didn't unbind");
	   }
	   return;
   }
        
   public static void main(String args[]) {
      try {
         Server obj = new Server();
         RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(obj, 0);

         // Bind the remote object's stub in the registry
         obj.registry = LocateRegistry.getRegistry();
         try{
		      obj.registry.bind("RemoteInterface", stub);
	      } catch (Exception e){
		      obj.registry.rebind("RemoteInterface", stub);
	      }
         System.err.println("Server ready");
      } catch (Exception e) {
         System.err.println("Server exception: " + e.toString());
         e.printStackTrace();
      }
   }
}