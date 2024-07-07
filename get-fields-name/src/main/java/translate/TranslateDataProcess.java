package translate;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.util.StringUtils;
import translate.mock.TranslateManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/7/6
 * @Copyright： https://github.com/DukeLewis
 */
@Slf4j
public class TranslateDataProcess {
    private TranslateManager translateManager;

    private static final String translateSplit = "\n";

    // 预置 moduleName 的翻译结果
    private static Map<String, String> translatedModuleName = null;

    // 不需要翻译的内容对应的 key
    private static HashSet<String> fixedNeedlessTranslateSet = null;

    static {
        translatedModuleName = new HashMap<>();
        translatedModuleName.put("基本信息", "Basic information");

        fixedNeedlessTranslateSet = new HashSet<>();
        fixedNeedlessTranslateSet.add("telephone");
        fixedNeedlessTranslateSet.add("email");
        fixedNeedlessTranslateSet.add("startToEndTime");
    }

    private List<String> getTranslateResultList(Set<String> needlessTranslateSet, List<ResumeRawContent.Module> moduleList) {
        // 翻译后的原始信息
        String translateResultStr = "";
        StringBuilder needTranslateTextBuilder = new StringBuilder();
        for (ResumeRawContent.Module module : moduleList) {
            Boolean isHide = module.getIsHide();
            if (isHide != null && isHide) {
                continue;
            }
            String moduleName = module.getModuleName();
            String moduleType = module.getModuleType();
            // 如果 moduleName 为空，在 needlessTranslateSet 中记录；
            //                未被修改，在 needlessTranslateSet 中记录；
            //                被修改，拼接原内容
            if (StrUtil.isEmpty(moduleName)) {
                needlessTranslateSet.add(moduleType);
            } else if (translatedModuleName.containsKey(moduleName)) {
                needlessTranslateSet.add(moduleType);
            } else {
                // 拼接前判断长度
                if (needTranslateTextBuilder.length() + moduleName.length() + translateSplit.length() > 6000) {
                    // 翻译，清空 needTranslateTextBuilder，保存翻译结果
                    String singleTranslateResult = translateManager.translate(needTranslateTextBuilder.toString());
                    translateResultStr += singleTranslateResult;
                    needTranslateTextBuilder = new StringBuilder();
                }
                // 清空简历中原有的换行符，避免影响后续处理
                needTranslateTextBuilder.append(moduleName.replaceAll("\n", "")).append(translateSplit);
            }
            for (int i = 0; i < module.getModuleItemList().size(); i++) {
                List<ResumeRawContent.ModuleItem> moduleItems = module.getModuleItemList().get(i);
                for (int j = 0; j < moduleItems.size(); j++) {
                    ResumeRawContent.ModuleItem moduleItem = moduleItems.get(j);
                    for (int k = 0; k < moduleItem.getFormItemList().size(); k++) {
                        ResumeRawContent.FormItem formItem = moduleItem.getFormItemList().get(k);
                        String itemValue = formItem.getValue();
                        String itemKey = formItem.getKey();
                        // 记录唯一key
                        String formattedItemKey = itemKey + i + j + k;
                        if (needlessTranslateSet.contains(formattedItemKey) || fixedNeedlessTranslateSet.contains(itemKey)) {
                            continue;
                        }
                        if (StrUtil.isBlank(itemValue)) {
                            needlessTranslateSet.add(formattedItemKey);
                            continue;
                        }
                        Element itemValueHtml = Jsoup.parse(itemValue).body();
                        String pureItemValue = itemValueHtml.text().replaceAll("\n", "");
                        if (needTranslateTextBuilder.length() + pureItemValue.length() + translateSplit.length() > 6000) {
                            // 翻译，清空 needTranslateTextBuilder
                            String singleTranslateResult = translateManager.translate(needTranslateTextBuilder.toString());
                            translateResultStr += singleTranslateResult;
                            needTranslateTextBuilder = new StringBuilder();
                        }
                        // 调用递归添加元素
                        handleHtmlValue(itemValueHtml, needTranslateTextBuilder);
                    }
                }
            }
        }
        // 翻译
        String singleTranslateResult = translateManager.translate(needTranslateTextBuilder.toString());
        translateResultStr += singleTranslateResult;

        // 切分翻译结果
        List<String> translateResult = Arrays.asList(translateResultStr.split(translateSplit));

        return translateResult;
    }

    /**
     * 递归拼接需要翻译的内容
     *
     * @param itemValueHtml
     * @param needTranslateTextBuilder
     */
    private void handleHtmlValue(Element itemValueHtml, StringBuilder needTranslateTextBuilder) {
        for (Node childNode : itemValueHtml.childNodes()) {
            if (childNode instanceof TextNode) {
                TextNode textNode = (TextNode) childNode;
                String text = textNode.text();
                if (StrUtil.isBlank(text)) {
                    continue;
                }
                needTranslateTextBuilder.append(text).append(translateSplit);
            } else if (childNode instanceof Element) {
                Element element = (Element) childNode;
                String text = element.text();
                if (StrUtil.isBlank(text)) {
                    continue;
                }
                handleHtmlValue(element, needTranslateTextBuilder);
            } else {
                log.warn("未处理类型：{}", childNode.getClass());
            }
        }
    }

    private void handleTranslateResultList(Set<String> needlessTranslateSet, List<ResumeRawContent.Module> moduleList, List<String> translateResult) {
        AtomicInteger counter = new AtomicInteger(0);
        for (ResumeRawContent.Module module : moduleList) {
            Boolean isHide = module.getIsHide();
            if (isHide != null && isHide) {
                continue;
            }
            String moduleName = module.getModuleName();
            String moduleType = module.getModuleType();
            if (needlessTranslateSet.contains(moduleType)) {
                if (translatedModuleName.containsKey(moduleName)) {
                    module.setModuleName(translatedModuleName.get(moduleName));
                }
            } else {
                module.setModuleName(translateResult.get(counter.getAndIncrement()));
            }

            for (int i = 0; i < module.getModuleItemList().size(); i++) {
                List<ResumeRawContent.ModuleItem> moduleItems = module.getModuleItemList().get(i);
                for (int j = 0; j < moduleItems.size(); j++) {
                    ResumeRawContent.ModuleItem moduleItem = moduleItems.get(j);
                    for (int k = 0; k < moduleItem.getFormItemList().size(); k++) {
                        ResumeRawContent.FormItem formItem = moduleItem.getFormItemList().get(k);
                        String itemKey = formItem.getKey();
                        // 记录唯一key
                        String formattedItemKey = itemKey + i + j + k;
                        // 判断是否需要赋值
                        if (needlessTranslateSet.contains(formattedItemKey) || fixedNeedlessTranslateSet.contains(itemKey)) {
                            continue;
                        }

                        // 设置非格式化输出，防止出现多余空格
                        Document itemValueHtml = Jsoup.parse(formItem.getValue());
                        itemValueHtml.outputSettings().prettyPrint(false);
                        Element itemValueBody = itemValueHtml.body();
                        setHtmlValue(itemValueBody, translateResult, counter);
                        String formItemValue = CharSequenceUtil.subAfter(itemValueBody.toString(), "<body>", false);
                        String s1 = CharSequenceUtil.subBefore(formItemValue, "</body>", true);

                        formItem.setValue(s1);
                    }
                }
            }
        }
    }

    private void setHtmlValue(Element itemValueHtml, List<String> translateResult, AtomicInteger counter) {
        for (Node childNode : itemValueHtml.childNodes()) {
            if (childNode instanceof TextNode) {
                TextNode textNode = (TextNode) childNode;
                String text = textNode.text();
                if (StrUtil.isBlank(text)) {
                    continue;
                }
                textNode.text(translateResult.get(counter.getAndIncrement()));
            } else if (childNode instanceof Element) {
                Element element = (Element) childNode;
                String text = element.text();
                if (StrUtil.isBlank(text)) {
                    continue;
                }
                setHtmlValue(element, translateResult, counter);
            } else {
                log.warn("简历翻译 未处理类型：{}", childNode.getClass());
            }
        }
    }

    public String translate(String resumeRawContent) {
        // 保存不需要翻译（空、不需要翻译或预置翻译）的 moduleName 或 value
        Set<String> needlessTranslateSet = new HashSet<>();

        // 简历 JSON 字符串转对象
        ResumeRawContent rawContent = JSONUtil.toBean(resumeRawContent, ResumeRawContent.class);
        List<ResumeRawContent.Module> moduleList = rawContent.getModuleList();

        // 获取翻译后的信息，翻译结果按顺序排列
        List<String> translateResult = this.getTranslateResultList(needlessTranslateSet, moduleList);

        // 将翻译后的结果封装进简历
        this.handleTranslateResultList(needlessTranslateSet, moduleList, translateResult);

        // 简历对象转 JSON 字符串
        return JSONUtil.toJsonStr(rawContent);
    }
}
