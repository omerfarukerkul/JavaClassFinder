import org.json.JSONArray;

import java.io.FileInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

public class ReflectJava {
    static Pattern patterns = Pattern.compile("^[a-zA-Z.]+.class$");

    @SuppressWarnings("resource")
    public static JSONArray getCrunchifyClassNamesFromJar(String crunchifyJarName) {
        JSONArray listofClasses = new JSONArray();
        try {
            JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(crunchifyJarName));
            JarEntry crunchifyJar;

            while (true) {
                crunchifyJar = crunchifyJarFile.getNextJarEntry();
                if (crunchifyJar == null) {
                    break;
                }
                if ((crunchifyJar.getName().endsWith(".class"))) {
                    String className = crunchifyJar.getName().replaceAll("/", "\\.");
                    if (patterns.matcher(className).matches()) {
                        String myClass = className.substring(0, className.lastIndexOf('.'));
                        listofClasses.put(myClass);

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Oops.. Encounter an issue while parsing jar" + e.toString());
        }
        return listofClasses;
    }
}


