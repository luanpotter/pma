package br.com.dextra.pma.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import xyz.luan.console.parser.Console;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.exceptions.NotLoggedIn;
import br.com.dextra.pma.models.Project;
import br.com.dextra.pma.models.Task;

public final class Wrapper {

    private static final String DOMAIN = "https://dextranet.dextra.com.br/pma/services/";

    private static final String TOKEN_FILE_NAME = "token.dat";
    private static final String INVALID_TOKEN_MESSAGE = "token inválido";

    private Wrapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<Project> getProjects(Console console) {
        final List<Project> projects = new ArrayList<>();

        console.tabIn();
        console.result("Fetching projects...");
        final Document document = get("listar_projetos");
        for (Element project : listElements(document, "//projetos/projeto")) {
            console.result("Fetching tasks for project...");
            projects.add(new Project(project));
        }
        console.tabOut();

        return projects;
    }

    public static List<Task> getTasksFromProject(Project project) {
        final List<Task> tasks = new ArrayList<>();

        final Document document = get("listar_atividades", new BasicNameValuePair("projeto", String.valueOf(project.getId())));
        for (Element node : listElements(document, "//atividades/atividade")) {
            tasks.add(new Task(project, node));
        }

        return tasks;
    }

    public static String createDay(Date date, Time start, Time end, Time interval) {
        return post("criar_apontamento_diario",
                new NameValuePair[] {
                    new BasicNameValuePair("data", date.toString()),
                    new BasicNameValuePair("inicio", start.toString()),
                    new BasicNameValuePair("fim", end.toString()),
                    new BasicNameValuePair("intervalo", interval.toString()), });
    }

    public static String createTask(Date date, long taskId, String description, Time duration) {
        return post("criar_apontamento",
                new NameValuePair[] {
                    new BasicNameValuePair("data", date.toString()),
                    new BasicNameValuePair("atividadeId", String.valueOf(taskId)),
                    new BasicNameValuePair("atividadeStatus", "working"),
                    new BasicNameValuePair("esforco", String.valueOf(duration.getRoundedMinutes())),
                    new BasicNameValuePair("descricao", description), });
    }

    private static String tokenCache = null;

    private static String evaluateElement(Document doc, String xpath) {
        List<Element> elementList = listElements(doc, xpath);
        assert elementList.size() == 1;
        return elementList.get(0).getText();
    }

    private static List<Element> listElements(Document doc, String xpath) {
        final XPathFactory xFactory = XPathFactory.instance();

        final XPathExpression<Element> expr = xFactory.compile(xpath, Filters.element());
        return expr.evaluate(doc);
    }

    public static boolean isLoggedIn() {
        File file = new File(TOKEN_FILE_NAME);

        if (file.exists() && isValidToken()) {
            return true;
        }

        return false;
    }

    private static boolean isValidToken() {
        Document doc = getRaw("listar_atividades", true);
        for (Element e : listElements(doc, "//erro")) {
            if (e.getText().equals(INVALID_TOKEN_MESSAGE)) {
                return false;
            }
        }
        return true;
    }

    public static class InvalidLoginException extends Exception {
        private static final long serialVersionUID = 2440114403933514632L;

        public InvalidLoginException(String message) {
            super(message);
        }
    }

    public static void logout() {
        new File(TOKEN_FILE_NAME).delete();
    }

    public static void requestLogin(Console console) throws InvalidLoginException {
        console.result("Logging in. Type your username:");
        String username = console.read();
        console.result("Now type your password:");
        char[] pass = console.readPassword();
        requestAndSaveToken(username, pass);
    }

    private static void requestAndSaveToken(String username, char[] pass) throws InvalidLoginException {
        final Document doc = getRaw("obter_token", false, new BasicNameValuePair("username", username),
                new BasicNameValuePair("password", String.valueOf(pass)));
        int code = getDocumentErrorCode(doc);
        if (code == 0) {
            String token = listElements(doc, "//token").get(0).getText();
            saveToken(token);
        } else {
            throw new InvalidLoginException(evaluateElement(doc, "//erro"));
        }
    }

    private static void saveToken(String token) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TOKEN_FILE_NAME))) {
            writer.println(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getToken() {
        if (tokenCache == null) {
            File file = new File(TOKEN_FILE_NAME);
            if (!file.exists()) {
                throw new NotLoggedIn();
            }
            try (BufferedReader r = new BufferedReader(new FileReader(file))) {
                tokenCache = r.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return tokenCache;
    }

    private static String post(String url, NameValuePair... customParams) {
        Document doc = get(url, customParams);
        return evaluateElement(doc, "//mensagem");
    }

    private static RuntimeException parseErrors(List<Element> elements) {
        if (elements.isEmpty()) {
            return new RuntimeException("Unknown error thrown: request failed, but no message received.");
        }
        StringBuilder messages = new StringBuilder("Some unexpected errors were thrown: ");
        for (Element e : elements) {
            String message = e.getText();
            if (message.equals(INVALID_TOKEN_MESSAGE)) {
                throw new NotLoggedIn();
            }
            messages.append(message);
        }
        return new RuntimeException(messages.toString());
    }

    private static int getDocumentErrorCode(Document doc) {
        return Integer.parseInt(evaluateElement(doc, "//responseType"));
    }

    private static Document get(String url, NameValuePair... customParams) {
        Document response = getRaw(url, true, customParams);
        int code = getDocumentErrorCode(response);
        if (code == 0) {
            return response;
        } else {
            throw parseErrors(listElements(response, "//erro"));
        }
    }

    private static Document getRaw(String url, boolean useToken, NameValuePair... customParams) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(DOMAIN + url);

            List<NameValuePair> params = new ArrayList<>(Arrays.asList(customParams));
            if (useToken) {
                params.add(new BasicNameValuePair("token", getToken()));
            }
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                final SAXBuilder jdomBuilder = new SAXBuilder();
                return jdomBuilder.build(entity.getContent());
            } else {
                throw new IOException("Response expected, none returned...");
            }
        } catch (IOException | IllegalStateException | JDOMException ex) {
            throw new RuntimeException(ex);
        }
    }
}
