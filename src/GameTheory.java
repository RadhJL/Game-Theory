import java.util.Scanner;

public class GameTheory {
	
	
	public static void main(String[] args) {

		  Scanner in = new Scanner(System.in);
		  System.out.println("Donnez le nombre de joueurs");
		  int size = in.nextInt();
		  if(size < 2){
		  	System.out.println("Un jeu doit contenir au moins 2 joueurs");
			System.exit(0);	
		  }
		  Integer[] array = new Integer[size]; 
		  System.out.println("Donnez le nombre de strategie de chaque joueurs (I-eme entier correspond au I-eme joueur)");
		  int i = 0;
		  while(size-- > 0)
		  {
			int stra = in.nextInt();
			if(stra < 1){
				System.out.println("Nombre de strategies des joueurs doit etre superieur a 1");
				System.exit(0);	
			}
			array[i++] = stra; 
		   }
		  Game jeu = new Game(array);

		  //Remplissage des gains de chaque profile de strategies
		  jeu.Fill();
		  System.out.println("\nQuestion 1:\n");
		  System.out.println(jeu.isNull() ? "Jeu à somme nulle":"Jeu n'est pas à somme nulle"); 
		  System.out.println("\nQuestion 2:\n");
		  jeu.NashEquilibrium();
		  System.out.println("\nQuestion 3:\n");
		  if(!jeu.DominantStrategie())
		  	System.out.println("Il n'y a pas de strategie strategie dominantes");
		  
		  in.close();
		  System.out.println();
	}

}
