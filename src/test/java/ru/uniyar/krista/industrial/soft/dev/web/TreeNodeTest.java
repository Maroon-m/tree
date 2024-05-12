package ru.uniyar.krista.industrial.soft.dev.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class TreeNodeTest {

    private TreeNode root;

    @BeforeEach
    void setUp() {
        root = new TreeNode("Root", 0);
    }

    @Test
    void createTree() {
        // Проверяем, что корневой узел создан правильно
        Assertions.assertEquals("Root", root.getName());
        Assertions.assertEquals(0, root.getId());
        Assertions.assertEquals(0, root.getChildren().size());
    }

    @Test
    void addNode() {
        // Добавляем дочерний узел к корню
        TreeNode child = new TreeNode("Child", 1);
        root.addNode(child);

        // Проверяем, что дочерний узел был добавлен правильно
        Assertions.assertEquals(1, root.getChildren().size());
        Assertions.assertEquals(child, root.getChildren().get(0));
        Assertions.assertEquals(root, child.getParent());
    }

    @Test
    void findChildNodeByName() {
        // Добавляем дочерний узел к корню
        TreeNode child = new TreeNode("Child", 1);
        root.addNode(child);

        // Ищем дочерний узел по имени
        TreeNode foundChild = root.findChildNodeByName("Child");

        // Проверяем, что дочерний узел был найден правильно
        Assertions.assertNotNull(foundChild);
        Assertions.assertEquals(child, foundChild);
    }

    @Test
    void deleteChildNodeByName() {
        // Добавляем дочерний узел к корню
        TreeNode child = new TreeNode("Child", 1);
        root.addNode(child);

        // Удаляем дочерний узел по имени
        root.deleteChildNodeByName("Child");

        // Проверяем, что дочерний узел был удален правильно
        Assertions.assertEquals(0, root.getChildren().size());
    }

    @Test
    void deleteChildNodeById() {
        // Добавляем дочерний узел к корню
        TreeNode child = new TreeNode("Child", 1);
        root.addNode(child);

        // Удаляем дочерний узел по идентификатору
        root.deleteChildNodeById(1);

        // Проверяем, что дочерний узел был удален правильно
        Assertions.assertEquals(0, root.getChildren().size());
    }

    @Test
    void deleteAllChildNodes() {
        // Добавляем два дочерних узла к корню
        TreeNode child1 = new TreeNode("Child 1", 1);
        TreeNode child2 = new TreeNode("Child 2", 2);
        root.addNode(child1);
        root.addNode(child2);

        // Удаляем все дочерние узлы
        root.deleteAllChildNodes();

        // Проверяем, что все дочерние узлы были удалены правильно
        Assertions.assertEquals(0, root.getChildren().size());
    }

    @Test
    void changeNodeName() {
        // Изменяем имя корневого узла
        root.setName("New Root");

        // Проверяем, что имя корневого узла было изменено правильно
        Assertions.assertEquals("New Root", root.getName());
    }

    @Test
    void iterateTree() {
        // Добавляем два уровня дочерних узлов к корню
        TreeNode child1 = new TreeNode("Child 1", 1);
        TreeNode child2 = new TreeNode("Child 2", 2);
        root.addNode(child1);
        root.addNode(child2);

        TreeNode child11 = new TreeNode("Child 1.1", 3);
        TreeNode child12 = new TreeNode("Child 1.2", 4);
        child1.addNode(child11);
        child1.addNode(child12);

        TreeNode child21 = new TreeNode("Child 2.1", 5);
        TreeNode child22 = new TreeNode("Child 2.2", 6);
        child2.addNode(child21);
        child2.addNode(child22);

        // Проверяем глубину дерева (максимальная длина пути от корня до листа)
        Assertions.assertEquals(3, root.getDepth());

        // Проверяем, что все узлы были посещены в порядке обхода в глубину
        List<TreeNode> visitedNodes = new ArrayList<>();
        root.iterateNodes(new TreeNode.TreeIteratorHandler() {
            @Override
            public void handleNode(int level, TreeNode node) {
                visitedNodes.add(node);
            }
        });

        Assertions.assertEquals(7, visitedNodes.size());
        Assertions.assertEquals(root, visitedNodes.get(0));
        Assertions.assertEquals(child1, visitedNodes.get(1));
        Assertions.assertEquals(child11, visitedNodes.get(2));
        Assertions.assertEquals(child12, visitedNodes.get(3));
        Assertions.assertEquals(child2, visitedNodes.get(4));
        Assertions.assertEquals(child21, visitedNodes.get(5));
        Assertions.assertEquals(child22, visitedNodes.get(6));
    }

    @Test
    void printTreeToString() {
        // Добавляем два уровня дочерних узлов к корню
        TreeNode child1 = new TreeNode("Child 1", 1);
        TreeNode child2 = new TreeNode("Child 2", 2);
        root.addNode(child1);
        root.addNode(child2);

        TreeNode child11 = new TreeNode("Child 1.1", 3);
        TreeNode child12 = new TreeNode("Child 1.2", 4);
        child1.addNode(child11);
        child1.addNode(child12);

        TreeNode child21 = new TreeNode("Child 2.1", 5);
        TreeNode child22 = new TreeNode("Child 2.2", 6);
        child2.addNode(child21);
        child2.addNode(child22);

        // Печатаем дерево в строку
        String treeString = root.printTreeToString();

        // Проверяем, что дерево было напечатано правильно
        String expectedTreeString = "Root\n" +
                "  Child 1\n" +
                "    Child 1.1\n" +
                "    Child 1.2\n" +
                "  Child 2\n" +
                "    Child 2.1\n" +
                "    Child 2.2\n";
        Assertions.assertEquals(expectedTreeString, treeString);
    }

    @Test
    void printTreeToHtml() throws IOException {
        // Добавляем два уровня дочерних узлов к корню
        TreeNode child1 = new TreeNode("Child 1", 1);
        TreeNode child2 = new TreeNode("Child 2", 2);
        root.addNode(child1);
        root.addNode(child2);

        TreeNode child11 = new TreeNode("Child 1.1", 3);
        TreeNode child12 = new TreeNode("Child 1.2", 4);
        child1.addNode(child11);
        child1.addNode(child12);

        TreeNode child21 = new TreeNode("Child 2.1", 5);
        TreeNode child22 = new TreeNode("Child 2.2", 6);
        child2.addNode(child21);
        child2.addNode(child22);

        // Печатаем дерево в HTML
        String treeHtml = root.printTreeToHtml();

        // Сохраняем HTML-код в файл
        File file = new File("tree.html");
        file.createNewFile();
        java.nio.file.Files.write(file.toPath(), treeHtml.getBytes());

        // Открываем HTML-файл в браузере
        java.awt.Desktop.getDesktop().browse(file.toURI());
    }

}
