
public class Texts {
	public static String[] texts;



	
	public static void setTexts(String langue)
	{
			if(langue.equals("french"))
			{
				texts = new String[]{
							
							"Quitter",
							"Retour",
							"Annuler",
							"Ajouter",
							"Valider",
							"D�marrer",
							"FALLDOWN",
							"Auteurs",
							"R�alis� par Laurent Herbin\n\nInspir� du jeu Falldown sur calculatrice texas instrument par Thomas Fernique",
							"Entrez votre nom : ",
							"Beau travail",
							"Resultat",
							"Meilleurs scores",
							"Partie bluetooth",
							"Serveur",
							"Client",
							"Session multi - Serveur",
							"Session multi - Client",
							"Attente du d�marrage de la partie par le serveur",
							"Selection du gameplay",
							"Solo",
							"R�seau - bluetooth",
							"Meilleurs scores",
							"Informations",
							"Quitter",
							"Resultat en mode ",
							"Felicitation ",
							"\nVous avez fait le ",
							"eme meilleur score\navec ",
							"Vainqueur : ",
							"\nAvec un score de : ",
							"Votre score est de ",
							"Classique",
							"Evite ou meurs",
							"Binaire",
							"Heavy trash Seb",
							"Facile pour test"
							
							
							
						
						
				};
			}
			else
			{
				texts = new String[]{
						
						"Exit",
						"Back",
						"Cancel",
						"Add",
						"Ok",
						"Start",
						"FALLDOWN",
						"Authors",
						"Made by Laurent Herbinn\n\nBased on the game Falldown on texas instrument calcs by Thomas Fernique",
						"Enter your name : ",
						"Good job",
						"Results",
						"Best scores",
						"Bluetooth game",
						"Server",
						"Client",
						"Session multi - Server",
						"Session multi - Client",
						"Waiting for game start from the server",
						"Gameplay Selection",
						"Solo",
						"Network - bluetooth",
						"Best scores ",
						"Informations",
						"Exit",
						"Results with gameplay ",
						"Great ",
						"\nYou did the ",
						" score\nwith ",
						"Winner : ",
						"\nWith a score of : ",
						"Your score is ",
						"Classical",
						"Straff or die",
						"Binary",
						"Heavy trash Seb",
						"Easy (for test)"
				};
			}
	}
	
	public static String get(int i)
	{
		return texts[i];
	}
}
