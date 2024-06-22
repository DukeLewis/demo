import javassist.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.*;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author duke
 */
public class TimingAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new TimingClassFileTransformer());
    }

    public static class TimingClassFileTransformer implements ClassFileTransformer {
        private static final Set<String> classNameSet = new HashSet<>();

        static {
            loadClassNamesFromConfig();
        }
        private static void loadClassNamesFromConfig() {
            Properties properties = new Properties();
            try (InputStream input = TimingClassFileTransformer.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new RuntimeException("Sorry, unable to find config.properties");
                }
                properties.load(input);
                String classNames = properties.getProperty("class.names");
                if (classNames != null) {
                    for (String className : classNames.split(",")) {
                        classNameSet.add(className.trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            className = className.replace("/", ".");
            if (!classNameSet.contains(className)) {
                return classfileBuffer;
            }
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.get(className);
                for (CtMethod method : ctClass.getDeclaredMethods()) {
                    System.out.println(method);
                    addTiming(method);
                }
                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return classfileBuffer;
        }

        private void addTiming(CtMethod method) throws CannotCompileException {
            System.out.println("Adding timing to method: " + method.getName());
            method.addLocalVariable("startTime", CtClass.longType);
            method.insertBefore("startTime = System.currentTimeMillis();");
            method.insertAfter("System.out.println(\"Method " + method.getName() + " executed in \" + " +
                    "(System.currentTimeMillis() - startTime) + \" ms.\");");
        }
    }
}
