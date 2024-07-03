package pro.service.rule

import org.springframework.stereotype.Service

import java.util.concurrent.ConcurrentHashMap

/**
 * @description:
 * @author ：dukelewis
 * @date: 2024/7/2
 * @Copyright ： https://github.com/DukeLewis
 */
@Service("groovyLocalCacheRuleEngine")
class GroovyLocalCacheRuleEngine extends RuleEngineConfig implements RuleEngine{
    /**
     * 缓存对应脚本， key 唯一标识， val 闭包
     */
    static final Map<String, Closure<?>> ruleMap = new ConcurrentHashMap<>()

    @Override
    Boolean addRule(String ruleKey, Closure<?> script) {
        return ruleMap.put(ruleKey, script) != null
    }

    @Override
    Boolean addRule(String ruleKey, String scriptStr) {
        return ruleMap.put(ruleKey, SHELL.evaluate(scriptStr) as Closure<?>) != null
    }

    @Override
    def execute(String ruleKey) {
        if (ruleMap[ruleKey] != null) {
            return ruleMap[ruleKey].call()
        }
        return null
    }
}
