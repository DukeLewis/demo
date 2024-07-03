package gro;

public interface RuleEngine {
    void addRule(GroovyRule rule);
    void execute();
}
