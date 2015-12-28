
public class ClientLocal extends Client {
	Serveur serveur;
	
	public ClientLocal(Serveur s) {
		connection = new ConnectionLocale();
		ConnectionLocale connectionServeur = new ConnectionLocale();
		((ConnectionLocale)connection).setConnection(connectionServeur);
		connectionServeur.setConnection(((ConnectionLocale)connection));
		s.add(connectionServeur);
	}
}
