package br.edu.cefsa.svg;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	private static void saveOnDisk(String path, String data) throws IOException{
		byte[] bytes = data.getBytes();
		FileOutputStream out = new FileOutputStream(path);

		out.write(bytes);
		out.close();
	}

	public static void main(String[] args) {
		try {
                        Scanner scan = new Scanner(System.in);
                        System.out.printf("Arquivos de texto pré gravados: input1 até input11\n");
                        System.out.printf("Informe o nome de arquivo texto: ");
                        String nome = scan.nextLine();
                        
			DolSystem dolSystem = new DolSystem(System.getProperty("user.dir") + "/inputs/" + nome + ".txt");

			SvgGenerator svgGenerator = new SvgGenerator(dolSystem);
			String svg = svgGenerator.generateSvg();
	
			saveOnDisk(System.getProperty("user.dir") + "/output.svg", svg);
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
