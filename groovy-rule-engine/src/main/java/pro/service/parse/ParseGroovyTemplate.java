package pro.service.parse;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/7/2
 * @Copyright： https://github.com/DukeLewis
 */
public interface ParseGroovyTemplate {
    /**
     * 解析模板类对应方法
     * @param templateName 模板名称
     * @param configKey 配置 key
     * @param <T> 返回结果类型
     * @return 执行结果
     */
    <T> T parseTemplate(String templateName, String configKey);
}
