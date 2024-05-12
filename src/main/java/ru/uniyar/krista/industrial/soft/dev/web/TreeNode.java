package ru.uniyar.krista.industrial.soft.dev.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private final int id;
    private String name;
    private final List<TreeNode> children;
    private TreeNode parent;

    public TreeNode(String name, int id) {
        this.name = name;
        this.id = id;
        this.children = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addNode(TreeNode node) {
        children.add(node);
        node.parent = this;
    }

    public void deleteNode(String name) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getName().equals(name)) {
                children.remove(i);
                return;
            }
        }
    }

    public TreeNode getParent() {
        return parent;
    }


    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }

    public TreeNode findChildNodeByName(String child) {
        for (TreeNode node : children) {
            if (node.getName().equals(child)) {
                return node;
            }
        }
        return null;
    }

    public void deleteChildNodeByName(String child) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getName().equals(child)) {
                children.remove(i);
                return;
            }
        }
    }

    public void deleteChildNodeById(int id) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getId() == id) {
                children.remove(i);
                return;
            }
        }
    }

    public void deleteAllChildNodes() {
        children.clear();
    }

    public int getDepth() {
        if (children.isEmpty()) {
            return 1;
        }
        int maxDepth = 0;
        for (TreeNode child : children) {
            maxDepth = Math.max(maxDepth, child.getDepth() + 1);
        }
        return maxDepth;
    }


    public interface TreeIteratorHandler {

        void handleNode(int level, TreeNode node);

    }

    public void iterateNodes(TreeIteratorHandler handler) {
        iterateNodes(handler, 0);
    }

    private void iterateNodes(TreeIteratorHandler handler, int level) {
        handler.handleNode(level, this);
        for (TreeNode child : children) {
            child.iterateNodes(handler, level + 1);
        }
    }


    public String printTreeToString() {
        StringBuilder result = new StringBuilder();
        printTreeToString(result, "");
        return result.toString();
    }

    private void printTreeToString(StringBuilder result, String prefix) {
        result.append(prefix).append(name).append("\n");
        for (TreeNode child : children) {
            child.printTreeToString(result, prefix + "  ");
        }
    }

    public String printTreeToHtml() {
        StringBuilder result = new StringBuilder();
        result.append("<ul>");
        printTreeToHtml(result, "");
        result.append("</ul>");
        return result.toString();
    }

    private void printTreeToHtml(StringBuilder result, String prefix) {
        result.append("<li>").append(name);
        for (TreeNode child : children) {
            result.append("<ul>");
            child.printTreeToHtml(result, prefix + "&nbsp;&nbsp;");
            result.append("</ul>");
        }
        result.append("</li>");
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка преобразования дерева в JSON", e);
        }
    }

    public static TreeNode fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, TreeNode.class);
        } catch (JsonParseException | JsonMappingException e) {
            throw new RuntimeException("Ошибка преобразования JSON в дерево", e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения JSON-файла", e);
        }
    }
}
