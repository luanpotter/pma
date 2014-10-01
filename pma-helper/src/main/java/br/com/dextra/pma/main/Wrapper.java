package br.com.dextra.pma.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import xyz.luan.console.parser.Output;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.models.Project;
import br.com.dextra.pma.models.Task;

public final class Wrapper {

    private static final String DOMAIN = "https://dextranet.dextra.com.br/pma/services/";

    private Wrapper() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static List<Project> getProjects() {
        final List<Project> projects = new ArrayList<>();

        final Document document = get("listar_projetos");
        for (Element project : listElements(document, "//projetos/projeto")) {
            projects.add(new Project(project));
        }

        return projects;
    }

    public static List<Task> getTasksFromProject(long projectId) {
        final List<Task> tasks = new ArrayList<>();

        final Document document = get("listar_atividades", new BasicNameValuePair("projeto", String.valueOf(projectId)));
        for (Element node : listElements(document, "//atividades/atividade")) {
            tasks.add(new Task(node));
        }

        return tasks;
    }

    public static String[] createDay(Date date, Time start, Time end, Time interval) {
        return new Output(post("criar_apontamento_diario", new NameValuePair[] {
                new BasicNameValuePair("data", date.toString()),
                new BasicNameValuePair("inicio", start.toString()),
                new BasicNameValuePair("fim", end.toString()),
                new BasicNameValuePair("intervalo", interval.toString()),
        }));
    }

    public static Output createTask(Date date, long taskId, String description, int duration) {
        return new Output(post("criar_apontamento", new NameValuePair[] {
                new BasicNameValuePair("data", date.toString()),
                new BasicNameValuePair("atividadeId", String.valueOf(taskId)),
                new BasicNameValuePair("atividadeStatus", "working"),
                new BasicNameValuePair("esforco", String.valueOf(duration)),
                new BasicNameValuePair("descricao", description),
        }));
    }

    private static String tokenCache = null;

    private static String evaluateElement(Document doc, String xpath) {
        return listElements(doc, xpath).get(0).getText();        
    }

    private static List<Element> listElements(Document doc, String xpath) {
        final XPathFactory xFactory = XPathFactory.instance();
        
        final XPathExpression<Element> expr = xFactory.compile(xpath, Filters.element());
        return expr.evaluate(doc);
    }
    
    public static String getToken() {
        if (tokenCache == null) {
            try (BufferedReader r = new BufferedReader(new FileReader(new File(System.getProperty("user.home") + "/.pma_token")))) {
                tokenCache = r.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return tokenCache;
    }

    private static String post(String url, NameValuePair... customParams) {
        Document doc = get(url, customParams);
        int code = Integer.parseInt(evaluateElement(doc, "//responseType"));
        if (code == 0) {
            return evaluateElement(doc, "//mensagem");
        } else {
            return evaluateElement(doc, "//erro");
        }
    }
    
    private static Document get(String url, NameValuePair... customParams) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(DOMAIN + url);

            List<NameValuePair> params = new ArrayList<>(Arrays.asList(customParams));
            params.add(new BasicNameValuePair("token", getToken()));
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
