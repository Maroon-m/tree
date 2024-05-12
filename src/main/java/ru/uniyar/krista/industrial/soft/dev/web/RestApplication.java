package ru.uniyar.krista.industrial.soft.dev.web;

import javax.ws.rs.core.Application;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Web-приложение в котором регистрируются все ресурсы.
 */
public class RestApplication extends Application {

    private List<TreeNode> tree = new ArrayList<>();

    public RestApplication() {
        TreeNode root = new TreeNode("Root", 0);
        tree.add(root);

        TreeNode child1 = new TreeNode("Child 1", 1);
        root.addNode(child1);

        TreeNode child11 = new TreeNode("Child 1.1", 2);
        child1.addNode(child11);

        TreeNode child12 = new TreeNode("Child 1.2", 3);
        child1.addNode(child12);

        TreeNode child2 = new TreeNode("Child 2", 4);
        root.addNode(child2);

        TreeNode child21 = new TreeNode("Child 2.1", 5);
        child2.addNode(child21);

        TreeNode child22 = new TreeNode("Child 2.2", 6);
        child2.addNode(child22);
    }

    /**
     * Возвращает список всех ресурсов web-приложения.
     * @return список всех ресурсов web-приложения.
     */
    @Override
    public Set<Object> getSingletons() {
        Set<Object> resources = new HashSet<>();
        resources.add(new TreePresentationController(tree));
        resources.add(new LoginController());
        return resources;
    }
}
