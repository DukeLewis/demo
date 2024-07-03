package pro.service.rule

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import org.springframework.stereotype.Service
import pro.dao.RuleEngineDao
import pro.domain.RuleEngineEntity

import javax.annotation.Resource

/**
 * @description:
 * @author ：dukelewis
 * @date: 2024/7/2
 * @Copyright ： https://github.com/DukeLewis
 */
@Service("groovyDatabaseRuleEngine")
class GroovyDatabaseRuleEngine extends RuleEngineConfig implements RuleEngine {

    @Resource
    private RuleEngineDao ruleEngineDao;

    @Override
    Boolean addRule(String ruleKey, Closure<?> script) {
        throw new RuntimeException("该类不能使用该方法")
    }

    @Override
    Boolean addRule(String ruleKey, String scriptStr) {
        return ruleEngineDao.save(RuleEngineEntity.builder()
                .ruleKey(ruleKey)
                .groovyScript(scriptStr)
                .build())
    }

    @Override
    def execute(String ruleKey) {
        RuleEngineEntity engineEntity = ruleEngineDao.getOne(new QueryWrapper<RuleEngineEntity>()
                .eq("rule_key", ruleKey)
                .eq("is_deleted", 0)
        )
        if (engineEntity != null) {
            def closure = SHELL.evaluate(engineEntity.getGroovyScript()) as Closure<?>
            return closure.call()
        }
        return null
    }
}
