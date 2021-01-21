package compAero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Services {
	// JDBC driver INFOS
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	// infos de connexion à la base de donnée
	static final String DB_URL = "jdbc:mysql://localhost/compagnie";

	// Infos de la base de données
	static final String USER = "root";
	static final String PASS = "";

	/**
	 * connexion au service qui s'occupe de l'affichage
	 */
	Affichage af = new Affichage();

	/**
	 * Etabli la configuration de la connexion à la base de données.
	 * 
	 * @param reçoit la Connection conn à configurer
	 * @return la Connection conn configurée
	 */
	public Connection setConn(Connection conn) {
		try {

			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			// System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

		} catch (SQLException se) {

			// Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) {

			// Handle errors for Class.forName
			e.printStackTrace();
		}

		return conn;
	}

	/**
	 * Etabli le Statement et le renvoie configuré
	 * 
	 * @param Reçoit le Statement stmt à configurer
	 * @param Reçoit la Connection conn qui sert à la creation du Statement stmt
	 * @return le Statement stmt configuré
	 */
	public Statement getStmt(Statement stmt, Connection conn) {

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return stmt;
	}

	/**
	 * Affiche la liste des Pilotes
	 */
	public void afficheListePilotes() {

		Connection conn = null;
		Statement stmt = null;

		// configuration la conexion
		conn = setConn(conn);

		// configuration la déclaration
		stmt = getStmt(stmt, conn);

		// Construction de la requête à exécuter
		String sql = "SELECT * FROM Pilote";

		try {
			// Execution de la requête et récupération des données
			ResultSet rs = stmt.executeQuery(sql);

			// affichage du titre
			af.titre("Liste des Pilotes");

			// affichage des données
			af.lignePilotes(rs);

			// fermeture de l'ensemble de résultats
			rs.close();

			// fermeture de la déclaration
			stmt.close();

			// fermeture de la connexion à la base de données
			conn.close();

		} catch (SQLException se) {

			// Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
	}

	/**
	 * Ajout de pilote
	 */
	public void ajoutePilote() {

		// affichage de la liste des Pilotes
		afficheListePilotes();

		// initialisation de la variable qui va servir à la récupération de données de
		// la console
		BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));

		try {
			// récupération du prénom du pilote à ajouter
			System.out.println(" ");
			System.out.printf("Ajouter Prénom: ");
			String prenom = (String) entree.readLine();

			// récupération du nom du pilote à ajouter
			System.out.println(" ");
			System.out.printf("Ajouter Nom: ");
			String nom = (String) entree.readLine();

			Connection conn = null;
			Statement stmt = null;

			// configuration de la conexion
			conn = setConn(conn);

			// configuration la déclaration
			stmt = getStmt(stmt, conn);

			// Création e la requête sql qui va être exécutée
			String sql = "INSERT INTO Pilote(idPilote,NomPilote,PrenomPilote) VALUES (LAST_INSERT_ID(),'" + nom + "','"
					+ prenom + "')";

			try {
				// Execution de la requête
				stmt.executeUpdate(sql);

				// fermeture de la déclaration
				stmt.close();

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// fermeture de la connexion à la base de données
			conn.close();

		} catch (IOException | SQLException e) {

			e.printStackTrace();

		}
		// affichage de la réussite de l'ajout
		af.titre("Ajout de Pilote effectué");

		// affichage de la liste des pilotes après ajout
		afficheListePilotes();

		// retour au menu
		circulationMenu();
	}

	/**
	 * Affichage de la liste des Vols
	 * 
	 * @param searchVol servant à la recherche d'un Numéro de Vol
	 */
	public void listeDesVols(String searchVol) {

		Connection conn = null;
		Statement stmt = null;

		// initialisation de la variable qui va servir à la création de la condition de
		// recherche
		String conditionDeRecherche = "";

		// configuration de la conexion
		conn = setConn(conn);

		// configuration la déclaration
		stmt = getStmt(stmt, conn);

		// si un paramêtre existe configuration de la condition qui permêtra le filtrage
		// du vol
		if (searchVol != "") {
			conditionDeRecherche = " WHERE Vol.NumVol like('" + searchVol + "');";
		}

		// Construction de la requête à exécuter
		String sql = "SELECT " + "Vol.NumVol,"
				+ "(Select NomAeroprt from Aeroport WHERE Aeroport.idAeroport=Vol.AeroportDept) as AeroportDept,"
				+ "Vol.HDepart,"
				+ "(Select NomAeroprt from Aeroport WHERE Aeroport.idAeroport=Vol.AeroportArr) as AeroportArr,"
				+ "Vol.HArrivee " + "FROM Vol" + conditionDeRecherche;

		try {
			// Execution de la requête et récupération des données
			ResultSet rs = stmt.executeQuery(sql);

			// recuprération du nombre de ligne que contient la récupération de données
			int nblineRS = getRowCount(rs);

			// affichage du titre en fonction du nombre de ligne
			af.titreVol(nblineRS);

			// si un ou plusieurs vol ont été trouvés affichage des ou du vol
			if (nblineRS >= 1) {
				af.ligneVol(rs);
			}

			// fermeture de l'ensemble de résultats
			rs.close();

			// fermeture de la déclaration
			stmt.close();

			// fermeture de la connexion à la base de données
			conn.close();

		} catch (SQLException se) {

			// Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) {

			// Handle errors for Class.forName
			e.printStackTrace();
		}

		// retour au menu
		circulationMenu();
	}

	/**
	 * Affichage de la liste des Affectations
	 */
	public void listeDesAffectation() {

		Connection conn = null;
		Statement stmt = null;

		// configuration de la conexion
		conn = setConn(conn);

		// configuration la déclaration
		stmt = getStmt(stmt, conn);

		// Construction de la requête à exécuter
		String sql = "SELECT " + "Affectation.NumVol," + "Affectation.DateVol," + "Affectation.NumAvion,"
				+ "(Select PrenomPilote from Pilote WHERE  Pilote.idPilote = Affectation.idPilote) as PrenomPilote, "
				+ "(Select NomPilote from Pilote WHERE  Pilote.idPilote = Affectation.idPilote) as NomPilote "
				+ "from Affectation";

		try {
			// Execution de la requête et récupération des données
			ResultSet rs = stmt.executeQuery(sql);

			// affichage de la liste des affectations
			af.ligneAffectation(rs);

			// fermeture de l'ensemble de résultats
			rs.close();

			// fermeture de la déclaration
			stmt.close();

			// fermeture de la connexion à la base de données
			conn.close();

		} catch (SQLException se) {

			// Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) {

			// Handle errors for Class.forName
			e.printStackTrace();
		}
	}

	/**
	 * Fonction servant à récupéré le nombre de lignes d'un ensemble de résultats
	 * 
	 * @param recçoit le ResultSet resultSet à tester
	 * @return le nombre de ligne du ResultSet resultSet
	 */
	private int getRowCount(ResultSet resultSet) {

		// si l'ensemble de résultats est null renvoi 0
		if (resultSet == null) {
			return 0;
		}

		try {
			// positionnement sur le dernier enregistrement
			resultSet.last();

			// renvoi du numéro de ligne
			return resultSet.getRow();

		} catch (SQLException exp) {

			exp.printStackTrace();

		} finally {

			try {
				// si plantage repositionnement de l'ensemble de résultats
				resultSet.beforeFirst();

			} catch (SQLException exp) {
				exp.printStackTrace();
			}
		}
		// si la tentative de positionnement sur le dernier enregistrement echoue
		// renvoi 0
		return 0;
	}

	/**
	 * Recherche d'un Vol
	 */
	public void searchVols() {

		// affichage du titre
		af.titre("Recherche d'un vol");

		// initialisation des variables qui vont servir à la récupération de données de
		// la console
		String c = "";
		BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));

		try {
			// récupération du N° de Vol
			System.out.println(" ");
			System.out.printf("tapez votre N° de Vol(\"ITXXX\"): ");
			c = (String) entree.readLine();

			// Affichage du N° de Vol recherché
			listeDesVols(c);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// retour au menu
		circulationMenu();
	}

	/**
	 * Liste des Aéopoort par Ville
	 */
	public void listeDesAeroParVille() {

		Connection conn = null;
		Statement stmt = null;

		// configuration de la conexion
		conn = setConn(conn);

		// configuration la déclaration
		stmt = getStmt(stmt, conn);

		// Construction de la requête à exécuter
		String sql = "SELECT * from Aeroport ORDER BY NomVilleDesservie asc";

		try {
			// Execution de la requête et récupération des données
			ResultSet rs = stmt.executeQuery(sql);

			// affichage du titre
			af.titre("Liste des Aeroports par Ville");

			// affichage des ligne récupérées de la requête
			af.ligneAeroport(rs);

			// fermeture de l'ensemble de résultats
			rs.close();

			// fermeture de la déclaration
			stmt.close();

			// fermeture de la connexion à la base de données
			conn.close();

		} catch (SQLException se) {

			// Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) {

			// Handle errors for Class.forName
			e.printStackTrace();
		}

		// retour au menu
		circulationMenu();
	}

	/**
	 * Modification et suppression des Affectations
	 * 
	 * @param reçoit un boolean suppression qui déterminera si la fonction des en
	 *               mode suppresion ou modiffication
	 */
	public void modifierSupprimerAffectation(boolean suppression) {

		// Affichage du titre en fonction du boolean suppression
		if (suppression) {

			// affichage du titre de la suppression
			af.titre("Supression d'une Affectation");

		} else {

			// affichage du titre de la modification
			af.titre("Modifier la date d'une Affectation");

		}

		// Affichage de la liste des Affectations
		listeDesAffectation();

		// initialisation de la variable qui va servir à constrir la requête à exécuter
		String sql = "";

		// initialisation des variables qui vont servir à la récupération de données de
		// la console
		String nouvelleDateAModifier = "";
		BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));

		try {
			// récupération du N° de Vol
			System.out.printf("Entrez Le N° de Vol à Modifier(\"ITXXX\"): ");
			String numVolEnModification = (String) entree.readLine();

			// récupération du N° d'Avion
			System.out.println(" ");
			System.out.printf("quel N° d'Avion: ");
			String numAvionPourModifier = (String) entree.readLine();

			// récupération du Nom du Pilote
			System.out.println(" ");
			System.out.printf("De quel Nom de Pilote: ");
			String pilotePourModifier = (String) entree.readLine();

			// si la fonction n'est pas en mode suppression, récupération de la date à
			// modifier
			if (suppression == false) {

				System.out.println(" ");
				System.out.printf("Entrez La Nouvelle Date: ");
				nouvelleDateAModifier = (String) entree.readLine();

			}

			Connection conn = null;
			Statement stmt = null;

			// configuration de la conexion
			conn = setConn(conn);

			// configuration la déclaration
			stmt = getStmt(stmt, conn);

			// Construction du debut de la requête à exécuter
			String condition = " WHERE NumVol='" + numVolEnModification + "' and NumAvion='" + numAvionPourModifier
					+ "'" + " and idPilote=(Select idPilote from Pilote where NomPilote = '" + pilotePourModifier
					+ "')";

			// Construction de la fin de la requête à exécuter en fonction du mode
			// suppression ou modification
			if (suppression == false) {

				// Construction de la fin de la requête modification
				sql = "UPDATE Affectation SET DateVol='" + nouvelleDateAModifier + "'" + condition;

			} else {

				// Construction de la fin de la requête suppression
				sql = "DELETE FROM Affectation" + condition;
			}

			try {

				// Execution de la requête
				stmt.executeUpdate(sql);

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// fermeture de la déclaration
			stmt.close();

			// fermeture de la connexion à la base de données
			conn.close();

		} catch (SQLException se) {

			// Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) {

			// Handle errors for Class.forName
			e.printStackTrace();
		}

		// affichage de la liste des Affectations modifié
		listeDesAffectation();

		// affichage du message résultant de la modification
		if (suppression == false) {

			// affichage du message après la modification de la date
			af.titre("Modifier la date effectuée");

		} else {

			// affichage du message après la suppression d'une affectation
			af.titre("Suppression effectuée");
		}

		// retour au menu
		circulationMenu();
	}

	/**
	 * Fonction de la gestion du menu
	 */
	public void circulationMenu() {

		// initialisation des variables qui vont servir à la récupération de données de
		// la console
		String c = "";
		BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));

		// affichage du menu
		af.menu();

		try {
			// récupération du choix tapé
			System.out.println(" ");
			System.out.printf("tapez votre choix: ");

			c = (String) entree.readLine();

			// dispachage en fonction du choix
			switch (c) {

			case "1":
				ajoutePilote();
				break;

			case "2":
				listeDesVols("");
				break;

			case "3":
				searchVols();
				break;

			case "4":
				listeDesAeroParVille();
				break;

			case "5":
				modifierSupprimerAffectation(false);
				break;

			case "6":
				modifierSupprimerAffectation(true);
				break;

			case "7":
				break;

			default:
				circulationMenu();
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// si le choix est 7 dans le menu fin du programme
		af.titre("Goodbye!");
	}
}
