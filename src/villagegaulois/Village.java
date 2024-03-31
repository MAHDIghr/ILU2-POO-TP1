package villagegaulois;

import java.util.Iterator;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtalsDuMarche) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(nbEtalsDuMarche);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef " + chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom() + " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}

	// classe interne Marché
	private class Marche {
		private Etal[] etals;

		public Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
		}

		private void utiliserEtalLibre(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}

		private int trouverEtalLibre() {
			for (int i = 0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe()) {
					return i;
				}
			}
			return -1;
		}

		private Etal[] trouverEtals(String produit) {
			int nbEtalsAvecProduit = 0;
			for (int i = 0; i < etals.length; i++) {
				if (this.etals[i].isEtalOccupe() && this.etals[i].contientProduit(produit))
					nbEtalsAvecProduit++;
			}
			Etal[] etalsAvecProduits = null;
			if (nbEtalsAvecProduit > 0) {
				etalsAvecProduits = new Etal[nbEtalsAvecProduit];
				int j = 0;
				for (int i = 0; i < etals.length; i++) {
					if (this.etals[i].isEtalOccupe() && this.etals[i].contientProduit(produit)) {
						etalsAvecProduits[j] = etals[i];
						j++;
					}
				}
			}

			return etalsAvecProduits;
		}

		private Etal trouverVendeur(Gaulois vendeur) {
			for (int i = 0; i < this.etals.length; i++) {
				if (this.etals[i].getVendeur() == vendeur) {
					return etals[i];
				}
			}
			return null;
		}

		private String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			String chaineArecuperer;
			int nbEtalVide = 0;
			for (int i = 0; i < this.etals.length; i++) {
				if (this.etals[i].isEtalOccupe()) {
					chaineArecuperer = etals[i].afficherEtal();
					chaineArecuperer = chaineArecuperer.replace("L'étal de ", "");
					chaineArecuperer = chaineArecuperer.replace(" est garni de ", " vend ");
					chaine.append(chaineArecuperer);
				} else {
					nbEtalVide++;
				}
			}

			if (nbEtalVide > 0)
				chaine.append("Il reste " + nbEtalVide + " etals non utilises dans le marche.\n");

			return chaine.toString();
		}

	}

	// question 2

	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] etalsVendeursDuProduit = marche.trouverEtals(produit);
		if (etalsVendeursDuProduit == null) {
			chaine.append("Il n'y a pas de vendeur qui propose des fleurs au marché.\n");
		} else if (etalsVendeursDuProduit.length == 1) {
			chaine.append("Seul le vendeur " + etalsVendeursDuProduit[0].getVendeur().getNom() + " propose des "
					+ produit + " au marché\n");
		} else {
			chaine.append("Les vendeurs qui proposent des fleurs sont :\n");
			for (int i = 0; i < etalsVendeursDuProduit.length; i++) {
				chaine.append("- " + etalsVendeursDuProduit[i].getVendeur().getNom() + "\n");
			}
		}
		return chaine.toString();
	}

	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		int indiceEtalLibre = marche.trouverEtalLibre();
		if (indiceEtalLibre == -1) {
			chaine.append("il n'y a plus d etal libre");
		} else {
			marche.utiliserEtalLibre(indiceEtalLibre, vendeur, produit, nbProduit);
			chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°"
					+ (indiceEtalLibre + 1) + ".\n");
		}

		return chaine.toString();
	}

	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}

	public String partirVendeur(Gaulois vendeur) {
		Etal etalDuVendeur = marche.trouverVendeur(vendeur);
		return etalDuVendeur.libererEtal();
	}

	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Le marché du village " + this.getNom() + " possède plusieurs étals :\n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}

}