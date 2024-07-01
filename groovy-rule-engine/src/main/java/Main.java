import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/6/30
 * @Copyright： https://github.com/DukeLewis
 */
public class Main {
    private static final GroovyClassLoader CLASS_LOADER = new GroovyClassLoader();

    public static void main(String[] args) {
        RuleEngine engine = new GroovyRuleEngine();
        // 添加规则
        engine.addRule(new GroovyRule("Check high priority",
                "{ -> 1 == 1 }",
        "{ -> println 'Urgent task needs immediate action!' }"
        ));
        engine.execute();
    }
}
