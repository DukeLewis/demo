package gro

class GroovyRule {
    String description
    Closure<Boolean> condition
    Closure<Void> action


    public static GroovyShell SHELL = new GroovyShell()

    GroovyRule(String description, String condition, String action) {
        this(description, SHELL.evaluate(condition) as Closure<Boolean>, SHELL.evaluate(action) as Closure<Void>)
    }

    GroovyRule(String description, Closure<Boolean> condition, Closure<Void> action) {
        this.description = description
        this.condition = condition
        this.action = action
    }

    boolean evaluate() {
        return condition.call()
    }

    void performAction() {
        action.call()
    }
}

