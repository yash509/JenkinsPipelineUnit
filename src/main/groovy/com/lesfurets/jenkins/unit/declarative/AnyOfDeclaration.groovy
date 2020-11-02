package com.lesfurets.jenkins.unit.declarative

import static com.lesfurets.jenkins.unit.declarative.GenericPipelineDeclaration.executeWith
import org.springframework.util.AntPathMatcher


class AnyOfDeclaration extends WhenDeclaration {

    List<String> branches = []
    List<Boolean> expressions = []

    def branch(String name) {
        this.branches.add(name)
    }

    def expression(Closure closure) {
        this.expressions.add(closure)
    }

    def expressions(delegate) {
        def exp_result;
        for (def exp in this.expressions) {
            exp_result = executeWith(delegate, exp)
            if (exp_result) {
                return true
            }
        }
        return false
    }

    Boolean execute(Object delegate) {
        boolean br = false;
        boolean exp = false;
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        if (this.branches.size() > 0) {
            for(def branch in this.branches) {
                br = br || antPathMatcher.match(branch, delegate.env.BRANCH_NAME)
            }
        }

        if (this.expressions.size() > 0) {
            exp = expressions(delegate)
        }

        return exp || br
    }
}