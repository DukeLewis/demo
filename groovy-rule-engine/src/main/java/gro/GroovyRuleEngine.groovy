package gro

class GroovyRuleEngine implements RuleEngine {
    List<GroovyRule> rules = []

    void addRule(GroovyRule rule) {
        rules << rule
    }
    void execute() {
        rules.findAll { it.evaluate() }.each { it.performAction() }
    }
}
