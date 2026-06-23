import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void main(String[] args) {
        try {
            BufferedReader entree = new BufferedReader(new FileReader(args[0]));
            BufferedWriter sortie = new BufferedWriter(new FileWriter(args[1]));
            
            TreeMap<String, TreeMap<String, Integer>> stock = new TreeMap<>(); //Premier Str : Nom du médicament, Deuxième Str : date d'expiration, Int : nombre en stock
            TreeMap<String, Integer> commandes = new TreeMap<>(); //Str : nom du médicament, Int : nombre de médicaments à commander

            String ligne = entree.readLine();
            while(ligne!=null) {
                System.out.println(ligne);
                ligne = entree.readLine();
            }

            // InputStream "DATE" :
            
            // InputStream "PRESCRIPTION" :

            // InputStream "APPROV" :
            
            // InputStream "STOCK" :
            
            entree.close();
            sortie.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erreur (FileNotFoundException): " + e.getMessage());
         } catch (IOException f) {
            System.out.println("Erreur (IOException): " + f.getMessage());
         }

    }
    
}
