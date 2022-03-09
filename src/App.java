import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws Exception {
        File arquivo = new File(args[0]);
        FileReader ler = new FileReader(arquivo); 
        BufferedReader origem = new BufferedReader(ler);

        String[] newArquivo = arquivo.getName().split(Pattern.quote("."));
        OutputStream os = new FileOutputStream(newArquivo[0]+"-check."+newArquivo[1]);
        Writer wr = new OutputStreamWriter(os);
        BufferedWriter br = new BufferedWriter(wr);

        Stack<String> pilhaarquivo = new Stack<String>();
        String linha = null;
        Map<String, Integer> brackets = new HashMap<String, Integer>();
        List<String> nexts = new ArrayList<String>();
        String[] ilegalStart = {"(","[","{"};
        brackets.put("Bracket", 0);
        brackets.put("Brace", 0);
        brackets.put("Key", 0);

        while ((linha = origem.readLine()) != null) {
            for(int i = 0; i < linha.length(); i++){
                pilhaarquivo.push(""+linha.charAt(i));
                br.write(""+linha.charAt(i));
            }
            while(!pilhaarquivo.empty()){
                String element = pilhaarquivo.pop();
                try {
                    switch (element) {
                        case "(":
                            if(nexts.get(nexts.size()-1) != "(") nexts.clear();
                            else {
                                nexts.remove(nexts.size()-1);
                                brackets.put("Bracket", brackets.get("Bracket")+1);
                            }
                            break;
                        case "[":
                            if(nexts.get(nexts.size()-1) != "[") nexts.clear();
                            else { 
                                nexts.remove(nexts.size()-1);
                                brackets.put("Brace", brackets.get("Brace")+1);
                            }
                            break;
                        case "{":
                            if(nexts.get(nexts.size()-1) != "{") nexts.clear();
                            else {
                                nexts.remove(nexts.size()-1);
                                brackets.put("Key", brackets.get("Key")+1);
                            }
                            break;
                        case ")":
                            nexts.add("(");
                            brackets.put("Bracket", brackets.get("Bracket")-1);
                            break;
                        case "]":
                            nexts.add("[");
                            brackets.put("Brace", brackets.get("Brace")-1);
                            break;
                        case "}":
                            nexts.add("{");
                            brackets.put("Key", brackets.get("Key")-1);
                            break;
                    }
                } catch (Exception e) {
                    boolean contains = Arrays.stream(ilegalStart).anyMatch(element::equals);
                    if(nexts.isEmpty() && contains && !pilhaarquivo.isEmpty()){
                        nexts.add("Error");
                        pilhaarquivo.clear();
                        brackets.put("Bracket", 0);
                        brackets.put("Brace", 0);
                        brackets.put("Key", 0);
                    }
                }
            };
            if(brackets.get("Bracket")==0 && brackets.get("Brace")==0 && brackets.get("Key")==0 && nexts.size()==0){
                br.write(" - OK");
            }
            else{
                br.write(" - Invalido");
            }
            nexts.clear();
            br.newLine();
        }
        origem.close();
        br.close();
    }
}
