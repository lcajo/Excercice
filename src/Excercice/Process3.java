package Excercice;


import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Arrays;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;


public class Process3 {
	/*Clase que permite leer los eventos que se encuentran en un archivo con su duración para establecer un calendario*/
	private String[] data=new String[19];
	int[][] arreglo=new int[19][2];
	int[][] arregloResultado = new int[19][2];
	int[][] arregloTmp = new int[10][2];



	public Process3 (String filename) {
        String line;
    	String hora=null;
    	String horaInicio;
    	String horaInicioTmp=null;
    	String horaMax;
    	int flag=0;
    	int j=0;
    	int k=0;
        int rows = 0;
        int duracion = 0;
        LocalTime hora1;
        LocalTime hora2;
        LocalTime hora3;
  	
    	
    	
        try {
           //Leer el archivo         	
           BufferedReader reader = new BufferedReader(new FileReader(filename));
                       
           while ((line = reader.readLine()) != null) {     
        	   
               // Creamos un patrón para buscar números con decimales en la cadena
               Pattern patron = Pattern.compile("\\d+(\\.\\d+)?");
               Matcher matcher = patron.matcher(line);

               // Buscamos la primera coincidencia numérica en la cadena
               if (matcher.find()) {
                   String numero = matcher.group();

                   duracion=Integer.parseInt(numero);
               } else {
            	   duracion=5;
               }
        	   
               data[rows]= line;                  
			   arreglo[rows][0]=rows;
			   arreglo[rows][1]=duracion;             		

               rows++;
            
          }  
          reader.close();     
         
          
          //Procesa el arreglo resultante
          arregloResultado = leeArreglo(arreglo);
          
          //Presentar en consola el resultado 
          System.out.println("Test output:");
          System.out.println("Track 1:");
          horaInicio="09:00";
          horaMax="12:00";
          
          for (int i = 0; i < arregloResultado.length; i++) {
        	  
        	  hora = horaInicio;
        	  duracion=arregloResultado[i][1];
  	          horaInicio = sumarMinutosAHora(hora,duracion); 
	          hora3 = LocalTime.parse(horaInicio);
  	          hora1 = LocalTime.parse(hora);
	          hora2 = LocalTime.parse(horaMax);  	          
	          
        	  
        	  if ( arregloResultado[i][1]==5 || (k==1 && hora3.isAfter(hora2)&&!hora1.equals(hora2))) {
	        	  arregloTmp[j][0]=arregloResultado[i][0];
	        	  arregloTmp[j][1]=arregloResultado[i][1];  
	        	  horaInicio=horaInicioTmp;
                  
	        	  j++;	        		  
        	  }else {        	         	  

		          if (hora1.equals(hora2)) {
		        	  if (flag==0) {
		    	        	System.out.println(hora + "   " + "*Lunch*");

		      	        	hora="01:00";
		      	        	horaMax="05:00";
		      	        	flag=1;
		      	        	
		        	  }else {
		    	        	System.out.println(hora + "   " + "Networking Event");
		    	        	System.out.println("Track 2:");
		      	        	hora="09:00";
		      	        	horaMax="12:00";
		      	        	flag=0;	
		      	        	k=1;
		        	  }
		            hora1 = LocalTime.parse(hora);
		            hora2 = LocalTime.parse(horaMax);  

	  	          }
	        	  if (hora1.isBefore(hora2) && !hora1.equals(hora2)) {
	  	        	  System.out.println(hora + "   " + data[arregloResultado[i][0]]);
	  	        	  horaInicioTmp=horaInicio;
	    	          horaInicio = sumarMinutosAHora(hora,arregloResultado[i][1]);
	        	  }  
	          }

  	     }  
          
          for (int l = arregloTmp.length-1; l>=0; l--) {
        	  hora = horaInicio;
        	  duracion=arregloTmp[l][1];
  	          horaInicio = sumarMinutosAHora(hora,duracion); 
  	          if (arregloTmp[l][0]!=0)  {
  	        	System.out.println(hora + "   " + data[arregloTmp[l][0]]);  
  	          }
        	  
          }
          hora = sumarMinutosAHora(hora,duracion);
    	  System.out.println("05:00   " + "Networking Event");
    	  
          
        	  
          
          
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception");   
            
        }		
	}	
 
	
	public String[] getData() {
        return data;
    }
 
    public static int [][] leeArreglo(int[][] arreglo) {
        
        // agrupar los elementos del arreglo según las especificaciones dadas
        List<List<Integer>> grupos = agruparElementos(arreglo, new int[] {540, 780}, new int[] {720, 960});
        
        // crear un nuevo arreglo ordenado con los subarreglos agrupados
        int[][] arregloOrdenado = new int[arreglo.length][2];
        int indice = 0;
        for (List<Integer> grupo : grupos) {
            // crear un subarreglo temporal para el grupo actual
            int[][] subarreglo = new int[grupo.size()][2];
            for (int i = 0; i < grupo.size(); i++) {
                int indiceOriginal = grupo.get(i);
                subarreglo[i][0] = arreglo[indiceOriginal][0];
                subarreglo[i][1] = arreglo[indiceOriginal][1];
            }
            
            // ordenar el subarreglo por el segundo valor (en orden descendente)
            Arrays.sort(subarreglo, (a, b) -> Integer.compare(b[1], a[1]));
            
            // añadir el subarreglo ordenado al nuevo arreglo ordenado
            for (int i = 0; i < subarreglo.length; i++) {
                arregloOrdenado[indice][0] = subarreglo[i][0];
                arregloOrdenado[indice][1] = subarreglo[i][1];
                indice++;
            }
        }
        
        return arregloOrdenado;
    }  
    public static List<List<Integer>> agruparElementos(int[][] arreglo, int[] limitesInferiores, int[] limitesSuperiores) {
    	//Método que permite ordenar un arreglo considerando condiciones
    	
    	
        // crear una lista de grupos vacía
        List<List<Integer>> grupos = new ArrayList<>();
        
        // crear una lista de índices de elementos no usados
        List<Integer> indicesNoUsados = new ArrayList<>();
        for (int i = 0; i < arreglo.length; i++) {
            indicesNoUsados.add(i);
        }
        
        // recorrer cada rango de límites y agrupar los elementos que cumplan las condiciones
        for (int i = 0; i < limitesInferiores.length; i++) {
            int limiteInferior = limitesInferiores[i];
            int limiteSuperior = limitesSuperiores[i];
            List<Integer> grupoActual = new ArrayList<>();
            int sumaActual = 0;
            
            // buscar elementos que cumplan las condiciones y añadirlos al grupo
            for (int j = 0; j < indicesNoUsados.size(); j++) {
                int indiceActual = indicesNoUsados.get(j);
                int valorActual = arreglo[indiceActual][1];
                
                // si el valor actual cumple las condiciones, añadirlo al grupo
                if (sumaActual + valorActual <= limiteSuperior) {
                    grupoActual.add(indiceActual);
                    sumaActual += valorActual;
                }
                
                // si se ha llegado al límite superior, añadir el grupo y empezar uno nuevo
                if (sumaActual == limiteSuperior) {
                    grupos.add(grupoActual);
                    indicesNoUsados.removeAll(grupoActual);
                    break;
                }
                
                // si se ha llegado al límite inferior, añadir el grupo y empezar uno nuevo
                if (sumaActual >= limiteInferior) {
                    grupos.add(grupoActual);
                    indicesNoUsados.removeAll(grupoActual);
                    grupoActual = new ArrayList<>();
                    sumaActual = 0;
                    j--; // retroceder para volver a considerar el elemento actual en el nuevo grupo
                }
            }
            
            // si no se ha llegado al límite superior ni inferior, añadir el grupo actual
            if (grupoActual.size() > 0) {
                grupos.add(grupoActual);
                indicesNoUsados.removeAll(grupoActual);
            }
        }
        
        return grupos;
    }

    public static String sumarMinutosAHora(String hora, int minutos) {
    	//Método que permite sumar minutos a una hora
        String[] h = hora.split(":");
        int horas = Integer.parseInt(h[0]);
        int minutosTotales = Integer.parseInt(h[1]) + minutos;
        horas += minutosTotales / 60;
        minutosTotales %= 60;
        return String.format("%02d:%02d", horas, minutosTotales);
    }


          
}

