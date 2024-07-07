package translate;

import lombok.Data;
import java.util.List;

@Data
public class ResumeRawContent {

    private List<Module> moduleList;

    @Data
    public static class ModuleItem {
        private String moduleItemName;
        private List<FormItem> formItemList;
    }

    @Data
    public static class FormItem {
        private String key;
        private String label;
        private String value;
        private Boolean isImage;
    }

    @Data
    public static class Module {
        private String moduleType;
        private String moduleName;
        private Boolean isHide;
        private List<List<ModuleItem>> moduleItemList;
    }

}
