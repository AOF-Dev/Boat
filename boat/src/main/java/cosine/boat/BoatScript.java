package cosine.boat;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

import static cosine.boat.LoadMe.*;

public class BoatScript {

    public static final String BOAT_ENV_WINDOW_WIDTH = "window_width";
    public static final String BOAT_ENV_WINDOW_HEIGHT = "window_height";
    public static final String BOAT_ENV_TMPDIR = "tmpdir";

    private static Pattern variablePattern;
    static {
        variablePattern = Pattern.compile("\\$\\{[a-zA-Z_]+\\}");
    }

    public static LinkedList<String[]> parseJson(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[(int)fis.available()];
        fis.read(buffer);
        fis.close();
        String json = new String(buffer, "UTF-8");

        Type type = new TypeToken<LinkedList<String[]>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private Map<String, String> variables;
    private List<String[]> commands;

    private String scriptFile;

    public BoatScript(Map<String, String> envvars, boolean priv, List<String[]> cmds, String file) {
        if (priv) {
            this.variables = new HashMap<>();
            this.variables.putAll(envvars);
        }
        else {
            this.variables = envvars;
        }
        this.commands = new LinkedList<>();
        this.commands.addAll(cmds);
        this.scriptFile = file;
        if (this.scriptFile == null) {
            this.scriptFile = "";
        }
    }

    private String replaceVariables(String str) throws ParseException {
        if (str == null) {
            str = "";
        }
        Matcher m = variablePattern.matcher(str);
        while (m.find()) {
            String varRef = m.group();
            String varName = varRef.substring(2, varRef.length() - 1);
            String varValue = this.variables.get(varName);
            if (varValue == null) {
                varValue = "";
            }
            str = str.replace(varRef, varValue);
        }
        return str;
    }

    public void execute() {
        int line = 0;
        try {
            for (; line < this.commands.size(); line++) {
                String[] args = this.commands.get(line);
                if (args == null) {
                    continue;
                }
                for (int i = 1; i < args.length; i++) {
                    args[i] = replaceVariables(args[i]);
                }
                switch (args[0]) {
                    case "patchLinker": {
                        patchLinker();
                        break;
                    }
                    case "setenv": {
                        setenv(args[1], args[2]);
                        break;
                    }
                    case "chdir": {
                        chdir(args[1]);
                        break;
                    }
                    case "redirectStdio": {
                        redirectStdio(args[1]);
                        break;
                    }
                    case "dlopen": {
                        dlopen(args[1]);
                        break;
                    }
                    case "dlexec": {
                        String[] finalArgs = new String[args.length - 1];
                        System.arraycopy(args, 1, finalArgs, 0, args.length - 1);
                        dlexec(finalArgs);
                        break;
                    }
                    case "strdef": {
                        String value = args[2];
                        if (value == null) {
                            value = "";
                        }
                        this.variables.put(args[1], value);
                        break;
                    }
                    case "strcat": {
                        String value = this.variables.get(args[1]);
                        if (value == null) {
                            value = "";
                        }
                        StringBuilder builder = new StringBuilder(value);
                        for (int i = 2; i < args.length; i++) {
                            if (args[i] != null) {
                                builder.append(args[i]);
                            }
                        }
                        this.variables.put(args[1], builder.toString());
                        break;
                    }
                    case "json": {
                        if (this.scriptFile.equals(args[1])) {
                            throw new Exception("Recursive script!");
                        }
                        LinkedList<String[]> includes = parseJson(args[1]);
                        new BoatScript(this.variables, false, includes, args[1]).execute();
                        break;
                    }
                    default : {
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception occurred, " + this.scriptFile + ":" + line);
            System.out.println(this.commands.get(line));
            e.printStackTrace();
        }
    }
}
