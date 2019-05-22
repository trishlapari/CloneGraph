package CloneGraph;

public class PreetyPrintClass {
    
public static String PreetyPrint( String s ) {
   StringBuilder sb = new StringBuilder();
        int indent = 0;
        char pre = 0;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) continue;
            if (c == ']' || c == '}') indent--;
            if (pre == '[' || pre == '{' || pre == ',' || c == ']' || c == '}') {
                sb.append('\n');
                for (int i = 0; i < indent; i++) sb.append("  ");
            }       
            sb.append(c);
            if (c == '[' || c == '{') indent++;
            pre = c;
        }       
        return sb.toString();
}
}
