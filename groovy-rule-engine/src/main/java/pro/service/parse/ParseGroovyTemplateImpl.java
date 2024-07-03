package pro.service.parse;

import org.springframework.stereotype.Service;
import pro.dao.TemplateScriptDao;
import pro.domain.TemplateScript;
import pro.service.rule.RuleEngineConfig;
import pro.service.test.TestGroovyEngine;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/7/2
 * @Copyright： https://github.com/DukeLewis
 */
@Service
public class ParseGroovyTemplateImpl extends RuleEngineConfig implements ParseGroovyTemplate{
    @Resource
    private TemplateScriptDao templateScriptDao;

    public static File staticDir;

    static {
        try {
            staticDir = new File(ParseGroovyTemplateImpl.class.getClassLoader().getResource("./templates/groovy").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseGroovyTemplate() throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File testGroovyEngineTemplate = new File(staticDir, "TestGroovyEngineTemplate");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(testGroovyEngineTemplate));
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder template = new StringBuilder();
        for(String line; (line = bufferedReader.readLine()) != null; ) {
            template.append(line).append("\n");
        }
        String className = "test01";
        String script = "println \"hello\"\n" +
                "        return false";
        String fullScript = template.toString().formatted(className, script);
        Class<TestGroovyEngine> aClass = CLASS_LOADER.parseClass(fullScript);
        TestGroovyEngine testGroovyEngine = aClass.getConstructor(null).newInstance();
        System.out.println(testGroovyEngine.run(""));
    }

    @Override
    public <T> T parseTemplate(String templateName, String configKey) {
        try {
            File templateFile = new File(staticDir, templateName);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(templateFile));
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder template = new StringBuilder();
            for(String line; (line = bufferedReader.readLine()) != null; ) {
                template.append(line).append("\n");
            }
            TemplateScript templateScript = templateScriptDao.lambdaQuery()
                    .eq(TemplateScript::getConfigKey, configKey)
                    .one();
            String fullScript = template.toString().formatted(configKey, templateScript.getConfigScript());
            Class<T> aClass = CLASS_LOADER.parseClass(fullScript);
            return aClass.getConstructor(null).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
