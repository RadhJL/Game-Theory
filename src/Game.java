import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Comparator;


public class Game {
	
	private static final int MAX = -Integer.MAX_VALUE;
	Map<ArrayList<Integer>,ArrayList<Integer>> map; 
	ArrayList<Integer> nb_stra ;
	ArrayList<ArrayList<Integer>> all_strategies;
	ArrayList<Integer> indices;
	Integer nb_stra_total;
	Integer nb_joueurs;
	boolean stra_pure = false;
	
	Game(Integer[] nb_strategies){

		nb_stra = new ArrayList<Integer>();
        Collections.addAll(nb_stra , nb_strategies);
		nb_joueurs = nb_stra.size();
		
		map = new LinkedHashMap<ArrayList<Integer>,ArrayList<Integer>>(); 
		all_strategies = GenerateAllStrategies(nb_stra,0,new ArrayList<Integer>(),new ArrayList<ArrayList<Integer>>());

		indices = new ArrayList<Integer>();
		for(int i = 0;i< nb_joueurs;i++) 
			indices.add(1);
		
		nb_stra_total = 1;   
		for(Integer i:nb_stra) 
			nb_stra_total*=i;

	}

	@SuppressWarnings("resource")
	void Fill() {
		Integer index = 0;
		Scanner in = new Scanner(System.in);

		Collections.sort(all_strategies, new IntegerListComparator());
		for(int i=0; i < nb_stra_total ; i++)
		{
			ArrayList<Integer> tmp = all_strategies.get(index++);
			
			System.out.print("Donnez les gains de l'ensemble de strategies ");
			for(int j = 0;j < nb_joueurs;j++)
				System.out.print("["+tmp.get(j)+"]");
			
			System.out.println();
			
			ArrayList<Integer> input = new ArrayList<Integer>();
			for(int j = 0;j < nb_joueurs;j++)
				input.add(in.nextInt());
			map.put(tmp, input);
		}
		Show();
	}

	void Show(){
		System.out.println("\n************************ Affichage **************************\n");
		 for(Map.Entry<ArrayList<Integer>,ArrayList<Integer>> m:map.entrySet()){  
			   ArrayList<Integer> joueurs = (ArrayList<Integer>) m.getKey();
			   ArrayList<Integer> gains = (ArrayList<Integer>) m.getValue();
			   System.out.print("L'ensemble de strategies ");
			   for(Integer i:joueurs) 
				   System.out.print("["+i+"]");
			   System.out.print(" a ");
			   System.out.print("(");
			   int j = 0;
			  for(Integer i:gains){
				if(j == gains.size() - 1)
					System.out.print(i);
				else
					System.out.print(i+", ");
			   j ++; 
			} 
			  System.out.println(") comme gains");
		  } 
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> GenerateAllStrategies(ArrayList<Integer> nb_stra, int id_joueurs, ArrayList<Integer> stra, ArrayList<ArrayList<Integer>> result) {
		
		if(!(id_joueurs>=nb_stra.size()))
			for(Integer i = 1; i <= nb_stra.get(id_joueurs);i++ ) {
				if(stra.size()<= id_joueurs)
				stra.add(i);
				else 
				stra.set(id_joueurs,i);
				
				GenerateAllStrategies(nb_stra,id_joueurs+1,stra,result);
				
				if(id_joueurs == nb_stra.size()-1) {
					ArrayList<Integer> tmp = new ArrayList<Integer>();
					tmp =  (ArrayList<Integer>) stra.clone();
					result.add(tmp);
				}
			}
		return result;
	}

	boolean isNull() {
		  int sum;
		  for(Map.Entry<ArrayList<Integer>,ArrayList<Integer>> m: map.entrySet())
		  {  
			   sum = 0;
			   ArrayList<Integer> tmp = (ArrayList<Integer>) m.getValue();
			   for(Integer i:tmp)
				   	sum +=i;  
			   
				if (sum != 0)
					return false;
		   }  
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	void bestResponse() {
		Map<ArrayList<Integer>,ArrayList<Boolean>> mark = new LinkedHashMap<ArrayList<Integer>,ArrayList<Boolean>>();
		
		for(int i=0 ; i< nb_joueurs; i++)
		{	
			ArrayList<Integer> tmp = (ArrayList<Integer>) nb_stra.clone();
			tmp.set(i,1);
			ArrayList<ArrayList<Integer>> stra_player = GenerateAllStrategies(tmp,0,new ArrayList<Integer>(),new ArrayList<ArrayList<Integer>>());
			int w = 0;
			
			for(int s = 0 ; s < stra_player.size(); s++)
			{
				int max = MAX;
				ArrayList<Integer> stra = stra_player.get(w);
				w++;
				
				for(int j = 1 ; j <=  nb_stra.get(i); j++) {
					stra.set(i, j);
					int g = map.get(stra).get(i);
					if(g > max)
						max = g;
				}
				
				for(int j = 1 ; j <=  nb_stra.get(i); j++) {
					stra.set(i, j);
					int g = map.get(stra).get(i);
					if(g == max) {
						ArrayList<Boolean> bool= new ArrayList<Boolean>();
						if(mark.containsKey(stra)) 
						{	
							bool= mark.get(stra);
							bool.set(i, true);
							mark.replace((ArrayList<Integer>) stra.clone(),bool);
						}
						else
						{
							for(int k =0 ; k< nb_joueurs; k ++) 
								bool.add(false);
								bool.set(i, true);
								mark.put((ArrayList<Integer>) stra.clone(),bool);
								
						}
					}
				}
				max = -MAX;
			}
		}
		mark.forEach((strategie, flag) -> {
			boolean tmp_flag = true;
			for(Boolean i:flag)  
				if(i == false)
				{
					tmp_flag = false;
					break;
				}
			
			if(tmp_flag)
			{
				System.out.print("Equilibre de nash en strategie pure :");
				System.out.print("Le profil ");
				for(Integer i:strategie)
					System.out.print("["+i+"]");
				System.out.print(" avec gains ");
				
				int j =0;
				System.out.print("(");
				for(Integer i:map.get(strategie)){
					if( j == map.get(strategie).size() - 1)
						System.out.print(i);
					else 
						System.out.print(i+", ");
					j++;
				} 
				System.out.println(")");
				
				stra_pure = true;
			}
		});
		
	}
	
	void mixedStrategie(){
		
		float a,b,a1,b1;
		boolean exist = false;
		System.out.println("\n");
		a=map.get(Arrays.asList(1,1)).get(1);
		b=map.get(Arrays.asList(2,1)).get(1);
		a1=map.get(Arrays.asList(1,2)).get(1);
		b1=map.get(Arrays.asList(2,2)).get(1);
		
		float x = (b1-b)/(float)(a-a1-b+b1);
		float y = 1 - x;
		
		if(y<1 && x < 1){
			exist = true;
			System.out.println("Equilibre de nash mixte du premier joueur  = ("+x+", "+y+")");
		}

		a=map.get(Arrays.asList(1,1)).get(0);
		b=map.get(Arrays.asList(1,2)).get(0);
		a1=map.get(Arrays.asList(2,1)).get(0);
		b1=map.get(Arrays.asList(2,2)).get(0);
	  
		x = (b1-b)/(float)(a-a1-b+b1);
		y = 1 - x;

		if(y<1 && x <1){
			exist = true;
			System.out.println("Equilibre de nash mixte du deuxieme Joueur  = ("+x+", "+y+")");
		}
		if(!exist){
			System.out.println("Il n'y a pas d'equilibre de nash en strategie mixte");
		}
	}

	void NashEquilibrium()
	{
		bestResponse();
		if(!stra_pure) 
			System.out.println("Ce jeu n'admet pas de d'equilibre de nash en strategie pure.");
		
		if(nb_joueurs == 2 && nb_stra.get(0) == 2 &&   nb_stra.get(1) == 2 )
		{
				mixedStrategie();
		}			
	}

	@SuppressWarnings("unchecked")
	boolean StriclyDominanted(ArrayList<ArrayList<Integer>>  stra_player,int player_id,int id_strategy0, int id_strategy1, boolean display)
	{
		 for(ArrayList<Integer>  array:stra_player)
		 {
			 ArrayList<Integer> strategie_0 = (ArrayList<Integer>) array.clone();
			 ArrayList<Integer> strategie_1 = (ArrayList<Integer>) array.clone();
			 strategie_0.set(player_id, id_strategy0);
			 strategie_1.set(player_id, id_strategy1);
			 if(map.get(strategie_0).get(player_id) <= map.get(strategie_1).get(player_id))
				 return false;
		}
		 if(display)
		 System.out.println("La strategie "+ id_strategy1 +" du joueur "+ (player_id+1) +" est strictement dominée par la strategie "+id_strategy0);
		 
		return true;
	}
	
	@SuppressWarnings("unchecked")
	boolean WeaklyDominanted(ArrayList<ArrayList<Integer>>  stra_player,int player_id,int id_strategy0, int id_strategy1)
	{
		 int equal_count = 0;
		 
		 for(ArrayList<Integer>  array:stra_player)
		 {
			 ArrayList<Integer> strategie_0 = (ArrayList<Integer>) array.clone();
			 ArrayList<Integer> strategie_1 = (ArrayList<Integer>) array.clone();
			 strategie_0.set(player_id, id_strategy0);
			 strategie_1.set(player_id, id_strategy1);
			 if(map.get(strategie_0).get(player_id) < map.get(strategie_1).get(player_id))
				 return false;
			 
			if (map.get(strategie_0).get(player_id) == map.get(strategie_1).get(player_id))
				 equal_count ++ ;
		 }
		 
		 if(equal_count > 0 ) {
			 System.out.println("La strategie "+ id_strategy1 +" du joueur "+ (player_id+1) +" est faiblement dominée par la strategie "+id_strategy0);
			 return true;
		 }
		return false;
	}
	
	@SuppressWarnings("unchecked")
	boolean DominantStrategie(){
		boolean exist = false;
		for(int i=0 ; i< nb_joueurs; i++)
		{	
			ArrayList<Integer> tmp = (ArrayList<Integer>) nb_stra.clone();
			tmp.set(i,1);
			ArrayList<ArrayList<Integer>> stra_player = GenerateAllStrategies(tmp,0,new ArrayList<Integer>(),new ArrayList<ArrayList<Integer>>());
			for(int j = 1 ; j <=  nb_stra.get(i); j++)
			{	
				boolean tmp_flag = true;
				for(int k = 1 ; k <=  nb_stra.get(i); k++)
				{   
					if(k == j)
						continue;
					if(!StriclyDominanted(stra_player,i,j,k,true))
						tmp_flag = false;
				}
				if(tmp_flag) {
					System.out.println("La strategie "+ j +" du joueur "+ (i+1) +" est dominante ");
					exist = true;
					break;
				}
				
			}
				
		}
		
		for(int i=0 ; i< nb_joueurs; i++)
		{	
			ArrayList<Integer> tmp = (ArrayList<Integer>) nb_stra.clone();
			tmp.set(i,1);
			ArrayList<ArrayList<Integer>> stra_player = GenerateAllStrategies(tmp,0,new ArrayList<Integer>(),new ArrayList<ArrayList<Integer>>());
			for(int j = 1 ; j <=  nb_stra.get(i); j++)
			{	
				
				boolean tmp_flag = true;
				int weak_count = 0;
				
				for(int k = 1 ; k <=  nb_stra.get(i); k++)
				{   
					if(k == j)
						continue;
					boolean strict_flag = StriclyDominanted(stra_player,i,j,k,false);
					boolean weak_flag = WeaklyDominanted(stra_player,i,j,k);
					if( strict_flag == false  && weak_flag == false )
					{
						tmp_flag = false;
						break;
					}
					if(weak_flag == true)
						weak_count ++ ;
				}
				if(tmp_flag && weak_count > 0) {
					System.out.println("La strategie "+ j +" du joueur "+ (i+1) +" est faiblement dominante ");
					exist = true;
					break;
				}
				
			}
				
		}
		return exist;
	}
}

final class IntegerListComparator implements Comparator<List< Integer >> {
	@Override
	public int compare(List<Integer> a, List<Integer> b) {
		for(int i = a.size() -1 ; i >= 0; --i){
			if(a.get(i) != b.get(i))
				return a.get(i).compareTo(b.get(i));
		}
		return 0;
	}
}
