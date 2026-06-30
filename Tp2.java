import java.util.TreeMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;

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

    public static void main(String[] args) {
        try {
            BufferedReader entree = new BufferedReader(new FileReader(args[0]));
            BufferedWriter sortie = new BufferedWriter(new FileWriter(args[1]));
            
            TreeMap<String, Object[]> stock = new TreeMap<>(); //Str : Nom du médicament, Object[] : date d'expiration (Str), stocks (Int)
            TreeMap<String, Integer> commandes = new TreeMap<>(); //Str : nom du médicament, Int : nombre de médicaments à commander

            String ligne = entree.readLine();
            if(ligne==null) { // Si fichier vide. 
                entree.close();
                sortie.close();
                return;
            }
            while(ligne!=null) {
                System.out.println(ligne);
                String[] infos = ligne.trim().split("\s++"); // Tableau des données de chaque ligne.
                
                // InputStream "DATE" :
                if(infos[0]=="DATE") {
                    sortie.write(infos[1] + " OK");
                    sortie.newLine();
                    ligne = entree.readLine(); // Passage à ligne suivante.
                }
                
                // InputStream "APPROV" :
                if(infos[0]=="APPROV") {
                    ligne = entree.readLine(); // Passage à ligne suivante.
                    infos = ligne.trim().split("\s++");
                    
                    while(infos[0]!=";") {
                        stock.put(infos[0], new Object[]{infos[2], infos[1]});
                        ligne = entree.readLine(); // Passage à ligne suivante.
                        infos = ligne.trim().split("\s++");
                        sortie.write("APPROV OK");
                        sortie.newLine();
                    }
                }
                
                // InputStream "PRESCRIPTION" :
                if(infos[0]=="PRESCRIPTION") {
                    /*
                    int compteur = 1;
                    ligne = entree.readLine(); // Passage à ligne suivante.
                    infos = ligne.trim().split("\s++");
                    
                    while(infos[0]!=";") {
                        
                        ligne = entree.readLine(); // Passage à ligne suivante.
                        infos = ligne.trim().split("\s++");
                    }
                    */
                }
                
                
                // InputStream "STOCK" :


                
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
