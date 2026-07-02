import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Tp2 {
    /*
    Input :  
    transaction de la forme : 
        Réception d'une livraison de médicaments
        Prescription
        Stock de pharmacie
        date actuelle
        commande pour les médicaments manquants lors d'un changement de date actuelle
        
    Traitement :
        Si pas assez de médicaments en stock, mettre la demande dans les commandes
        Si assez : accepter la demande 
    
        Ajuster le stock de médicaments si prescription (perte) ou livraison (ajout)

        Pour chaque prescription : créer un identificateur unique.

        Médicaments possèdent une date d'expiration : si atteignent leur date, les supprimer

        Lors d'une demande, donner les médicaments les plus proches de leur date d'expiration
        si la date n'est pas supérieure à la date de fin de traitement complet (voir exemple p.2 du pdf)
        
        La pharmacie fournit l'entièreté des médicaments nécessaires au traitement d'un coup
    */

    private static String currentDate = "2000-01-01"; // Stockage de la date courante
    private static int idPrescription = 1; // Compteur unique pour l'ID de prescription

    public static void main(String[] args) {
        try {
            BufferedReader entree = new BufferedReader(new FileReader(args[0]));
            BufferedWriter sortie = new BufferedWriter(new FileWriter(args[1]));
            
            TreeMap<String, TreeMap<String, Integer>> stock = new TreeMap<>(); // String : nom du médicament, TreeMap : sous-arbre des lots triés par date d'expiration
            TreeMap<String, Integer> commandes = new TreeMap<>(); // String : nom du médicament, Integer : nombre de médicaments à commander

            String ligne = entree.readLine();
            if(ligne==null) { // si fichier vide. 
                entree.close();
                sortie.close();
                return;
            }
            
            while(ligne!=null) { // O(N) => N: nombre total de lignes du fichier d'entrée
                System.out.println(ligne);
                String[] infos = ligne.trim().split("\\s++"); // tableau des données de chaque ligne.
                
                if ((infos.length == 0) || infos[0].isEmpty()) {
                    ligne = entree.readLine();
                    continue; // ignorer les lignes vides
                }

                // InputStream "DATE" :
                /**
                 * COMPLEXITÉ TEMPORELLE: O(C + n * L)
                 * O(C) pour parcourir et afficher l'arbre des commandes accumulées
                 * O(n * L) pour nettoyer les lots expirés de tous les médicaments en stock
                 */
                if(infos[0].equals("DATE")) {
                    String newDate = infos[1].replace(";", "");
                    
                    if (commandes.isEmpty()) {
                        sortie.write(newDate + " OK");
                        sortie.newLine();
                    } else {
                        sortie.write(currentDate + " COMMANDES :");
                        sortie.newLine();
                        
                        // Affichage des commandes accumulées
                        for (Map.Entry<String, Integer> entry : commandes.entrySet()) { // O(C) => C: taille de l'arbre des commandes
                            sortie.write(entry.getKey() + " " + entry.getValue());
                            sortie.newLine();
                        }
                        
                        // Vider la liste après affichage
                        commandes.clear(); // O(C) pour nettoyer la structure des commandes
                    }
                    
                    currentDate = newDate;

                    // Nettoyage automatique des médicaments expirés
                    for (TreeMap<String, Integer> lots : stock.values()) { // O(n) => n: nombre de médicaments
                        lots.keySet().removeIf(dateExp -> dateExp.compareTo(currentDate) <= 0); // O(L) => L: nombre de lots par médicament
                    }
                    
                    ligne = entree.readLine(); 
                }
                
                // InputStream "APPROV" :
                /**
                 * COMPLEXITÉ TEMPORELLE: O(m * (log n + log L))
                 * O(m) pour parcourir les m lignes de livraison
                 * O(log n) pour chercher / insérer la clé médicament dans l'arbre principal
                 * O(log L) pour insérer / mettre à jour le lot dans l'arbre secondaire
                 */
                else if(infos[0].startsWith("APPROV")) {
                    ligne = entree.readLine();
                    if (ligne != null) {
                        infos = ligne.trim().split("\\s++");
                    }
                    
                    // Parcours des lignes de livraison jusqu'à la fin (par un point-virgule)
                    while((ligne != null) && !infos[0].equals(";")) { // O(m) => m: nombre de lignes de livraison
                        if (infos.length >= 3) {
                            String nom = infos[0];
                            int quantite = Integer.parseInt(infos[1]);
                            String expiration = infos[2].replace(";", "");

                            // Vérifier si le lot est expiré
                            if (expiration.compareTo(currentDate) > 0) {
                                stock.putIfAbsent(nom, new TreeMap<>()); // O(log n) pour chercher/insérer la clé médicament
                                TreeMap<String, Integer> lots = stock.get(nom); // O(log n) pour récupérer l'arbre secondaire
                                lots.put(expiration, lots.getOrDefault(expiration, 0) + quantite); // O(log L) pour insérer/mettre à jour le lot
                            }
                        }
                        
                        ligne = entree.readLine();
                        if (ligne != null) {
                            infos = ligne.trim().split("\\s++");
                        }
                    }
                    
                    sortie.write("APPROV OK");
                    sortie.newLine();
                    
                    ligne = entree.readLine();
                }
                
                // InputStream "PRESCRIPTION" :
                /**
                 * COMPLEXITÉ TEMPORELLE: O(m * (log n + L))
                 * O(m) pour parcourir les m lignes de prescriptions
                 * O(log n) pour chercher / insérer la clé médicament dans l'arbre principal
                 * O(L) pour chercher / insérer le lot dans l'arbre secondaire
                 */
                else if(infos[0].startsWith("PRESCRIPTION")) {
                    sortie.write("PRESCRIPTION " + idPrescription);
                    sortie.newLine();

                    idPrescription++; // prochain prescription
                    ligne = entree.readLine();
                    if (ligne != null) {
                        infos = ligne.trim().split("\\s++");
                    }
                    
                    // Parcours des lignes de prescriptions jusqu à la fin (par un point-virgule)
                    while((ligne != null) && !infos[0].equals(";")) { // O(m) => m: nombre de lignes de prescription
                        if (infos.length >= 3) {
                            String nom = infos[0];
                            int dose = Integer.parseInt(infos[1]);
                            int repetitions = Integer.parseInt(infos[2].replace(";", ""));
                            int quantiteRequise = dose * repetitions;
                            int disponible = 0;
                            
                            if (stock.containsKey(nom)) { // O(log n) pour vérifier l'existence du médicament
                                // Évaluation de la quantité disponible en stock pour le médicament demandé
                                for (Map.Entry<String, Integer> lot : stock.get(nom).entrySet()) { // O(L) parcours linéaire des L lots
                                    if (lot.getKey().compareTo(currentDate) > 0) {
                                        disponible += lot.getValue();
                                    }
                                }
                            }

                            // Vérifier si la quantité disponible est suffisante
                            if (disponible >= quantiteRequise) {
                                int restant = quantiteRequise;
                                TreeMap<String, Integer> lots = stock.get(nom); // O(log n) pour cibler le médicament
                                Object[] dates = lots.keySet().toArray(); // O(L) pour copier les clés des lots dans un tableau
                                
                                for (Object d : dates) { // O(L) pour parcourir les L lots du médicament
                                    String dateExp = (String) d;
                                    if (dateExp.compareTo(currentDate) <= 0) {
                                        continue; // lot expiré
                                    }

                                    int lot = lots.get(dateExp); // O(log L) pour lire la valeur du lot
                                    
                                    // Vérifier si la quantité de lot est suffisante
                                    if (lot <= restant) {
                                        restant -= lot;
                                        lots.remove(dateExp); // O(log L) pour supprimer le lot du stock
                                    } else {
                                        lots.put(dateExp, lot - restant); // O(log L) pour ajuster le stock restant
                                        restant = 0;
                                        break;
                                    }
                                }
                               
                                sortie.write(nom + " " + dose + " " + repetitions + " OK");
                            } else {
                                commandes.put(nom, commandes.getOrDefault(nom, 0) + quantiteRequise); // O(log C) insertion dans les commandes
                                sortie.write(nom + " " + dose + " " + repetitions + " COMMANDE");
                            }
                            
                            sortie.newLine();
                        }
                        
                        ligne = entree.readLine();
                        if (ligne != null) {
                            infos = ligne.trim().split("\\s++");
                        }
                    }
                    
                    ligne = entree.readLine();
                }
                
                // InputStream "STOCK" :
                /**
                 * COMPLEXITÉ TEMPORELLE: O(n * L)
                 * O(n) pour parcourir l'arbre global des médicaments
                 * O(L) pour parcourir les lots de chaque médicament
                 */
                else if(infos[0].startsWith("STOCK")) {
                    sortie.write("STOCK " + currentDate);
                    sortie.newLine();
                    
                    // Affichage des lots expirés
                    for (Map.Entry<String, TreeMap<String, Integer>> med : stock.entrySet()) { // O(n) pour parcourir tous les médicaments
                        String nom = med.getKey();
                        
                        for (Map.Entry<String, Integer> lot : med.getValue().entrySet()) { // O(L) pour parcourir les L lots de chaque médicament
                            // Vérifier si le lot est expiré et a une quantité positive
                            if ((lot.getKey().compareTo(currentDate) <= 0) && (lot.getValue() > 0)) {
                                sortie.write(nom + " " + lot.getValue() + " " + lot.getKey());
                                sortie.newLine();
                            }
                        }
                    }
                    
                    ligne = entree.readLine();
                } else {
                    ligne = entree.readLine();
                }
            }
            
            entree.close();
            sortie.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erreur (FileNotFoundException): " + e.getMessage());
         } catch (IOException f) {
            System.out.println("Erreur (IOException): " + f.getMessage());
         }
    }
}
