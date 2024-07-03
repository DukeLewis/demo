package pro.service.rule

/**
 * @description:
 * @author ：dukelewis
 * @date: 2024/7/2
 * @Copyright ： https://github.com/DukeLewis
 */
interface RuleEngine {
    /**
     * 添加规则
     * @param ruleKey 规则 key
     * @param script 闭包
     * @param T 脚本执行返回的对象类型
     * @return 是否成功
     */
    Boolean addRule(String ruleKey, Closure<?> script);

    /**
     * 添加规则
     * @param ruleKey 规则 key
     * @param scriptStr 闭包字符串
     * @param T 脚本执行返回的对象类型
     * @return
     */
    Boolean addRule(String ruleKey, String scriptStr);

    /**
     * 执行规则对应脚本
     * @param ruleKey 规则 key
     * @return 执行结果
     */
    def execute(String ruleKey);
}
