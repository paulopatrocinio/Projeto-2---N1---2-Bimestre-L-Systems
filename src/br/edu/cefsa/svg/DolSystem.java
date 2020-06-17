package br.edu.cefsa.svg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DolSystem {
	private String initiator;
	private HashMap<String, String> generators = new HashMap<String, String>();
	private int n;
	private int d;
	private int a;
	
	public DolSystem(String path)throws IOException {
		List<String> content = Files.readAllLines(Paths.get(path));
		
		if(content.size() < 2)
			throw new IOException("O arquivo deve ter pelo menos 2 linhas");
		
		if(!Pattern.compile("^n=[0-9]+, d=[0-9]{1,3}(, a=[0-9]{1,3})?$").matcher(content.get(0)).matches()) {
			throw new IOException("A linha de declaração das constantes não está no padrão");
		}else {	
			String[] firstLineData = content.get(0).split(", ");
			this.n = Integer.parseInt(firstLineData[0].substring(2, firstLineData[0].length()));
			this.d = Integer.parseInt(firstLineData[1].substring(2, firstLineData[1].length())) % 360;
			this.a = 360 - ((firstLineData.length > 2) ? Integer.parseInt(firstLineData[2].substring(2, firstLineData[2].length())) % 360 : 90);
			//O ângulo é invertido porque o eixo Y no HTML tem as referências de cima para baixo
		}
		
		
		
		if(!Pattern.compile("^[Ff+-]+$").matcher(content.get(1)).matches()) {
			throw new IOException("O Inicializador não segue está no padrão \"pattern\"");
		}else {
			this.initiator = content.get(1);
		}
		
		
		for(int i = 2; i < content.size(); i++){
			if(!Pattern.compile("^[Ff+-]{1}=[Ff+-]+$").matcher(content.get(i)).matches()) 
				throw new IOException(String.format("O Gerador na linha %s não segue o padrão \"patern\"", i + 1));
			
			String[] generator = content.get(i).split("=");
			
			if(this.generators.containsKey(generator[0])) 
				throw new IOException(String.format("Exixte mais de um Gerador com \"%s\" key", generator[0]));
			
			this.generators.put(generator[0], generator[1]);
		}
			
	}
	
	public int getIncrementAngle(){
		return this.d;
	}
	
	public int getStarterAngle() {
		return this.a;
	}
	
	public String getTurtle(){
		String lSystem = this.initiator;
		String[] keys = this.generators.keySet().toArray(new String[this.generators.size()]);
		Pattern p = Pattern.compile(this.stringJoin(keys, "|"));
		
		for(int i = 0; i < this.n; i++){
			Matcher m = p.matcher(lSystem);
			StringBuffer sb = new StringBuffer();
			
			while (m.find())
			    m.appendReplacement(sb, this.generators.get(m.group()));
			m.appendTail(sb);
			
			lSystem = sb.toString();
		}
		return lSystem;
	}
	
	private String stringJoin(String[] array, String separator){
		String joined = "";
		
		if(array.length > 0)
			joined = array[0];
		
		for(int i = 1; i < array.length; i++)
			joined += separator + array[i];
		
		return joined;
	}
}