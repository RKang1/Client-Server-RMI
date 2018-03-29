import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote{
	// Methods availbe for the client to use

	String GetDateTime() throws RemoteException;
	
	String GetNumProcs() throws RemoteException;
	
	String GetNumConns() throws RemoteException;
	
	String GetLastBoot() throws RemoteException;
	
	String GetCurrUsers() throws RemoteException;
	
	String Echo(String input) throws RemoteException;

	void RageQuit() throws RemoteException;
}
