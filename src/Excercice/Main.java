package Excercice;

import java.io.File;

public class Main {

	//Procedimiento principal para establecer un cronograma de conferencias
	
	public static void main(String[] args) {  
		
		//Lee un archivo para obtener las conferencias y armar un cronograma para 2 días
		//considerando que el Almuerzo debe ser a las 12:00 y el Evento de Networking debe 
		//empezar a las 17:00
		
		File directorioActual = new File("");
		
		String pathArchivo = directorioActual.getAbsolutePath() + File.separator + "Conferences.txt";
		System.out.println("El arachivo Imput es : "+pathArchivo);
		
		new Process3(pathArchivo);

	}

}
