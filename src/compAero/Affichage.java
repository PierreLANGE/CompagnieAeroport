package compAero;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Affichage {
	
	/**
	 * Affichage du menu
	 */
	public void menu() {
		System.out.println("|----------------------------------------|");
		System.out.println("|          --=<{[ AeroSoft ]}>=--        |");
		System.out.println("|----------------------------------------|");
		System.out.println("| Pour:                                  |");
		System.out.println("|                                        |");
		System.out.println("| Ajouter un Pilote                  (1) |");
		System.out.println("| La liste des Vols                  (2) |");
		System.out.println("| Rechercher un Vol                  (3) |");
		System.out.println("| La liste des Aeroports par Ville   (4) |");
		System.out.println("| Modifier la date d'une Affectation (5) |");
		System.out.println("| Supprimer une Affectation          (6) |");
		System.out.println("|                                        |");
		System.out.println("| Quitter                            (7) |");
		System.out.println("|----------------------------------------|");
	}
	
	/**
	 * Fonction pour afficher un titres
	 * @param String titre
	 * @throws SQLException
	 */
	public void titre(String titre) {
		
		//Calcul de la différence entre la largeur du menu et la longueur du titre
		double diffEsp = 40 - titre.length();

		//récupération de la première moitié d'espace à ajouter devant le titre
		int espBefore = (int) diffEsp / 2;
		
		//récupération de la deuxième moitié d'espace à ajouter après le titre
		//récupération sous forme de float pour l'arondir au plus haut
		//au cas ou la division ne tombe pas juste
		float a = (float) diffEsp / 2;
		int espAfter = (int) Math.round(a);

		//Contruction du titre
		String titreConstruit = construitEsp(espBefore) + titre + construitEsp(espAfter);

		//Affichage du titre
		System.out.println(" ");
		System.out.println("|----------------------------------------|");
		System.out.println("|" + titreConstruit + "|");
		System.out.println("|----------------------------------------|");
		System.out.println(" ");
	}
	
	/**
	 * Affichage des différents titres de la table Vol
	 * @param int nbLigne
	 * @throws SQLException
	 */
	public void titreVol(int nbLigne) {
		
		if (nbLigne == 1) {
			titre("Liste du Vol trouvé");

		} else if (nbLigne > 1) {
			titre("Liste des Vols");

		} else if (nbLigne < 1) {
			titre("Pas de vol trouvé");
		}
	}

	/**
	 * Affichage des lignes de la table Vol
	 * @param ResultSet rs
	 * @throws SQLException
	 */
	public void ligneVol(ResultSet rs) throws SQLException {

		while (rs.next()) {

			// Retrieve by column name
			String numVol = rs.getString("NumVol");
			String aeroportDept = rs.getString("AeroportDept");
			String hDepart = rs.getString("HDepart");
			String aeroportArr = rs.getString("AeroportArr");
			String hArrivee = rs.getString("HArrivee");

			// Display values
			System.out.print("N°: " + numVol);
			System.out.print(", Départ  à: " + hDepart);
			System.out.println(" de  l'aréoport de " + aeroportDept);
			System.out.print( construitEsp(11) + "Arrivée à: " + hArrivee);
			System.out.println(" sur l'aréoport de " + aeroportArr+"\n");
		}
	}

	/**
	 * Affichage des lignes de la table Pilotes
	 * @param ResultSet rs
	 * @throws SQLException
	 */
	public void lignePilotes(ResultSet rs) throws SQLException {
		
		int ecartId = 0;
		int espAfter = 0;

		while (rs.next()) {

			// Retrieve by column name
			int idPilote = rs.getInt("idPilote");
			String prenomPilote = rs.getString("PrenomPilote");
			String nomPilote = rs.getString("NomPilote");
			
			//ajout d'un espace aux id Pilote qui seront plus petit que 9 pour les aligner
			if(idPilote < 10) {
				 ecartId = 1;
			}else {
				ecartId = 0;
			}
			
			//Configuration de la variable espAfter pour aligner le Nom des Pilotes
			espAfter = 8 - prenomPilote.length();

			// Display values
			System.out.print("id: " + construitEsp(ecartId) + idPilote);
			System.out.print(", Prénom: " + prenomPilote + construitEsp(espAfter));
			System.out.println(", Nom: " + nomPilote);

		}
		System.out.println(" ");
	}
	
	/**
	 * Affichage des lignes de la table Aeroport
	 * @param ResultSet rs
	 * @throws SQLException
	 */
	public void ligneAeroport(ResultSet rs) throws SQLException {

		int espAfter = 0;

		while (rs.next()) {

			// Retrieve by column name
			String idAeroport = rs.getString("idAeroport");
			String nomAeroprt = rs.getString("NomAeroprt");
			String nomVilleDesservie = rs.getString("NomVilleDesservie");

			//Configuration de la variable espAfter pour aligner les villes desservies
			espAfter = 16 - nomAeroprt.length();

			// Display values
			System.out.print("id: " + idAeroport);
			System.out.print(", Aeroprt: " + nomAeroprt + construitEsp(espAfter));
			System.out.println(", Ville desservie: " + nomVilleDesservie);

		}
		System.out.println(" ");
	}
	
	/**
	 * Affichage des lignes de la table Affectation
	 * @param ResultSet rs
	 * @throws SQLException
	 */
	public void ligneAffectation(ResultSet rs) throws SQLException {
		
		int espAfter = 0;

		while (rs.next()) {
		
			// Retrieve by column name
			String numVol = rs.getString("NumVol");
			String dateVol = rs.getString("DateVol");
			String numAvion = rs.getString("NumAvion");
			String prenomPilote = rs.getString("PrenomPilote");
			String nomPilote = rs.getString("NomPilote");
			
			//Configuration de la variable espAfter pour aligner les Pilotes
			espAfter = 5 - numAvion.length();

			// Display values
			System.out.print("N°" + numVol);
			System.out.print(", Affecté le: " + dateVol);
			System.out.print(" sur l'avion: " + numAvion + construitEsp(espAfter));
			
			//Reconfiguration de la variable espAfter pour aligner le Nom des Pilotes
			espAfter = 7 - prenomPilote.length();
			
			System.out.println(" piloté par : " + prenomPilote + construitEsp(espAfter) + nomPilote);
			System.out.println(" ");
		}
	}

	/**
	 * Fonction servant à ajouter un nombre nbEspace d'espaces
	 * @param nbEspace
	 * @return
	 */
	private String construitEsp(int nbEspace) {

		//initialisation des variables
		String espaces = "";
		int i = 0;
		
		//boucler tantque i est différent du paramêtre reçu
		while (i != nbEspace) {
			
			//Construction de la chaine d'espaces
			espaces = espaces + " ";
			
			//incrementation de la variable i
			i++;
		}
		
		//Renvoi de la chaine d'espaces constituée
		return espaces;
	}

}
