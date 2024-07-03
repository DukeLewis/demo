package pro;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pro.service.parse.ParseGroovyTemplate;
import pro.service.rule.RuleEngine;
import pro.service.parse.ParseGroovyTemplateImpl;
import pro.service.test.TestGroovyEngine;

import javax.annotation.Resource;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/7/2
 * @Copyright： https://github.com/DukeLewis
 */
@SpringBootTest
@Slf4j
public class ApiTest {
    @Resource(name = "groovyLocalCacheRuleEngine")
    private RuleEngine groovyLocalCacheRuleEngine;

    @Resource
    private ParseGroovyTemplate parseGroovyTemplateImpl;

    @Test
    public void test() {
        System.out.println("hello");
    }

    @Test
    public void testExecuteScript() {
        log.info("添加规则，key：{}， script：{}", "test01", "{ -> println 'Urgent task needs immediate action!' }");
        groovyLocalCacheRuleEngine.addRule("test01", "{ -> println 'Urgent task needs immediate action!' }");
        groovyLocalCacheRuleEngine.execute("test01");
        log.info("执行成功");
    }

    @Test
    public void testScriptTemplate() {
        try {
            TestGroovyEngine testGroovyEngine = parseGroovyTemplateImpl.parseTemplate("TestGroovyEngineTemplate", "test01");
            System.out.println(testGroovyEngine.run(groovyLocalCacheRuleEngine));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
