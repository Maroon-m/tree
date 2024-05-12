package ru.uniyar.krista.industrial.soft.dev.web;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Контроллер отвечающий за представление дерева.
 */
@Path("/")
public class TreePresentationController {
    private final List<TreeNode> tree;

    /**
     * Запоминает дерево, с которым будет работать.
     * @param tree дерево, с которым будет работать контроллер.
     */
    public TreePresentationController(List<TreeNode> tree) {
        this.tree = tree;
    }

    /**
     * Пример вывода простого текста.
     */
    @GET
    @Path("example")
    @Produces("text/plain")
    public String getSimpleText() {
        return "hello world";
    }

    /**
     * Выводит HTML-страницу с деревом, ссылками на страницы редактирования, добавления и удаления.
     * @return HTML-страница с деревом.
     */
    @GET
    @Path("/")
    @Produces("text/html")
    public String getTree() {
        return getTreeHtml(tree, "");
    }

    /**
     * Рекурсивно генерирует HTML-код для дерева.
     * @param tree дерево для генерации HTML-кода.
     * @param prefix префикс для элементов дерева (используется для отступов).
     * @return HTML-код для дерева.
     */
    private String getTreeHtml(List<TreeNode> tree, String prefix) {
        StringBuilder result = new StringBuilder();
        result.append("<ul style=\"font-size: 20px; font-weight: bold;\">");
        for (TreeNode node : tree) {
            result.append("<p></p><li>" + prefix + node.getName() + " ");
            result.append("<a href=\"edit/" + node.getId() + "\" style=\"color: black;\">Редактировать</a> ");
            result.append("<a href=\"delete/" + node.getId() + "\" style=\"color: black;\">Удалить</a> ");
            result.append("<a href=\"add/" + node.getId() + "\" style=\"color: black;\">Добавить</a>");
            if (!node.getChildren().isEmpty()) {
                result.append(getTreeHtml(node.getChildren(), prefix + "&nbsp;&nbsp;"));
            }
            result.append("</li>");
        }
        result.append("</ul>");
        return result.toString();
    }


    /**
     * Отображает форму для добавления дочернего узла к узлу с указанным идентификатором.
     * @param parentId идентификатор родительского узла.
     * @return HTML-форма для добавления дочернего узла.
     */
    @GET
    @Path("/add/{parentId}")
    @Produces("text/html")
    public String getAddForm(@PathParam("parentId") int parentId) {
        TreeNode parentNode = getNodeById(parentId, tree);
        assert parentNode != null;
        String result =
                "<html>" +
                        "  <head>" +
                        "    <title>Добавление дочернего узла</title>" +
                        "  </head>" +
                        "  <body>" +
                        "    <h1>Добавление дочернего узла</h1>" +
                        "    <form method=\"post\" action=\"/add_item/" + parentId + "\">" +
                        "      <p>Имя дочернего узла</p>" +
                        "      <input type=\"text\" name=\"childName\" placeholder=\"Имя дочернего узла\" />" +
                        "      <input type=\"submit\" value=\"Добавить\" />" +
                        "    </form>" +
                        "  </body>" +
                        "</html>";
        return result;
    }



    /**
     * Добавляет дочерний узел к узлу с указанным идентификатором.
     * @param parentId идентификатор родительского узла.
     * @param childName имя дочернего узла.
     * @return перенаправление на основную страницу с деревом.
     */
    @POST
    @Path("/add_item/{parentId}")
    @Produces("text/html")
    public Response addItem(@PathParam("parentId") int parentId, @FormParam("childName") String childName) {
        TreeNode parentNode = getNodeById(parentId, tree);
        assert parentNode != null;
        parentNode.addNode(new TreeNode(childName, generateId(tree)));
        try {
            return Response.seeOther(new URI("/")).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ошибка построения URI для перенаправления");
        }
    }



    /**
     * Выводит страничку для редактирования одного элемента дерева.
     * @param id идентификатор элемента дерева.
     * @return страничка для редактирования одного элемента дерева.
     */
    @GET
    @Path("/edit/{id}")
    @Produces("text/html")
    public String getEditPage(@PathParam("id") int id) {
        TreeNode node = getNodeById(id, tree);
        String result =
                "<html>" +
                        "  <head>" +
                        "    <title>Редактирование элемента дерева</title>" +
                        "  </head>" +
                        "  <body>" +
                        "    <h1>Редактирование элемента дерева</h1>" +
                        "    <form method=\"post\" action=\"/edit/" + id + "\">" +
                        "      <p>Значение</p>" +
                        "      <input type=\"text\" name=\"value\" value=\"" + node.getName() +"\"/>" +
                        "      <input type=\"submit\"/>";
        result +=
                "            </form>" +
                        "  </body>" +
                        "</html>";
        return result;
    }

    /**
     * Редактирует элемент дерева на основе полученных данных.
     * @param id идентификатор элемента дерева.
     * @return перенаправление на основную страницу с деревом.
     */
    @POST
    @Path("/edit/{id}")
    @Produces("text/html")
    public Response editItem(@PathParam("id") int id, @FormParam("value") String itemValue) {
        try {
            TreeNode node = getNodeById(id, tree);
            node.setName(itemValue);
            return Response.seeOther(new URI("/")).build();
        } catch (IllegalArgumentException | URISyntaxException e) {
            return Response.status(404).entity("Узел с идентификатором " + id + " не найден").build();
        }
    }

    /**
     * Удаляет элемент дерева по его идентификатору.
     * @param id идентификатор элемента дерева.
     * @return перенаправление на основную страницу с деревом.
     */
    @GET
    @Path("/delete/{id}")
    @Produces("text/html")
    public Response deleteItem(@PathParam("id") int id) {
        try {
            TreeNode node = getNodeById(id, tree);
            assert node != null;
            TreeNode parentNode = node.getParent();
            parentNode.deleteNode(node.getName());
            return Response.seeOther(new URI("/")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity("Узел с идентификатором " + id + " не найден").build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает узел дерева по его идентификатору.
     * @param id идентификатор узла дерева.
     * @return узел дерева с указанным идентификатором.
     */
    private TreeNode getNodeById(int id, List<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            if (node.getId() == id) {
                return node;
            }
            List<TreeNode> children = node.getChildren();
            if (!children.isEmpty()) {
                TreeNode result = getNodeById(id, children);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }


    /**
     * Генерирует новый уникальный идентификатор для узла дерева.
     * @return новый уникальный идентификатор.
     */
    private int generateId(List<TreeNode> nodes) {
        int id = 0;
        for (TreeNode node : nodes) {
            if (node.getId() > id) {
                id = node.getId();
            }
            List<TreeNode> children = node.getChildren();
            if (!children.isEmpty()) {
                id = Math.max(id, generateId(children));
            }
        }
        return id + 1;
    }
}
