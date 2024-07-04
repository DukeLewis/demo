package pro.service.rule

import org.springframework.stereotype.Component

import javax.annotation.PreDestroy
import java.util.concurrent.ConcurrentHashMap

/**
 * @description:
 * @author ：dukelewis
 * @date: 2024/7/2
 * @Copyright ： https://github.com/DukeLewis
 */
@Component
class RuleEngineConfig {
    /**
     * groovy 类加载器
     */
    protected static final GroovyClassLoader CLASS_LOADER = new GroovyClassLoader()

    /**
     * groovy 命令行执行器
     */
    protected static GroovyShell SHELL = new GroovyShell()

    /**
     * 缓存动态生成的类， key ： 动态类对应的 key， val ： 动态生成的类
     */
    protected static Map<String, Object> dynamicGenerateClassMap = new ConcurrentHashMap<>();

    @PreDestroy
    public void destroy() {
        CLASS_LOADER.clearCache()
    }
}
